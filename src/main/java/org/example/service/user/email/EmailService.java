package org.example.service.user.email;

public interface EmailService {
    void sendEmail(String to, String subject, String text);
}
