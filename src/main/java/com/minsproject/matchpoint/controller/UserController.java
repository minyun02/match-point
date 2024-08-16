package com.minsproject.matchpoint.controller;

import com.minsproject.matchpoint.dto.request.JoinRequest;
import com.minsproject.matchpoint.dto.request.LoginRequest;
import com.minsproject.matchpoint.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
@RestController
public class UserController {

    private final UserService userService;

    @PostMapping("/login")
    public String login(@RequestBody LoginRequest req) {
        return userService.login(req);
    }

    @PostMapping("/join")
    public Long join(@RequestBody JoinRequest req) {
        return userService.join(req);
    }
}
