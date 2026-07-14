package com.bookcorner.auth.service.serviceimpl;

import com.bookcorner.auth.service.SmsService;
import org.springframework.stereotype.Service;

@Service
public class MockSmsServiceImpl implements SmsService {
    @Override
    public void sendSms(String phoneNumber, String message) {
        System.out.println("--------------------------------");
        System.out.println("SMS SENT");
        System.out.println("To      : " + phoneNumber);
        System.out.println("Message : " + message);
        System.out.println("--------------------------------");

    }
}
