package com.example.userservice.web.dto;

import com.example.userservice.common.model.User;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(description = "Ответ с данными пользователя")
public record UserResponse(
        @Schema(description = "Уникальный идентификатор пользователя", example = "1")
        Long id,
        @Schema(description = "Имя пользователя", example = "Алексей Алексеев")
        String name,
        @Schema(description = "Email пользователя", example = "Alexeev@example.com")
        String email,
        @Schema(description = "Возраст пользователя", example = "31")
        Integer age,
        @Schema(description = "Дата и время создания пользователя", example = "2026-10-26T10:15:30")
        LocalDateTime createdAt
) {
    public static UserResponse from(User user) {
        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getAge(),
                user.getCreatedAt()
        );
    }
}