package com.example.userservice.commands;

import com.example.userservice.dao.UserDao;
import com.example.userservice.model.User;
import java.util.List;

public class ListCommand implements Command {
    private final UserDao userDao;
    private static final String COLLECTION_EMPTY = "Collection is empty";

    public ListCommand(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public String getName() {
        return "list";
    }

    @Override
    public Result execute(String[] args) {
        List<User> users = userDao.getAll();
        if (users.isEmpty()) {
            return Result.failure(COLLECTION_EMPTY);
        }
        StringBuilder sb = new StringBuilder();
        for (User user : users) {
            sb.append(user.toString()).append("\n");
        }
        return Result.success(sb.toString());
    }
}