package com.example.userservice.gateway.controller;

import com.example.userservice.gateway.dto.FallbackResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FallbackController {
    private static final String USERS_FALLBACK_PATH = "/fallback/users";
    private static final String USER_SERVICE_UNAVAILABLE_MESSAGE = "Сервис пользователей временно недоступен";
    private static final String STATUS_SERVICE_UNAVAILABLE = "SERVICE_UNAVAILABLE";

    @RequestMapping(USERS_FALLBACK_PATH)
    public ResponseEntity<FallbackResponse> userFallback() {
        FallbackResponse response = new FallbackResponse(
                STATUS_SERVICE_UNAVAILABLE,
                USER_SERVICE_UNAVAILABLE_MESSAGE
        );
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(response);
    }
}