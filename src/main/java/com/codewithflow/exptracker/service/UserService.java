package com.codewithflow.exptracker.service;

import com.codewithflow.exptracker.dto.UserDTO;
import com.codewithflow.exptracker.entity.User;

import java.text.ParseException;
import java.util.Optional;

public interface UserService {

    User register(UserDTO userDTO) throws ParseException;

    UserDTO findUserById(Long id);

    UserDTO convertToDTO(User user);

    User convertToEntity(UserDTO userDTO) throws ParseException;
}
