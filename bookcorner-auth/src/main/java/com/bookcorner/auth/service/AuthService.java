package com.bookcorner.auth.service;


import com.bookcorner.auth.dto.RegisterRequest;
import com.bookcorner.auth.entity.User;
import com.bookcorner.auth.enums.OtpPurpose;
import com.bookcorner.auth.enums.Role;
import com.bookcorner.auth.enums.UserStatus;
import com.bookcorner.auth.exception.UserAlreadyExistsException;
import com.bookcorner.auth.repository.UserRepository;
import com.bookcorner.auth.service.serviceimpl.OtpService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
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


    public void register(RegisterRequest registerRequest) {

        otpService.verifyOtp(registerRequest.getPhoneNumber(),
                OtpPurpose.REGISTER,
                registerRequest.getOtp());


        boolean phn = userRepository.existsByPhoneNumber(registerRequest.getPhoneNumber());


        if (phn) {

            throw new UserAlreadyExistsException("User already exists.");
        }


        String pass = passwordEncoder.encode(registerRequest.getPassword());

        User user = new User();

        user.setPhoneNumber(
                registerRequest.getPhoneNumber()
        );

        user.setPasswordHash(
                pass
        );

        user.setRole(
                Role.User
        );

        user.setStatus(
                UserStatus.Active
        );

        userRepository.save(user);
    }

}





