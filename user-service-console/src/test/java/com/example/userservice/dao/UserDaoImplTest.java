package com.example.userservice.dao;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.example.userservice.model.User;
import java.util.List;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
class UserDaoImplTest {
    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:17")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");
    private static SessionFactory sessionFactory;
    private UserDaoImpl userDao;

    @BeforeAll
    static void beforeAll() {
        Configuration config = new Configuration();
        config.setProperty("hibernate.connection.driver_class", "org.postgresql.Driver");
        config.setProperty("hibernate.connection.url", postgres.getJdbcUrl());
        config.setProperty("hibernate.connection.username", postgres.getUsername());
        config.setProperty("hibernate.connection.password", postgres.getPassword());
        config.setProperty("hibernate.hbm2ddl.auto", "create-drop");
        config.setProperty("hibernate.show_sql", "false");
        config.addAnnotatedClass(User.class);
        sessionFactory = config.buildSessionFactory();
    }

    @BeforeEach
    void setUp() {
        userDao = new UserDaoImpl(sessionFactory);
        try (var session = sessionFactory.openSession()) {
            var transaction = session.beginTransaction();
            session.createMutationQuery("DELETE FROM User").executeUpdate();
            transaction.commit();
        }
    }

    @AfterAll
    static void afterAll() {
        if (sessionFactory != null) {
            sessionFactory.close();
        }
    }

    @Test
    void saveShouldPersistUser() {
        User user = new User("Alexey", "alexey@mail.ru", 25);
        User saved = userDao.save(user);
        assertNotNull(saved.getId());
        assertEquals("Alexey", saved.getName());
        assertEquals("alexey@mail.ru", saved.getEmail());
        assertEquals(25, saved.getAge());
        assertNotNull(saved.getCreatedAt());
    }

    @Test
    void saveShouldThrowOnDuplicateEmail() {
        userDao.save(new User("Alexey", "alexey@mail.ru", 25));
        User duplicate = new User("Alex", "alexey@mail.ru", 33);
        assertThrows(ConstraintViolationException.class, () -> userDao.save(duplicate));
    }

    @Test
    void getByIdShouldReturnUser() {
        User saved = userDao.save(new User("Alexey", "alexey@mail.ru", 25));
        User found = userDao.getById(saved.getId());
        assertNotNull(found);
        assertEquals(saved.getId(), found.getId());
        assertEquals(saved.getName(), found.getName());
        assertEquals(saved.getEmail(), found.getEmail());
        assertEquals(saved.getAge(), found.getAge());
        assertNotNull(found.getCreatedAt());
    }

    @Test
    void getByIdShouldReturnNullIfNotFound() {
        User found = userDao.getById(616L);
        assertNull(found);
    }

    @Test
    void getByEmailShouldReturnUser() {
        User saved = userDao.save(new User("Alexey", "alexey@mail.ru", 25));
        User found = userDao.getByEmail(saved.getEmail());
        assertNotNull(found);
        assertEquals(saved.getName(), found.getName());
        assertEquals(saved.getEmail(), found.getEmail());
    }

    @Test
    void getByEmailShouldReturnNullIfNotFound() {
        User found = userDao.getByEmail("unknown@mail.ru");
        assertNull(found);
    }

    @Test
    void getAllShouldReturnAllUsers() {
        userDao.save(new User("Alexey", "alexey@mail.ru", 25));
        userDao.save(new User("Vital", "vital@mail.ru", 30));
        userDao.save(new User("Robert", "robert@mail.ru", 35));
        List<User> users = userDao.getAll();
        assertEquals(3, users.size());
    }

    @Test
    void getAllShouldReturnEmptyList() {
        List<User> users = userDao.getAll();
        assertTrue(users.isEmpty());
    }

    @Test
    void updateShouldModifyUserInDb() {
        User saved = userDao.save(new User("Alexey", "alexey@mail.ru", 25));
        saved.setName("Robert");
        saved.setAge(33);
        userDao.update(saved);
        User reloaded = userDao.getById(saved.getId());
        assertEquals("Robert", reloaded.getName());
        assertEquals(33, reloaded.getAge());
        assertEquals("alexey@mail.ru", reloaded.getEmail());
    }

    @Test
    void updateShouldThrowOnDuplicateEmail() {
        User alexey = userDao.save(new User("Alexey", "alexey@mail.ru", 25));
        userDao.save(new User("Bob", "bob@mail.ru", 30));
        alexey.setEmail("bob@mail.ru");
        assertThrows(ConstraintViolationException.class, () -> userDao.update(alexey));
    }

    @Test
    void deleteByIdShouldRemoveUserFromDb() {
        User saved = userDao.save(new User("Alexey", "alexey@mail.ru", 25));
        userDao.deleteById(saved.getId());
        User found = userDao.getById(saved.getId());
        assertNull(found);
    }

    @Test
    void deleteByIdShouldNotFailIfUserMissing() {
        assertDoesNotThrow(() -> userDao.deleteById(616L));
    }
}