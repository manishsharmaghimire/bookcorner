package com.bookcorner.auth.service;

import com.bookcorner.auth.dto.*;
import com.bookcorner.auth.entity.RefreshToken;
import com.bookcorner.auth.entity.User;
import com.bookcorner.auth.enums.OtpPurpose;
import com.bookcorner.auth.enums.Role;
import com.bookcorner.auth.enums.UserStatus;
import com.bookcorner.auth.exception.UserAlreadyExistsException;
import com.bookcorner.auth.exception.UserNotFoundException;
import com.bookcorner.auth.repository.UserRepository;
import com.bookcorner.auth.service.serviceimpl.OtpService;
import com.bookcorner.auth.service.serviceimpl.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final OtpService otpService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final RefreshTokenService refreshTokenService;

    public void register(RegisterRequest registerRequest) {

        otpService.verifyOtp(
                registerRequest.getPhoneNumber(),
                OtpPurpose.REGISTER,
                registerRequest.getOtp()
        );

        boolean exists = userRepository.existsByPhoneNumber(
                registerRequest.getPhoneNumber()
        );

        if (exists) {
            throw new UserAlreadyExistsException(
                    "User already exists."
            );
        }

        User user = new User();

        user.setPhoneNumber(
                registerRequest.getPhoneNumber()
        );

        user.setPasswordHash(
                passwordEncoder.encode(registerRequest.getPassword())
        );

        user.setRole(Role.User);

        user.setStatus(UserStatus.Active);

        userRepository.save(user);
    }

    public AuthResponse login(LoginRequest loginRequest) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getPhoneNumber(),
                        loginRequest.getPassword()
                )
        );

        User user = userRepository.findByPhoneNumber(
                loginRequest.getPhoneNumber()
        ).orElseThrow(
                () -> new UserNotFoundException(
                        "User not found."
                )
        );

        String accessToken = jwtService.generateToken(
                user.getPhoneNumber()
        );

        RefreshToken refreshToken =
                refreshTokenService.createRefreshToken(user);

        return new AuthResponse(
                accessToken,
                "Bearer",
                refreshToken.getToken()
        );
    }

    public AuthResponse refreshToken(RefreshTokenRequest request) {

        RefreshToken refreshToken =
                refreshTokenService.findByToken(
                        request.getRefreshToken()
                );

        refreshToken =
                refreshTokenService.verifyExpiration(
                        refreshToken
                );

        User user = refreshToken.getUser();

        String accessToken =
                jwtService.generateToken(
                        user.getPhoneNumber()
                );

        return new AuthResponse(
                accessToken,
                "Bearer",
                refreshToken.getToken()
        );
    }

    public void logout() {

        Authentication authentication=
                SecurityContextHolder.getContext().getAuthentication();

        String phoneNumber = authentication.getName();
        User user = userRepository.findByPhoneNumber(
                phoneNumber).orElseThrow(
                () -> new UserNotFoundException(
                        "User not found."
                )
        );
        refreshTokenService.deleteByUser(user);


    }
}