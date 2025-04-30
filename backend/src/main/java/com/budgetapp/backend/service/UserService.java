package com.budgetapp.backend.service;

import com.budgetapp.backend.dto.LoginResponseDto;
import com.budgetapp.backend.dto.UserDto;
import com.budgetapp.backend.dto.UserResponseDto;
import com.budgetapp.backend.exception.InvalidCredentialsException;
import com.budgetapp.backend.exception.UsernameAlreadyExistsException;
import com.budgetapp.backend.model.User;
import com.budgetapp.backend.repository.UserRepository;
import com.budgetapp.backend.security.JwtService;
import jakarta.validation.constraints.NotBlank;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    private User toEntity(UserDto dto) {
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setEmail(dto.getEmail());
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        return user;
    }

    private UserResponseDto toResponseDto(User user) {
        UserResponseDto dto = new UserResponseDto();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setCreatedAt(user.getCreatedAt());
        return dto;
    }

    public UserResponseDto createUser(UserDto userDto) {
        if (userRepository.existsByUsername(userDto.getUsername())) {
            throw new UsernameAlreadyExistsException("Username already exists");
        }
        User user = toEntity(userDto);
        User savedUser = userRepository.save(user);
        return toResponseDto(savedUser);
    };


    public LoginResponseDto login(String username, String rawPassword) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(InvalidCredentialsException::new);

        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            throw new InvalidCredentialsException();
        }

        String token = jwtService.generateToken(user);
        UserResponseDto responseDto = toResponseDto(user);

        return new LoginResponseDto(token, responseDto);
    }

}
