package com.bookcorner.auth.config;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {
    /**
     * Secret key used to sign JWTs.
     */
    private String secret;

    /**
     * Access token lifetime.
     */
    private Duration accessTokenExpiration;

    /**
     * Refresh token lifetime.
     */
    private Duration refreshTokenExpiration ;
}
