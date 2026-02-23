package org.example.ecom.controller;

import jakarta.validation.Valid;
import org.example.ecom.dtos.request.LoginRequest;
import org.example.ecom.dtos.request.UserRegisterRequest;
import org.example.ecom.dtos.response.LoginResponse;
import org.example.ecom.dtos.response.UserResponse;
import org.example.ecom.service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/auth")
public class AuthController {
    private final UserService userService;

    public AuthController(UserService userService){
        this.userService = userService;
    }

    @PostMapping("/register")
    public UserResponse register(@Valid @RequestBody UserRegisterRequest request) {
        return userService.register(request);
    }//validates the objects

    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest loginRequest){
        return userService.login(loginRequest);
    }


}
