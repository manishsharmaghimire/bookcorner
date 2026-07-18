package com.bookcorner.auth.service.serviceimpl;

import com.bookcorner.auth.config.OtpProperties;
import com.bookcorner.auth.enums.OtpPurpose;
import com.bookcorner.auth.exception.*;
import com.bookcorner.auth.service.SmsService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.HexFormat;

@Service
@RequiredArgsConstructor
public class OtpService {

    private final RedisTemplate<String, String> redisTemplate;
    private final OtpProperties otpProperties;
    private final SmsService smsService;

    private final SecureRandom secureRandom = new SecureRandom();

    private String generateOtp() {

        int otp = 100000 + secureRandom.nextInt(900000);
        return String.valueOf(otp);

    }

    public void sendOtp(String phoneNumber, OtpPurpose purpose) {

        String otpKey = otpKey(phoneNumber, purpose);
        String coolDownKeys = coolDownKey(phoneNumber, purpose);
        String attemptsKey = attemptsKey(phoneNumber, purpose);

        if (Boolean.TRUE.equals(redisTemplate.hasKey(coolDownKeys))) {
            throw new OtpRateLimitException(
                    "Please wait before requesting another OTP."
            );
        }


        String otp = generateOtp();
        String hashedOtp = hmacHash(otp);
        redisTemplate.opsForValue().set(
                otpKey,
                hashedOtp,
                otpProperties.getTtl()
        );

        redisTemplate.delete(attemptsKey);

        redisTemplate.opsForValue().set(coolDownKeys, "1", otpProperties.getResendCoolDown());
        try {

            smsService.send(
                    phoneNumber,
                    "Your BookCorner OTP is: " + otp
            );

        } catch (Exception e) {

            redisTemplate.delete(otpKey);

            throw new OtpDeliveryException(
                    "Failed to send OTP.",
                    e
            );
        }
    }


    public boolean verifyOtp(
            String phoneNumber,
            OtpPurpose purpose,
            String otp
    ) {


        String otps = otpKey(phoneNumber, purpose);
        String attemptsKey = attemptsKey(phoneNumber, purpose);
        String cooldownKey = coolDownKey(phoneNumber, purpose);

        String storedHash = redisTemplate.opsForValue().get(otps);


        if (storedHash == null) {
            throw new InvalidOtpException(
                    "OTP has expired or does not exist."
            );
        }

        String attempts =
                redisTemplate.opsForValue().get(attemptsKey);


        int failedAttempts =
                attempts == null ? 0 : Integer.parseInt(attempts);
        if (failedAttempts >= otpProperties.getMaxAttempts()) {
            throw new OtpAttemptsExceededException(
                    "Maximum OTP verification attempts exceeded."
            );
        }
        String enteredHash = hmacHash(otp);
        if(!enteredHash.equals(storedHash)){

            failedAttempts++;

            redisTemplate.opsForValue().set(
                    attemptsKey,
                    String.valueOf(failedAttempts),
            otpProperties.getTtl());

            throw new InvalidOtpException(
                    "Invalid OTP."
            );


        }

        redisTemplate.delete(otps);
        redisTemplate.delete(attemptsKey);
        redisTemplate.delete(cooldownKey);

        return true;


}

    private String otpKey(
            String phoneNumber,
            OtpPurpose purpose
    ) {

        return "otp:%s:%s".formatted(
                purpose,
                phoneNumber
        );
    }
    private String coolDownKey(
            String phoneNumber,
            OtpPurpose purpose
    ) {

        return "otp:cooldown:%s:%s".formatted(
                purpose,
                phoneNumber
        );
    }

    private String attemptsKey(
            String phoneNumber,
            OtpPurpose purpose
    ) {

        return "otp:cooldown:%s:%s".formatted(
                purpose,
                phoneNumber
        );
    }

    private String hmacHash(String value) {

        try {

            Mac mac = Mac.getInstance("HmacSHA256");

            SecretKeySpec secretKey =
                    new SecretKeySpec(
                            otpProperties.getHmacSecret()
                                    .getBytes(StandardCharsets.UTF_8),
                            "HmacSHA256"
                    );

            mac.init(secretKey);

            byte[] hashBytes = mac.doFinal(
                    value.getBytes(StandardCharsets.UTF_8)
            );

            return HexFormat.of().formatHex(hashBytes);

        } catch (Exception e) {

            throw new IllegalStateException(
                    "Failed to generate OTP hash",
                    e
            );
        }
    }
}
