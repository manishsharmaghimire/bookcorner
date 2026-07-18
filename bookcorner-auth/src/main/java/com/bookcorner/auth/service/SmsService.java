package com.bookcorner.auth.service;

public interface SmsService {

    void send(String phoneNumber, String message);
}
