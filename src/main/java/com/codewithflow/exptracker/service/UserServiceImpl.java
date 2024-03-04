package com.codewithflow.exptracker.service;

import com.codewithflow.exptracker.dto.LoginReqDTO;
import com.codewithflow.exptracker.dto.UserReqDTO;
import com.codewithflow.exptracker.dto.UserRespDTO;
import com.codewithflow.exptracker.entity.IssuedJwt;
import com.codewithflow.exptracker.entity.Role;
import com.codewithflow.exptracker.entity.User;
import com.codewithflow.exptracker.repository.IssuedJwtRepository;
import com.codewithflow.exptracker.repository.RoleRepository;
import com.codewithflow.exptracker.repository.UserRepository;
import com.codewithflow.exptracker.util.exception.ResourceNotFoundException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.util.Collections;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final VerificationTokenService tokenService;
    private final ModelMapper modelMapper;
    private final EmailService emailService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authManager;
    private final UserDetailsService userDetailsService;
    private final IssuedJwtRepository issuedJwtRepository;

    @Value("${exptracker.server.url}")
    private String serverUrl;

    @Value("${spring.profiles.active}")
    private String activeProfile;

    public UserServiceImpl(
            UserRepository userRepository,
            RoleRepository roleRepository,
            VerificationTokenService tokenService,
            ModelMapper modelMapper,
            EmailService emailService,
            PasswordEncoder passwordEncoder,
            JwtService jwtService,
            AuthenticationManager authManager,
            UserDetailsService userDetailsService,
            IssuedJwtRepository issuedJwtRepository
    ) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.tokenService = tokenService;
        this.modelMapper = modelMapper;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authManager = authManager;
        this.userDetailsService = userDetailsService;
        this.issuedJwtRepository = issuedJwtRepository;
    }

    @Override
    public UserRespDTO register(UserReqDTO userReqDTO) throws ParseException {
        User user = convertToEntity(userReqDTO);
        if (roleRepository.findByName("ROLE_USER").isEmpty()) {
            Role role = new Role();
            role.setName("ROLE_USER");
            roleRepository.save(role);
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(Collections.singletonList(roleRepository.findByName("ROLE_USER").get()));
        user.setBlock(false);
        user.setEnabled(false);
        user = userRepository.save(user);

        String token = tokenService.createVerificationTokenForUser(user);

//        emailService.sendSimpleMessage(user.getEmail(), "Account Verification", "Click the link to verify your account: " + serverUrl + "/registerConfirm?token=" + token);

        return convertToDTO(user);
    }

    @Override
    public UserRespDTO findUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return convertToDTO(user);
    }

    @Override
    public User enabledUser(User user) {
        user.setEnabled(true);
        return userRepository.save(user);
    };

    @Override
    public void resendVerificationEmail(User user) {
        String token = tokenService.createVerificationTokenForUser(user);
//        emailService.sendSimpleMessage(user.getEmail(), "[Resend] Account Verification", "Click the link to verify your account: " + serverUrl + "/registerConfirm?token=" + token);
    };

    @Override
    public void login(LoginReqDTO loginReqDTO, HttpServletResponse response) {
        Authentication authentication = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginReqDTO.getUsername(), loginReqDTO.getPassword())
        );
        User user = userRepository.findByEmail(loginReqDTO.getUsername()).orElseThrow();
        jwtService.revokeAllUserTokens(user);
        issueJwtToUser(user, response);
    }

    @Override
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) {

        String refreshToken = null;
        String userEmail;
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            throw new AuthenticationCredentialsNotFoundException("No refresh token found");
        }

        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("JWT_REFRESH_TOKEN")) {
                refreshToken = cookie.getValue();
                break;
            }
        }

        if (refreshToken == null) {
            throw new AuthenticationCredentialsNotFoundException("No refresh token found");
        }

        if (!jwtService.isTokenValid(refreshToken)) {
            throw new BadCredentialsException("Invalid refresh token");
        }

        userEmail = jwtService.extractUsername(refreshToken);
        User user = userRepository.findByEmail(userEmail).orElseThrow();

        jwtService.revokeAllUserTokens(user);
        issueJwtToUser(user, response);
    }

    private void issueJwtToUser(User user, HttpServletResponse response) {

        String jwt = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);
        jwtService.saveUserToken(user, jwt);

        Cookie accessCookie = new Cookie("JWT_ACCESS_TOKEN", jwt);
        accessCookie.setMaxAge(60 * 60);
        accessCookie.setPath("/");
        accessCookie.setSecure(activeProfile.equals("production"));
        response.addCookie(accessCookie);

        Cookie refreshCookie = new Cookie("JWT_REFRESH_TOKEN", refreshToken);
        refreshCookie.setMaxAge(60 * 60);
        refreshCookie.setPath("/");
        refreshCookie.setSecure(activeProfile.equals("production"));
        response.addCookie(refreshCookie);
    }

    public void logout(HttpServletRequest request, HttpServletResponse response) {

        String jwt = null;
        Cookie[] cookies = request.getCookies();

        if (cookies == null) {
            throw new AuthenticationCredentialsNotFoundException("No access token found");
        }

        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("JWT_ACCESS_TOKEN")) {
                jwt = cookie.getValue();
                break;
            }
        }

        if (jwt == null) {
            throw new AuthenticationCredentialsNotFoundException("No access token found");
        }

        IssuedJwt storedToken = issuedJwtRepository.findByToken(jwt).orElse(null);

        if (storedToken != null) {
            storedToken.setExpired(true);
            storedToken.setRevoked(true);
            issuedJwtRepository.save(storedToken);
            SecurityContextHolder.clearContext();
        }

        Cookie cookie = new Cookie("JWT_ACCESS_TOKEN", "");
        Cookie cookie2 = new Cookie("JWT_REFRESH_TOKEN", "");
        cookie.setMaxAge(0);
        cookie2.setMaxAge(0);
        response.addCookie(cookie);
        response.addCookie(cookie2);
    }

    @Override
    public UserRespDTO convertToDTO(User user) {
        return modelMapper.map(user, UserRespDTO.class);
    }

    @Override
    public User convertToEntity(UserReqDTO userReqDTO) throws ParseException {
        return modelMapper.map(userReqDTO, User.class);
    }
}
