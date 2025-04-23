package com.budgetapp.backend.controller;

import com.budgetapp.backend.dto.UserDto;
import com.budgetapp.backend.dto.UserResponseDto;
import com.budgetapp.backend.model.User;
import com.budgetapp.backend.service.UserService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/create")
    public UserResponseDto createUser(@RequestBody @Valid UserDto userDto) {
        return userService.createUser(userDto);
    }
}
