package com.bookcorner.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class AuthResponse {


    @NotBlank(message = "Phone number is required.")
    @Pattern(
            regexp = "^\\+?[1-9]\\d{7,14}$",
            message = "Invalid phone number."
    )
    private String phoneNumber;

    @NotBlank(message = "Password is required.")
    private String password;

}
