package com.client.service_client.service;

import java.util.concurrent.CompletableFuture;

import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.client.service_client.model.config.MailConfigProperties;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {
    
    private final JavaMailSender mailSender;
    private final MailConfigProperties mailConfig;

    public EmailService(JavaMailSender mailSender, MailConfigProperties mailConfig) {
        this.mailSender = mailSender;
        this.mailConfig = mailConfig;
    }

    @Async
    public CompletableFuture<Void> sendEmail(String to, String subject, String body) throws SendEmailException {
        try {
            SimpleMailMessage message = new SimpleMailMessage();

            message.setTo(to);
            message.setFrom(mailConfig.username());
            message.setSubject(subject);
            message.setText(body);

            mailSender.send(message);
            return CompletableFuture.completedFuture(null);
        }
        catch (MailException e) {
            throw new SendEmailException("Failed to send plain text email", e);
        }
    }

    @Async
    public CompletableFuture<Void> sendHtmlEmail(String subject, String name, String email, String number, String comment) throws SendHtmlEmailException {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, "utf-8");

            StringBuilder htmlContent = new StringBuilder();
            htmlContent.append("<html>")
                       .append("<body>")
                       .append("<h2>Contact Information</h2>")
                       .append("<table style='width:100%; border-collapse: collapse;'>")
                       .append("<tr><td style='padding: 8px; border: 1px solid #ddd;'><strong>Name:</strong></td><td style='padding: 8px; border: 1px solid #ddd;'>").append(name).append("</td></tr>")
                       .append("<tr><td style='padding: 8px; border: 1px solid #ddd;'><strong>Email:</strong></td><td style='padding: 8px; border: 1px solid #ddd;'>").append(email).append("</td></tr>");
    
            if (number != null && !number.isEmpty()) {
                htmlContent.append("<tr><td style='padding: 8px; border: 1px solid #ddd;'><strong>Phone Number:</strong></td><td style='padding: 8px; border: 1px solid #ddd;'>").append(number).append("</td></tr>");
            }
    
            if (comment != null && !comment.isEmpty()) {
                htmlContent.append("<tr><td style='padding: 8px; border: 1px solid #ddd;'><strong>Comment:</strong></td><td style='padding: 8px; border: 1px solid #ddd;'>").append(comment).append("</td></tr>");
            }
    
            htmlContent.append("</table>")
                       .append("</html>");

            helper.setTo(mailConfig.username());
            helper.setFrom(mailConfig.username());
            helper.setSubject(subject);
            helper.setText(htmlContent.toString(), true);
    
            mailSender.send(message);
            return CompletableFuture.completedFuture(null);
        }
        catch (MessagingException e) {
            throw new SendHtmlEmailException("Failed to send HTML email", e);
        }
    }
}
