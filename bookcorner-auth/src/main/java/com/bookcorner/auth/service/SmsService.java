package com.bookcorner.auth.service;

public interface SmsService {

    void sendSms(String phoneNumber,String message);
}
