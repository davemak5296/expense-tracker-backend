package com.codewithflow.exptracker.controller;

import com.codewithflow.exptracker.dto.UserReqDTO;
import com.codewithflow.exptracker.dto.UserRespDTO;
import com.codewithflow.exptracker.entity.User;
import com.codewithflow.exptracker.entity.VerificationToken;
import com.codewithflow.exptracker.repository.RoleRepository;
import com.codewithflow.exptracker.service.UserService;
import com.codewithflow.exptracker.service.VerificationTokenService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Map;
import java.util.Optional;

@RestController
public class UserController {
    private final UserService userService;
    private final VerificationTokenService tokenService;
    private final RoleRepository roleRepository;

    @Value("${exptracker.client.url}")
    private String clientUrl;

    @Value("${spring.profiles.active}")
    private String activeProfile;

    public UserController(UserService userService, VerificationTokenService tokenService, RoleRepository roleRepository) {
        this.userService = userService;
        this.tokenService = tokenService;
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

    @GetMapping("/registerConfirm")
    @Transactional
    public RedirectView registerConfirm(@RequestParam("token") String token, HttpServletResponse response) {
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

        Cookie cookie = new Cookie("firstLogin", "true");
        cookie.setMaxAge(60 * 60);
        cookie.setPath("/");
        cookie.setSecure(activeProfile.equals("production"));
        response.addCookie(cookie);

        return new RedirectView(clientUrl + "/profile");
    };

    @PostMapping("/test")
    public void test(@RequestBody Map<String, Long> greeting) {
        final Calendar cal = Calendar.getInstance();
        System.out.println(cal.getTime());
    }

//    @PostMapping("/test")
//    public GreetingDTO test(@RequestBody Map<String, Long> greeting) {
//       return new GreetingDTO(greeting.get("testId"), "ok");
//    }
}
