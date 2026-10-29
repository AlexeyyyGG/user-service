package com.example.userservice.web.controller.api;

import com.example.userservice.web.dto.ErrorResponse;
import com.example.userservice.web.dto.UserRequest;
import com.example.userservice.web.dto.UserResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Tag(name = "Users", description = "API для управления пользователями")
@RequestMapping("/api/users")
public interface UserApi {
    @Operation(
            summary = "Получить всех пользователей",
            description = "Возвращает список всех пользователей с HATEOAS-ссылками для навигации"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Пользователи успешно получены"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<CollectionModel<EntityModel<UserResponse>>> getAll();

    @Operation(
            summary = "Получить пользователя по ID",
            description = "Возвращает данные пользователя по его идентификатору"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Пользователь найден"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<EntityModel<UserResponse>> get(
            @Parameter(description = "ID пользователя", required = true, example = "1")
            @PathVariable("id") Long id);

    @Operation(
            summary = "Создать нового пользователя",
            description = "Создает нового пользователя и возвращает его данные"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Пользователь успешно создан"),
            @ApiResponse(responseCode = "400", description = "Ошибка валидации входных данных",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "Пользователь с таким Email уже существует",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<EntityModel<UserResponse>> add(
            @Valid @RequestBody UserRequest request
    );

    @Operation(
            summary = "Обновить данные пользователя",
            description = "Обновляет существующего пользователя по его ID"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Пользователь успешно обновлен"),
            @ApiResponse(responseCode = "400", description = "Ошибка валидации входных данных",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "Пользователь с таким Email уже существует",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PutMapping(value = "/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<EntityModel<UserResponse>> update(
            @Parameter(description = "ID пользователя", required = true, example = "1")
            @PathVariable("id") Long id,
            @Valid @RequestBody UserRequest request
    );

    @Operation(
            summary = "Удалить пользователя",
            description = "Удаляет пользователя по его ID"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Пользователь успешно удален"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping("/{id}")
    ResponseEntity<Void> delete(
            @Parameter(description = "ID пользователя", required = true, example = "1")
            @PathVariable("id") Long id);
}