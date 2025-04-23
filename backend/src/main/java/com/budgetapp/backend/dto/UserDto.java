package com.budgetapp.backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserDto {

    @NotBlank
    private String username;

    @Email
    private String email;

    @NotBlank
    private String password;

    private String firstName;

    private String lastName;
}
