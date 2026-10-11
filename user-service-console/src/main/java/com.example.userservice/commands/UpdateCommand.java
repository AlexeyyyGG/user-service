package com.example.userservice.commands;

import com.example.userservice.exception.EmailAlreadyExistsException;
import com.example.userservice.exception.UserNotFoundException;
import com.example.userservice.model.User;
import com.example.userservice.service.UserService;
import com.example.userservice.util.Utils;

public class UpdateCommand implements Command {
    private final UserService userService;
    private static final String INSUFFICIENT_ARGS_UPDATE = "Usage: update <id> <name> <email> <age>";
    private static final String USER_UPDATED = "User updated";

    public UpdateCommand(UserService userService) {
        this.userService = userService;
    }

    @Override
    public String getName() {
        return "update";
    }

    @Override
    public Result execute(String[] args) {
        if (args.length < 4) {
            return Result.failure(INSUFFICIENT_ARGS_UPDATE);
        }
        try {
            Long id = Utils.parseId(args[0]);
            String name = args[1];
            String email = args[2];
            int age = Utils.parseAge(args[3]);
            User updated = userService.update(id, name, email, age);
            return Result.success(USER_UPDATED + ": " + updated);
        } catch (UserNotFoundException | EmailAlreadyExistsException | IllegalArgumentException e) {
            return Result.failure(e.getMessage());
        }
    }
}