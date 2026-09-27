package com.example.userservice.commands;

import com.example.userservice.dao.UserDao;
import com.example.userservice.model.User;
import com.example.userservice.util.Utils;

public class UpdateCommand implements Command {
    private final UserDao userDao;
    private static final String INSUFFICIENT_ARGS_UPDATE = "Usage: update <id> <name> <email> <age>";
    private static final String USER_NOT_EXIST = "User with this ID does not exist";
    private static final String USER_UPDATED = "User updated";
    private static final String EMAIL_ALREADY_EXISTS = "User with this email already exists";

    public UpdateCommand(UserDao userDao) {
        this.userDao = userDao;
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
            User user = userDao.getById(id);
            if (user == null) {
                return Result.failure(USER_NOT_EXIST);
            }
            String name = args[1];
            String email = args[2];
            int age = Utils.parseAge(args[3]);
            User existing = userDao.getByEmail(email);
            if (existing != null && !existing.getId().equals(user.getId())) {
                return Result.failure(EMAIL_ALREADY_EXISTS);
            }
            user.setName(name);
            user.setEmail(email);
            user.setAge(age);
            User updated = userDao.update(user);
            return Result.success(USER_UPDATED + ": " + updated);
        } catch (IllegalArgumentException e) {
            return Result.failure(e.getMessage());
        }
    }
}