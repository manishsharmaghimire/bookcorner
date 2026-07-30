package com.bookcorner.payment.config;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "esewa")
public class EsewaConfiguration {

    private String merchantId;
    private String secretKey;
    private String paymentUrl;

    private String verificationUrl;

    private String successUrl;

    private String failureUrl;


}
