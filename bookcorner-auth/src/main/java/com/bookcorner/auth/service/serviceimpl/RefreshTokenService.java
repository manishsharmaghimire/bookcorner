package com.bookcorner.auth.service.serviceimpl;


import com.bookcorner.auth.entity.RefreshToken;
import com.bookcorner.auth.entity.User;
import com.bookcorner.auth.exception.InvalidRefreshTokenException;
import com.bookcorner.auth.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;




        public RefreshToken createRefreshToken(User user) {

            RefreshToken refreshToken = new RefreshToken();
            refreshToken.setToken(UUID.randomUUID().toString());
            refreshToken.setUser(user);
            refreshToken.setExpiryDate(Instant.now().plus(Duration.ofDays(15)));
            return refreshTokenRepository.save(refreshToken);




        }

        public RefreshToken verifyExpiration(RefreshToken token) {
            if (token.getExpiryDate().isBefore(Instant.now())) {
                refreshTokenRepository.delete(token);
                throw new InvalidRefreshTokenException(
                        "Refresh token has expired."
                );            }
                   return  token;
        }


    public RefreshToken findByToken(String token) {
        return refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new InvalidRefreshTokenException("Refresh token not found."));
    }



    public void deleteByUser(User user) {
        refreshTokenRepository.deleteByUser(user);
    }






}
