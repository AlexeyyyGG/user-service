package com.example.userservice.notification.kafka;

import com.example.userservice.common.dto.UserEvent;
import com.example.userservice.notification.mail.MailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class UserEventListener {
    private static final Logger log = LoggerFactory.getLogger(UserEventListener.class);
    private final MailService mailService;

    @Autowired
    public UserEventListener(MailService mailService) {
        this.mailService = mailService;
    }

    @KafkaListener(topics = "user-events")
    public void handleUserEvent(UserEvent event) {
        log.info("Получено событие из Kafka {}", event);
        mailService.sendEmail(event);
    }
}