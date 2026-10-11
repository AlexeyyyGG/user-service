package com.example.userservice.commands;

public interface Command {
    Result execute(String[] args);

    String getName();
}