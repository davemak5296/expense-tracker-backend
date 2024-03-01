package com.codewithflow.exptracker.controller;

import com.codewithflow.exptracker.dto.GreetingDTO;
import com.codewithflow.exptracker.dto.UserReqDTO;
import com.codewithflow.exptracker.dto.UserRespDTO;
import com.codewithflow.exptracker.entity.User;
import com.codewithflow.exptracker.repository.RoleRepository;
import com.codewithflow.exptracker.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.Map;

@RestController
public class UserController {
    private final UserService userService;
    private final RoleRepository roleRepository;

    public UserController(UserService userService, RoleRepository roleRepository) {
        this.userService = userService;
        this.roleRepository = roleRepository;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public UserRespDTO register(@Valid @RequestBody UserReqDTO userReqDTO) throws ParseException {
        return userService.register(userReqDTO);
    }

    @GetMapping("/user/{id}")
    @ResponseBody
    public UserRespDTO getUserById(@PathVariable Long id) {
        System.out.println(roleRepository.findByName("ROLE_USER"));
        return userService.findUserById(id);
    }

    @PostMapping("/test")
//    @ResponseBody
    public GreetingDTO test(@RequestBody Map<String, Long> greeting) {
       return new GreetingDTO(greeting.get("testId"), "ok");
    }
}
