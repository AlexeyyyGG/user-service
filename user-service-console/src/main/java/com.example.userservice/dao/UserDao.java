package com.example.userservice.dao;

import com.example.userservice.model.User;
import java.util.List;

public interface UserDao {
    User save(User user);

    User getById(Long id);

    User getByEmail(String email);

    List<User> getAll();

    User update(User user);

    void deleteById(Long id);
}