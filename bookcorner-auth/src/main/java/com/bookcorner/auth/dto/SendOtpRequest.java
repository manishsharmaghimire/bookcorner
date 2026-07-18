package com.bookcorner.auth.dto;

import com.bookcorner.auth.enums.OtpPurpose;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SendOtpRequest {

    @NotBlank(message = "Phone number is required.")
    private String phoneNumber;

    @NotNull(message = "OTP purpose is required.")
    private OtpPurpose purpose;

}
