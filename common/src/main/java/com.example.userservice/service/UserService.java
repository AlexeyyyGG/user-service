package com.example.userservice.service;

import com.example.userservice.model.User;
import java.util.List;

public interface UserService {
    User create(String name, String email, int age);

    User getById(Long id);

    List<User> getAll();

    User update(Long id, String name, String email, int age);

    void delete(Long id);
}