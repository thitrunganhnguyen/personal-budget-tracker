package com.budgetapp.backend.controller;

import com.budgetapp.backend.dto.LoginRequestDto;
import com.budgetapp.backend.dto.LoginResponseDto;
import com.budgetapp.backend.dto.UserDto;
import com.budgetapp.backend.dto.UserResponseDto;
import com.budgetapp.backend.service.UserService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public LoginResponseDto login(@RequestBody @Valid LoginRequestDto loginRequestDto) {
        return userService.login(loginRequestDto.getUsername(), loginRequestDto.getPassword());
    }
}
