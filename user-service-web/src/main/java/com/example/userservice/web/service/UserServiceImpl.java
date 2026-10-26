package com.example.userservice.web.service;

import com.example.userservice.common.dto.UserEvent;
import com.example.userservice.common.dto.UserEvent.EventType;
import com.example.userservice.common.exception.EmailAlreadyExistsException;
import com.example.userservice.common.exception.UserNotFoundException;
import com.example.userservice.common.model.User;
import com.example.userservice.common.service.UserService;
import com.example.userservice.web.kafka.UserEventProducer;
import com.example.userservice.web.repository.UserRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserEventProducer userEventProducer;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserEventProducer userEventProducer) {
        this.userRepository = userRepository;
        this.userEventProducer = userEventProducer;
    }

    @Override
    @Transactional
    public User create(String name, String email, int age) {
        if (userRepository.existsByEmail(email)) {
            throw new EmailAlreadyExistsException(email);
        }
        User savedUser = userRepository.save(new User(name, email, age));
        userEventProducer.sendEvent(new UserEvent(savedUser.getEmail(), EventType.CREATE));
        return savedUser;
    }

    @Override
    @Transactional(readOnly = true)
    public User getById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Override
    @Transactional
    public User update(Long id, String name, String email, int age) {
        User existing = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        userRepository.findByEmail(email).ifPresent(byEmail -> {
            if (!byEmail.getId().equals(id)) {
                throw new EmailAlreadyExistsException(email);
            }
        });
        existing.setName(name);
        existing.setEmail(email);
        existing.setAge(age);
        return userRepository.save(existing);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        User existing = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        userRepository.deleteById(id);
        userEventProducer.sendEvent(new UserEvent(existing.getEmail(), EventType.DELETE));
    }
}