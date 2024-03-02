package com.codewithflow.exptracker.service;

import com.codewithflow.exptracker.dto.UserReqDTO;
import com.codewithflow.exptracker.dto.UserRespDTO;
import com.codewithflow.exptracker.entity.Role;
import com.codewithflow.exptracker.entity.User;
import com.codewithflow.exptracker.repository.RoleRepository;
import com.codewithflow.exptracker.util.exception.ResourceNotFoundException;
import com.codewithflow.exptracker.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${exptracker.server.url}")
    private String serverUrl;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, VerificationTokenService tokenService,ModelMapper modelMapper, EmailService emailService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.tokenService = tokenService;
        this.modelMapper = modelMapper;
        this.emailService = emailService;
    }

    @Override
    public UserRespDTO register(UserReqDTO userReqDTO) throws ParseException {
        User user = convertToEntity(userReqDTO);
        if (roleRepository.findByName("ROLE_USER").isEmpty()) {
            Role role = new Role();
            role.setName("ROLE_USER");
            roleRepository.save(role);
        }
        user.setRoles(Collections.singletonList(roleRepository.findByName("ROLE_USER").get()));
        user.setBlock(false);
        user.setEnabled(false);
        user = userRepository.save(user);

        String token = tokenService.createVerificationTokenForUser(user);

        emailService.sendSimpleMessage(user.getEmail(), "Account Verification", "Click the link to verify your account: " + serverUrl + "/registerConfirm?token=" + token);

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
        emailService.sendSimpleMessage(user.getEmail(), "[Resend] Account Verification", "Click the link to verify your account: " + serverUrl + "/registerConfirm?token=" + token);
    };

    @Override
    public UserRespDTO convertToDTO(User user) {
        return modelMapper.map(user, UserRespDTO.class);
    }

    @Override
    public User convertToEntity(UserReqDTO userReqDTO) throws ParseException {
        return modelMapper.map(userReqDTO, User.class);
    }
}
