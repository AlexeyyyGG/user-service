package com.example.userservice.notification.controller;

import com.example.userservice.common.dto.UserEvent;
import com.example.userservice.notification.dto.NotifyRequest;
import com.example.userservice.notification.mail.MailService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(NotificationController.NOTIFICATIONS_URL)
public class NotificationController {
    public static final String NOTIFICATIONS_URL = "/api/notifications";
    private final MailService mailService;

    @Autowired
    public NotificationController(MailService mailService) {
        this.mailService = mailService;
    }

    @PostMapping
    public ResponseEntity<Void> sendNotification(@Valid @RequestBody NotifyRequest request) {
        mailService.sendEmail(new UserEvent(request.email(), request.type()));
        return ResponseEntity.ok().build();
    }
}