package com.codewithflow.exptracker.controller;

import com.codewithflow.exptracker.dto.LoginReqDTO;
import com.codewithflow.exptracker.dto.UserReqDTO;
import com.codewithflow.exptracker.dto.UserRespDTO;
import com.codewithflow.exptracker.entity.User;
import com.codewithflow.exptracker.entity.VerificationToken;
import com.codewithflow.exptracker.repository.RoleRepository;
import com.codewithflow.exptracker.repository.VerificationTokenRepository;
import com.codewithflow.exptracker.service.UserService;
import com.codewithflow.exptracker.service.VerificationTokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.text.ParseException;
import java.util.Optional;

@RestController
public class UserController {
    private final UserService userService;
    private final VerificationTokenService tokenService;
    private final VerificationTokenRepository tokenRepository;
    private final RoleRepository roleRepository;
    private final HttpServletRequest request;
    private final HttpServletResponse response;

    @Value("${exptracker.client.url}")
    private String clientUrl;

    public UserController(
            UserService userService,
            VerificationTokenService tokenService,
            VerificationTokenRepository tokenRepository,
            RoleRepository roleRepository,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        this.userService = userService;
        this.tokenService = tokenService;
        this.tokenRepository = tokenRepository;
        this.roleRepository = roleRepository;
        this.request = request;
        this.response = response;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserRespDTO register(@Valid @RequestBody UserReqDTO userReqDTO) throws ParseException {
        return userService.register(userReqDTO);
    }

    @GetMapping("/user/{id}")
    public UserRespDTO getUserById(@PathVariable Long id) {
        System.out.println(roleRepository.findByName("ROLE_USER"));
        return userService.findUserById(id);
    }

    @GetMapping("/registerConfirm")
    @Transactional
    public RedirectView registerConfirm(@RequestParam("token") String token) {
        System.out.println("token is: " + token);

        Optional<VerificationToken> vtoken = tokenService.getVerificationToken(token);

        if (vtoken.isEmpty()) {
            return new RedirectView(clientUrl + "/badUser?type=tokenInvalid");
        }

        if (tokenService.isTokenExpired(vtoken.get())) {
            return new RedirectView(clientUrl + "/badUser?type=tokenExpired&expiredToken=" + token);
        }

        User user = userService.enabledUser(vtoken.get().getUser());

        tokenService.deleteVerificationToken(vtoken.get());

        return new RedirectView(clientUrl + "/accountVerified");
    };

    @GetMapping("/resendVerificationToken")
    @Transactional
    public void resendVerificationToken(@RequestParam("expiredToken") String expiredToken) {

        Optional<VerificationToken> vExpiredToken = tokenService.getVerificationToken(expiredToken);

        if (vExpiredToken.isEmpty()) {
            throw new IllegalArgumentException("Expired token not found");
        }

        tokenService.deleteVerificationToken(vExpiredToken.get());
        tokenRepository.flush();

        userService.resendVerificationEmail(vExpiredToken.get().getUser());

    }

    @PostMapping("/login")
    public void login(@RequestBody LoginReqDTO loginReqDTO) {
        userService.login(loginReqDTO, response);
    }

    @GetMapping("/refreshToken")
    public void refreshToken() {
        userService.refreshToken(request, response);
    }

    @GetMapping("/logout")
    public void logout() {
        userService.logout(request, response);
    }
}
