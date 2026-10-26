package com.example.userservice.console.service;

import com.example.userservice.common.service.UserService;
import com.example.userservice.console.dao.UserDao;
import com.example.userservice.common.exception.EmailAlreadyExistsException;
import com.example.userservice.common.exception.UserNotFoundException;
import com.example.userservice.common.model.User;
import java.util.List;

public class UserServiceImpl implements UserService {
    private final UserDao userDao;

    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public User create(String name, String email, int age) {
        if (userDao.getByEmail(email) != null) {
            throw new EmailAlreadyExistsException(email);
        }
        return userDao.save(new User(name, email, age));
    }

    @Override
    public User getById(Long id) {
        User user = userDao.getById(id);
        if (user == null) {
            throw new UserNotFoundException(id);
        }
        return user;
    }

    @Override
    public List<User> getAll() {
        return userDao.getAll();
    }

    @Override
    public User update(Long id, String name, String email, int age) {
        User existing = userDao.getById(id);
        if (existing == null) {
            throw new UserNotFoundException(id);
        }
        User byEmail = userDao.getByEmail(email);
        if (byEmail != null && !byEmail.getId().equals(id)) {
            throw new EmailAlreadyExistsException(email);
        }
        existing.setName(name);
        existing.setEmail(email);
        existing.setAge(age);
        return userDao.update(existing);
    }

    @Override
    public void delete(Long id) {
        if (userDao.getById(id) == null) {
            throw new UserNotFoundException(id);
        }
        userDao.deleteById(id);
    }
}