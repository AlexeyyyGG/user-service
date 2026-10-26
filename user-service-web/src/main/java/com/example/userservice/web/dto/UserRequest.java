package com.example.userservice.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Запрос на создание или обновление пользователя")
public record UserRequest(
        @Schema(description = "Имя пользователя", example = "Алексей Алексеев")
        @NotBlank(message = "Имя обязательно")
        String name,
        @Schema(description = "Email пользователя", example = "Alexeev@example.com")
        @NotBlank(message = "Email обязателен")
        @Email(message = "Некорректный формат email")
        String email,
        @Schema(description = "Возраст пользователя (от 18 до 65 лет)", example = "31")
        @NotNull(message = "Возраст обязателен")
        @Min(value = 18, message = "Возраст должен быть не менее 18 лет")
        @Max(value = 65, message = "Возраст должен быть не более 65 лет")
        Integer age
) {
}