package com.client.service_client.service;

import java.util.concurrent.CompletableFuture;

import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    
    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Async
    public CompletableFuture<Void> sendEmail(String to, String subject, String body) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();

            message.setTo(to);
            message.setFrom("d4m14n257@gmail.com");
            message.setSubject(subject);
            message.setText(body);

            mailSender.send(message);
            return CompletableFuture.completedFuture(null);
        }
        catch (MailException e) {
            return CompletableFuture.failedFuture(e);
        }
    }
}
