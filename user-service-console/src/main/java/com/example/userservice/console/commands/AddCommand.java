package com.example.userservice.console.commands;

import com.example.userservice.common.exception.EmailAlreadyExistsException;
import com.example.userservice.common.model.User;
import com.example.userservice.common.service.UserService;
import com.example.userservice.console.util.Utils;

public class AddCommand implements Command {
    private final UserService userService;
    private static final String USER_ADDED = "User added";
    private static final String NOT_ENOUGH_ARGUMENTS = "Usage: add <name> <email> <age>";

    public AddCommand(UserService userService) {
        this.userService = userService;
    }

    @Override
    public String getName() {
        return "add";
    }

    @Override
    public Result execute(String[] args) {
        if (args.length < 3) {
            return Result.failure(NOT_ENOUGH_ARGUMENTS);
        }
        try {
            String name = args[0];
            String email = args[1];
            int age = Utils.parseAge(args[2]);
            User user = userService.create(name, email, age);
            return Result.success(USER_ADDED + ": " + user);
        } catch (EmailAlreadyExistsException | IllegalArgumentException e) {
            return Result.failure(e.getMessage());
        }
    }
}