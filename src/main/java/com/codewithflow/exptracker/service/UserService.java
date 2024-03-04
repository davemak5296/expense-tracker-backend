package com.codewithflow.exptracker.service;

import com.codewithflow.exptracker.dto.LoginReqDTO;
import com.codewithflow.exptracker.dto.UserReqDTO;
import com.codewithflow.exptracker.dto.UserRespDTO;
import com.codewithflow.exptracker.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.text.ParseException;

public interface UserService {

    UserRespDTO register(UserReqDTO userReqDTO) throws ParseException;

    UserRespDTO findUserById(Long id);

    User enabledUser(User user);

    void resendVerificationEmail(User user);

    void login(LoginReqDTO loginReqDTO, HttpServletResponse response);

    void refreshToken(HttpServletRequest request, HttpServletResponse response);

    void logout(HttpServletRequest request,HttpServletResponse response);

    UserRespDTO convertToDTO(User user);

    User convertToEntity(UserReqDTO userReqDTO) throws ParseException;
}
