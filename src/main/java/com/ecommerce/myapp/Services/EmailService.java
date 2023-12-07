package com.ecommerce.myapp.Services;

import jakarta.mail.MessagingException;

import java.util.List;

public interface EmailService {
    void sendBulkEmail(List<String> recipients, String subject, String content) throws MessagingException;

    void sendEmail(String to, String subject, String content) throws MessagingException;
}
