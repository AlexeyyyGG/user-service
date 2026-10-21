package com.example.userservice.notification.dto;

import com.example.userservice.common.dto.UserEvent;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record NotifyRequest(
        @NotBlank(message = "Email не должен быть пустым")
        @Email(message = "Некорректный формат email")
        String email,
        @NotNull(message = "Тип события обязателен")
        UserEvent.EventType type
) {
}