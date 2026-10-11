package com.example.userservice.commands;

import com.example.userservice.exception.UserNotFoundException;
import com.example.userservice.model.User;
import com.example.userservice.service.UserService;
import com.example.userservice.util.Utils;

public class GetCommand implements Command {
    private final UserService userService;
    private static final String NO_ID_SPECIFIED = "You must specify the user ID";

    public GetCommand(UserService userService) {
        this.userService = userService;
    }

    @Override
    public String getName() {
        return "get";
    }

    @Override
    public Result execute(String[] args) {
        if (args.length < 1) {
            return Result.failure(NO_ID_SPECIFIED);
        }
        try {
            Long id = Utils.parseId(args[0]);
            User user = userService.getById(id);
            return Result.success(user.toString());
        } catch (UserNotFoundException | IllegalArgumentException e) {
            return Result.failure(e.getMessage());
        }
    }
}