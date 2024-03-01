package com.codewithflow.exptracker.service;

import com.codewithflow.exptracker.dto.UserReqDTO;
import com.codewithflow.exptracker.dto.UserRespDTO;
import com.codewithflow.exptracker.entity.Role;
import com.codewithflow.exptracker.entity.User;
import com.codewithflow.exptracker.entity.VerificationToken;
import com.codewithflow.exptracker.repository.RoleRepository;
import com.codewithflow.exptracker.repository.VerificationTokenRepository;
import com.codewithflow.exptracker.util.exception.ResourceNotFoundException;
import com.codewithflow.exptracker.repository.UserRepository;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.util.Collections;
import java.util.UUID;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final VerificationTokenRepository tokenRepository;
    private final ModelMapper modelMapper;
    private final EmailService emailService;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, VerificationTokenRepository tokenRepository,ModelMapper modelMapper, EmailService emailService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.tokenRepository = tokenRepository;
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
        user = userRepository.save(user);

        String token = createVerificationTokenForUser(user);

        emailService.sendSimpleMessage(user.getEmail(), "Account Verification", "Click the link to verify your account: http://localhost:8080/verify/" + token);

        return convertToDTO(user);
    }

    @Override
    public UserRespDTO findUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return convertToDTO(user);
    }

    @Override
    public String createVerificationTokenForUser(User user){
        String token = UUID.randomUUID().toString();
        VerificationToken vToken = new VerificationToken(token, user);
        tokenRepository.save(vToken);
        return vToken.getToken();
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
