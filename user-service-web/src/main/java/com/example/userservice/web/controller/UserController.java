package com.example.userservice.web.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import com.example.userservice.web.controller.api.UserApi;
import com.example.userservice.web.dto.UserRequest;
import com.example.userservice.web.dto.UserResponse;
import com.example.userservice.common.model.User;
import com.example.userservice.common.service.UserService;
import java.net.URI;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController implements UserApi {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Override
    public ResponseEntity<CollectionModel<EntityModel<UserResponse>>> getAll() {
        List<EntityModel<UserResponse>> users = userService.getAll().stream()
                .map(this::toModel)
                .toList();
        CollectionModel<EntityModel<UserResponse>> collectionModel = CollectionModel.of(users,
                linkTo(methodOn(UserApi.class).getAll()).withSelfRel());
        return ResponseEntity.ok(collectionModel);
    }

    @Override
    public ResponseEntity<EntityModel<UserResponse>> get(Long id) {
        return ResponseEntity.ok(toModel(userService.getById(id)));
    }

    @Override
    public ResponseEntity<EntityModel<UserResponse>> add(UserRequest request) {
        User user = userService.create(request.name(), request.email(), request.age());
        EntityModel<UserResponse> model = toModel(user);
        URI location = model.getRequiredLink(IanaLinkRelations.SELF).toUri();
        return ResponseEntity.created(location).body(model);
    }

    @Override
    public ResponseEntity<EntityModel<UserResponse>> update(Long id, UserRequest request) {
        User updated = userService.update(id, request.name(), request.email(), request.age());
        return ResponseEntity.ok(toModel(updated));
    }

    @Override
    public ResponseEntity<Void> delete(Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }

    private EntityModel<UserResponse> toModel(User user) {
        UserResponse dto = UserResponse.from(user);
        return EntityModel.of(
                dto,
                linkTo(methodOn(UserApi.class).get(dto.id())).withSelfRel(),
                linkTo(methodOn(UserApi.class).getAll()).withRel("users"),
                linkTo(methodOn(UserApi.class).update(dto.id(), null)).withRel("update"),
                linkTo(methodOn(UserApi.class).delete(dto.id())).withRel("delete")
        );
    }
}