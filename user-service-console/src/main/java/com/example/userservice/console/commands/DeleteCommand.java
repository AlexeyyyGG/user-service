package com.example.userservice.console.commands;

import com.example.userservice.common.exception.UserNotFoundException;
import com.example.userservice.common.service.UserService;
import com.example.userservice.console.util.Utils;

public class DeleteCommand implements Command {
    private final UserService userService;
    private static final String NO_ID_SPECIFIED = "You must specify the user ID";
    private static final String USER_DELETED = "User deleted with id: %d";

    public DeleteCommand(UserService userService) {
        this.userService = userService;
    }

    @Override
    public String getName() {
        return "delete";
    }

    @Override
    public Result execute(String[] args) {
        if (args.length < 1) {
            return Result.failure(NO_ID_SPECIFIED);
        }
        try {
            Long id = Utils.parseId(args[0]);
            userService.delete(id);
            return Result.success(String.format(USER_DELETED, id));
        } catch (UserNotFoundException | IllegalArgumentException e) {
            return Result.failure(e.getMessage());
        }
    }
}