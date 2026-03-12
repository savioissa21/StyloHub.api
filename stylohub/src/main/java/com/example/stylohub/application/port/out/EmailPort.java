package com.example.stylohub.application.port.out;

public interface EmailPort {
    void sendPasswordReset(String toEmail, String resetLink);
    void sendWelcome(String toEmail, String username);
    void sendPaymentFailed(String toEmail);
}
