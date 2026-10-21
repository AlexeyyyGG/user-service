package com.example.userservice.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = {"com.example.userservice.common.model"})
public class UserServiceWebApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserServiceWebApplication.class, args);
    }
}