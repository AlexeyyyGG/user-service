package com.example.userservice.notification.mail;

import com.example.userservice.common.dto.UserEvent;

public interface MailService {
    void sendEmail(UserEvent event);
}