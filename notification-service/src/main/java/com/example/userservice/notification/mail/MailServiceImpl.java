package com.example.userservice.notification.mail;

import com.example.userservice.common.dto.UserEvent;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailServiceImpl implements MailService {
    private final JavaMailSender mailSender;

    public MailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void sendEmail(UserEvent event) {
        if (event == null || event.email() == null || event.type() == null) {
            throw new IllegalArgumentException("Некорректное событие: " + event);
        }
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("no-reply@example.com");
        message.setTo(event.email());
        switch (event.type()) {
            case CREATE -> {
                message.setSubject("Аккаунт создан");
                message.setText("Здравствуйте! Ваш аккаунт на сайте был успешно создан.");
            }
            case DELETE -> {
                message.setSubject("Аккаунт удалён");
                message.setText("Здравствуйте! Ваш аккаунт был удалён.");
            }
        }
        mailSender.send(message);
    }
}