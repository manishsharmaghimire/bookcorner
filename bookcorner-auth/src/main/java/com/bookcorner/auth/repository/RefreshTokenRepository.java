package com.bookcorner.auth.repository;

import com.bookcorner.auth.entity.RefreshToken;
import com.bookcorner.auth.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByToken(String token);

    void deleteByUser(User user);

}
