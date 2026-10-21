package com.example.userservice.common.dto;

public record UserEvent(
        String email,
        EventType type
) {
    public enum EventType {
        CREATE, DELETE
    }
}