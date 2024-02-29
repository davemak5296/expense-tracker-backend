package com.codewithflow.exptracker.service;

import com.codewithflow.exptracker.dto.UserDTO;
import com.codewithflow.exptracker.entity.User;
import com.codewithflow.exptracker.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Optional;

public interface UserService {

    User register(UserDTO userDTO) throws ParseException;

    Optional<UserDTO> findUserById(Long id) throws ResourceNotFoundException;

    UserDTO convertToDTO(User user);

    User convertToEntity(UserDTO userDTO) throws ParseException;
}
