package com.codewithflow.exptracker.controller;

import com.codewithflow.exptracker.dto.GreetingDTO;
import com.codewithflow.exptracker.dto.UserDTO;
import com.codewithflow.exptracker.entity.User;
import com.codewithflow.exptracker.exception.ResourceNotFoundException;
import com.codewithflow.exptracker.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.Map;
import java.util.Optional;

@RestController
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public User register(@Valid @RequestBody UserDTO userDTO) throws ParseException {
        return userService.register(userDTO);
    }

    @GetMapping("/user/{id}")
    @ResponseBody
    public Optional<UserDTO> getUserById(@PathVariable Long id) {
        return userService.findUserById(id);
    }

    @PostMapping("/test")
//    @ResponseBody
    public GreetingDTO test(@RequestBody Map<String, Long> greeting) {
       return new GreetingDTO(greeting.get("testId"), "ok");
    }
}
