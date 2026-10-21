package com.example.userservice.console.commands;

public interface Command {
    Result execute(String[] args);

    String getName();
}