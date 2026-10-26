package com.example.userservice.web.controller;

import static com.example.userservice.web.controller.UserController.USERS_URL;

import com.example.userservice.web.dto.UserRequest;
import com.example.userservice.web.dto.UserResponse;
import com.example.userservice.common.model.User;
import com.example.userservice.common.service.UserService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(USERS_URL)
public class UserController {
    public static final String USERS_URL = "/api/users";
    private static final String ID_PATH = "/{id}";
    private static final String ID_PARAM = "id";
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> getAll() {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getAll().stream()
                .map(UserResponse::from)
                .toList());
    }

    @GetMapping(ID_PATH)
    public ResponseEntity<UserResponse> get(@PathVariable(ID_PARAM) Long id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(UserResponse.from(userService.getById(id)));
    }

    @PostMapping
    public ResponseEntity<UserResponse> add(@Valid @RequestBody UserRequest request) {
        User user = userService.create(request.name(), request.email(), request.age());
        return ResponseEntity.status(HttpStatus.CREATED).body(UserResponse.from(user));
    }

    @PutMapping(ID_PATH)
    public ResponseEntity<UserResponse> update(
            @PathVariable(ID_PARAM) Long id,
            @Valid @RequestBody UserRequest request
    ) {
        User updated = userService.update(id, request.name(), request.email(), request.age());
        return ResponseEntity.status(HttpStatus.OK).body(UserResponse.from(updated));
    }

    @DeleteMapping(ID_PATH)
    public ResponseEntity<Void> delete(@PathVariable(ID_PARAM) Long id) {
        userService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}