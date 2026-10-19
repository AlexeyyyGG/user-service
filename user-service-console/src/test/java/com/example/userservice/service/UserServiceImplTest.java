package com.example.userservice.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.userservice.dao.UserDao;
import com.example.userservice.exception.EmailAlreadyExistsException;
import com.example.userservice.exception.UserNotFoundException;
import com.example.userservice.model.User;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @Mock
    private UserDao userDao;
    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void createShouldSaveUserWhenEmailIsFree() {
        User user = new User("Alexey", "alexey@mail.ru", 25);
        when(userDao.getByEmail("alexey@mail.ru")).thenReturn(null);
        when(userDao.save(any(User.class))).thenReturn(user);
        User result = userService.create("Alexey", "alexey@mail.ru", 25);
        assertNotNull(result);
        assertEquals("Alexey", result.getName());
        verify(userDao).save(any(User.class));
    }

    @Test
    void createShouldThrowWhenEmailExists() {
        when(userDao.getByEmail("alexey@mail.ru"))
                .thenReturn(new User("Alex", "alexey@mail.ru", 37));
        assertThrows(
                EmailAlreadyExistsException.class,
                () -> userService.create("Alexey", "alexey@mail.ru", 25));
        verify(userDao, never()).save(any(User.class));
    }

    @Test
    void getByIdShouldReturnUserWhenExists() {
        User user = new User("Alexey", "alexey@mail.ru", 25);
        when(userDao.getById(1L)).thenReturn(user);
        User result = userService.getById(1L);
        assertSame(user, result);
    }

    @Test
    void getByIdShouldThrowWhenNotFound() {
        when(userDao.getById(616L)).thenReturn(null);
        assertThrows(UserNotFoundException.class, () -> userService.getById(616L));
    }

    @Test
    void getAllShouldReturnList() {
        when(userDao.getAll()).thenReturn(List.of(
                new User("Alexey", "alexey@mail.ru", 25),
                new User("Vital", "vital@mail.ru", 30)
        ));
        List<User> result = userService.getAll();
        assertEquals(2, result.size());
    }

    @Test
    void getAllShouldReturnEmptyList() {
        when(userDao.getAll()).thenReturn(List.of());
        List<User> result = userService.getAll();
        assertTrue(result.isEmpty());
    }

    @Test
    void updateShouldUpdateUser() {
        User existing = new User("Alexey", "alexey@mail.ru", 25);
        when(userDao.getById(1L)).thenReturn(existing);
        when(userDao.getByEmail("new@mail.com")).thenReturn(null);
        when(userDao.update(existing)).thenReturn(existing);
        User result = userService.update(1L, "Robert", "new@mail.com", 33);
        assertEquals("Robert", result.getName());
        assertEquals("new@mail.com", result.getEmail());
        assertEquals(33, result.getAge());
        verify(userDao).update(existing);
    }

    @Test
    void updateShouldThrowWhenUserNotFound() {
        when(userDao.getById(999L)).thenReturn(null);
        assertThrows(
                UserNotFoundException.class,
                () -> userService.update(999L, "Alexey", "alexey@mail.ru", 25));
        verify(userDao, never()).update(any());
    }

    @Test
    void deleteShouldCallDaoWhenUserExists() {
        when(userDao.getById(1L)).thenReturn(new User("Alexey", "alexey@mail.ru", 25));
        userService.delete(1L);
        verify(userDao).deleteById(1L);
    }

    @Test
    void deleteShouldThrowWhenUserNotFound() {
        when(userDao.getById(616L)).thenReturn(null);
        assertThrows(UserNotFoundException.class, () -> userService.delete(616L));
        verify(userDao, never()).deleteById(any());
    }
}