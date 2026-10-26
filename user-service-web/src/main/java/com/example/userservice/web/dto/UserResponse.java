package com.example.userservice.web.dto;

import com.example.userservice.common.model.User;
import java.time.LocalDateTime;

public record UserResponse(
        Long id,
        String name,
        String email,
        Integer age,
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