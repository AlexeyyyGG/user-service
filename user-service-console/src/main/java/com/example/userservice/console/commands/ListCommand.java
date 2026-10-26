package com.example.userservice.console.commands;

import com.example.userservice.common.model.User;
import com.example.userservice.common.service.UserService;
import java.util.List;

public class ListCommand implements Command {
    private final UserService userService;
    private static final String COLLECTION_EMPTY = "Collection is empty";

    public ListCommand(UserService userService) {
        this.userService = userService;
    }

    @Override
    public String getName() {
        return "list";
    }

    @Override
    public Result execute(String[] args) {
        List<User> users = userService.getAll();
        if (users.isEmpty()) {
            return Result.failure(COLLECTION_EMPTY);
        }
        StringBuilder sb = new StringBuilder();
        for (User user : users) {
            sb.append(user).append("\n");
        }
        return Result.success(sb.toString());
    }
}