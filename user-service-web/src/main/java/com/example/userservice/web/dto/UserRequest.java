package com.example.userservice.web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UserRequest(
        @NotBlank(message = "Имя обязательно")
        String name,
        @NotBlank(message = "Email обязателен")
        @Email(message = "Некорректный формат email")
        String email,
        @NotNull(message = "Возраст обязателен")
        @Min(value = 18, message = "Возраст должен быть не менее 18 лет")
        @Max(value = 65, message = "Возраст должен быть не более 65 лет")
        Integer age
) {
}