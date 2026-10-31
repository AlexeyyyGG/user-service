package com.example.userservice.gateway.dto;

public record FallbackResponse(
        String status,
        String message
) {
}