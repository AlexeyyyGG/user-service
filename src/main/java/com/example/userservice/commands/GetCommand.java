package com.example.userservice.commands;

import com.example.userservice.dao.UserDao;
import com.example.userservice.model.User;
import com.example.userservice.util.Utils;

public class GetCommand implements Command {
    private final UserDao userDao;
    private static final String NO_ID_SPECIFIED = "You must specify the user ID";
    private static final String NO_USER_WITH_ID = "There are no users with this ID";

    public GetCommand(UserDao userDao) {
        this.userDao = userDao;
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
            User user = userDao.getById(id);
            if (user == null) {
                return Result.failure(NO_USER_WITH_ID);
            }
            return Result.success(user.toString());
        } catch (IllegalArgumentException e) {
            return Result.failure(e.getMessage());
        }
    }
}