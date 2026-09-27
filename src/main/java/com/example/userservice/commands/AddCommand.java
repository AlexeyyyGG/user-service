package com.example.userservice.commands;

import com.example.userservice.dao.UserDao;
import com.example.userservice.model.User;
import com.example.userservice.util.Utils;

public class AddCommand implements Command {
    private final UserDao userDao;
    private static final String USER_ADDED = "User added";
    private static final String NOT_ENOUGH_ARGUMENTS = "Usage: add <name> <email> <age>";
    private static final String EMAIL_ALREADY_EXISTS = "User with this email already exists";

    public AddCommand(UserDao userDao) {
        this.userDao = userDao;
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
            if (userDao.getByEmail(email) != null) {
                return Result.failure(EMAIL_ALREADY_EXISTS);
            }
            User user = new User(name, email, age);
            User saved = userDao.save(user);
            return Result.success(USER_ADDED + ": " + saved);
        } catch (IllegalArgumentException e) {
            return Result.failure(e.getMessage());
        }
    }
}