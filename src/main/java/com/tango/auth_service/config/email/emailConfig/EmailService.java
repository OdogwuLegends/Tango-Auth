package com.tango.auth_service.config.email.emailConfig;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {
    private final JavaMailSender mailSender;

    @Value("${spring.mail.properties.mail.smtp.from}")
    private String from;

    @Value("${spring.application.name}")
    private String appName;


    public void sendEmail(String to, String subject, String context){
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "UTF-8");

        try {
            helper.setTo(to);
            helper.setFrom(new InternetAddress(from,appName));
            helper.setSubject(subject);
            helper.setText(context, true);

            mailSender.send(mimeMessage);

            log.info("Email sent successfully to: {}", to);
            System.err.println("Email sent successfully to: " + to);
        } catch (MessagingException | UnsupportedEncodingException e) {
            log.error("Failed to send email to: {} due to: {}", to, e.getMessage());
            System.err.println("Failed to send email to: " + to);
            throw new RuntimeException(e);
        }

    }
}
