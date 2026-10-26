package com.example.userservice.web.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.userservice.common.model.User;
import com.example.userservice.web.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
class UserControllerTest {
    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:17")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    void createShouldReturn201AndUserResponse() throws Exception {
        String body = """
                {
                    "name": "Alexey",
                    "email": "alexey@mail.ru",
                    "age": 25
                }
                """;
        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name").value("Alexey"))
                .andExpect(jsonPath("$.email").value("alexey@mail.ru"))
                .andExpect(jsonPath("$.age").value(25))
                .andExpect(jsonPath("$.createdAt").isNotEmpty());
    }

    @Test
    void createShouldReturn400WhenAllFieldsInvalid() throws Exception {
        String body = """
                {
                    "name": "",
                    "email": "not-email",
                    "age": 15
                }
                """;
        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").value("Ошибка валидации"))
                .andExpect(jsonPath("$.fieldErrors.name").exists())
                .andExpect(jsonPath("$.fieldErrors.email").exists())
                .andExpect(jsonPath("$.fieldErrors.age").exists());
    }

    @Test
    void createShouldReturn400WhenEmailInvalid() throws Exception {
        String body = """
                {
                    "name": "Alexey",
                    "email": "not-email",
                    "age": 25
                }
                """;
        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fieldErrors.email").exists());
    }

    @Test
    void createShouldReturn409WhenEmailAlreadyExists() throws Exception {
        userRepository.save(new User("Existing", "alexey@mail.ru", 30));
        String body = """
                {
                    "name": "Alexey",
                    "email": "alexey@mail.ru",
                    "age": 25
                }
                """;
        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.error").value("Conflict"))
                .andExpect(jsonPath("$.message").value(
                        "Пользователь с email alexey@mail.ru уже существует"));
    }

    @Test
    void getAllShouldReturnEmptyArray() throws Exception {
        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void getAllShouldReturnAllUsers() throws Exception {
        userRepository.save(new User("Alexey", "alexey@mail.ru", 25));
        userRepository.save(new User("Bob", "bob@mail.ru", 30));
        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void getByIdShouldReturnUser() throws Exception {
        User saved = userRepository.save(new User("Alexey", "alexey@mail.ru", 25));
        mockMvc.perform(get("/api/users/{id}", saved.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(saved.getId()))
                .andExpect(jsonPath("$.name").value("Alexey"))
                .andExpect(jsonPath("$.email").value("alexey@mail.ru"))
                .andExpect(jsonPath("$.age").value(25));
    }

    @Test
    void getByIdShouldReturn404WhenNotFound() throws Exception {
        mockMvc.perform(get("/api/users/{id}", 999L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value("Пользователь с id 999 не найден"));
    }

    @Test
    void updateShouldReturn200AndUpdatedUser() throws Exception {
        User saved = userRepository.save(new User("Alexey", "alexey@mail.ru", 25));
        String body = """
                {
                    "name": "Alexander",
                    "email": "alexey@mail.ru",
                    "age": 26
                }
                """;
        mockMvc.perform(put("/api/users/{id}", saved.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(saved.getId()))
                .andExpect(jsonPath("$.name").value("Alexander"))
                .andExpect(jsonPath("$.email").value("alexey@mail.ru"))
                .andExpect(jsonPath("$.age").value(26));
    }

    @Test
    void updateShouldReturn409WhenEmailTakenByAnother() throws Exception {
        User first = userRepository.save(new User("Alexey", "alexey@mail.ru", 25));
        userRepository.save(new User("Bob", "bob@mail.ru", 30));
        String body = """
                {
                    "name": "Alexander",
                    "email": "bob@mail.ru",
                    "age": 26
                }
                """;
        mockMvc.perform(put("/api/users/{id}", first.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(
                        jsonPath("$.message").value(
                                "Пользователь с email bob@mail.ru уже существует"));
    }

    @Test
    void updateShouldReturn404WhenUserNotFound() throws Exception {
        String body = """
                {
                    "name": "Robert",
                    "email": "robert@mail.ru",
                    "age": 30
                }
                """;
        mockMvc.perform(put("/api/users/{id}", 999L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Пользователь с id 999 не найден"));
    }

    @Test
    void updateShouldReturn400WhenValidationFails() throws Exception {
        User saved = userRepository.save(new User("Alexey", "alexey@mail.ru", 25));
        String body = """
                {
                    "name": "",
                    "email": "not-email",
                    "age": 15
                }
                """;
        mockMvc.perform(put("/api/users/{id}", saved.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fieldErrors.name").exists())
                .andExpect(jsonPath("$.fieldErrors.email").exists())
                .andExpect(jsonPath("$.fieldErrors.age").exists());
    }

    @Test
    void deleteShouldReturn204() throws Exception {
        User saved = userRepository.save(new User("Alexey", "alexey@mail.ru", 25));
        mockMvc.perform(delete("/api/users/{id}", saved.getId()))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteShouldReturn404WhenUserNotFound() throws Exception {
        mockMvc.perform(delete("/api/users/{id}", 999L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Пользователь с id 999 не найден"));
    }
}