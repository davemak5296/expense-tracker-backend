package com.codewithflow.exptracker.service;

import com.codewithflow.exptracker.dto.UserReqDTO;
import com.codewithflow.exptracker.dto.UserRespDTO;
import com.codewithflow.exptracker.entity.Role;
import com.codewithflow.exptracker.entity.User;
import com.codewithflow.exptracker.repository.RoleRepository;
import com.codewithflow.exptracker.util.exception.ResourceNotFoundException;
import com.codewithflow.exptracker.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.util.Collections;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ModelMapper modelMapper;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.modelMapper = modelMapper;
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

        return convertToDTO(userRepository.save(user));
    }

    @Override
    public UserRespDTO findUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return convertToDTO(user);
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
