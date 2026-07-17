package com.bookcorner.auth.controller;

import com.bookcorner.auth.dto.*;
import com.bookcorner.auth.service.AuthService;
import com.bookcorner.auth.service.serviceimpl.OtpService;
import com.bookcorner.auth.service.serviceimpl.RefreshTokenService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private   final OtpService otpService;
    private final RefreshTokenService refreshTokenService;

    @PostMapping("/register")
    public ResponseEntity<String> register(

            @Valid
            @RequestBody
            RegisterRequest request

    ) {

        authService.register(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body("User registered successfully.");

    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(

            @Valid
            @RequestBody
            LoginRequest request

    ) {

        AuthResponse response =
                authService.login(request);

        return ResponseEntity.ok(response);

    }


    @PostMapping("/send-otp")
    public ResponseEntity<String> sendOtp(

            @Valid
            @RequestBody
            SendOtpRequest request

    ) {

        otpService.sendOtp(
                request.getPhoneNumber(),
                request.getPurpose()
        );

        return ResponseEntity.ok(
                "OTP sent successfully."
        );

    }
    @PostMapping("/logout")
    public ResponseEntity<String> logout() {

        authService.logout();

        return ResponseEntity.ok(
                "Logged out successfully."
        );
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(
            @Valid @RequestBody LogoutRequest request
    ) {

        authService.logout();

        return ResponseEntity.ok("Logged out successfully.");
    }
}
