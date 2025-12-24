package com.alertix.snooker.service;

public interface EmailService {
    void sendInfoEmail(String to, String subject, String content);

    void sendSupportEmail(String to, String subject, String content);

    void sendPromoEmail(String to, String subject, String content);
}
