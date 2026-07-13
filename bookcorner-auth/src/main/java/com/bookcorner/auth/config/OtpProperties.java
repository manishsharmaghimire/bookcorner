package com.bookcorner.auth.config;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Getter
@Setter
@ConfigurationProperties(prefix = "otp")
public class OtpProperties {

    private Duration ttl;
    private Duration resendCoolDown;
    private int maxAttempts;
    private String hmacSecret;


}
