package com.codewithflow.exptracker.service;

import com.codewithflow.exptracker.dto.UserReqDTO;
import com.codewithflow.exptracker.dto.UserRespDTO;
import com.codewithflow.exptracker.entity.User;

import java.text.ParseException;

public interface UserService {

    UserRespDTO register(UserReqDTO userReqDTO) throws ParseException;

    UserRespDTO findUserById(Long id);

    User enabledUser(User user);

    void resendVerificationEmail(User user);

    UserRespDTO convertToDTO(User user);

    User convertToEntity(UserReqDTO userReqDTO) throws ParseException;
}
