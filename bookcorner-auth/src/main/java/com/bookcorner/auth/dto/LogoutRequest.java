package com.bookcorner.auth.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LogoutRequest {
    @NotBlank(message = "Phone number is required.")
    private String phoneNumber;
}
