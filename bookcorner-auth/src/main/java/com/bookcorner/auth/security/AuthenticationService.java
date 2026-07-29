package com.bookcorner.auth.security;


import com.bookcorner.auth.entity.User;
import com.bookcorner.auth.exception.UserNotFoundException;
import com.bookcorner.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

        private final UserRepository userRepository;

        public User getAuthenticatedUser() {

            String phoneNumber = SecurityContextHolder.getContext()
                    .getAuthentication()
                    .getName();

            return userRepository.findByPhoneNumber(phoneNumber)
                    .orElseThrow(() ->
                            new UserNotFoundException("User not found.")
                    );
        }
    }

