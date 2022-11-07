package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import org.apache.commons.validator.routines.EmailValidator;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class UserService {
    private int userId = 0;
    private final Map<Integer, User> users = new HashMap<>();

    public Collection<User> findAll() {
        return users.values();
    }

    private int getUserId() {
        userId++;
        return userId;
    }

    // Проверка на существование электронной почты в списке зарегистрированных пользователей
    private boolean checkingExistEmail(User user) {
        for (User tempUser : users.values()) {
            if (tempUser.getEmail().equals(user.getEmail())) {
                return true;
            }
        }
        return false;
    }

    private User userValidation (User user) {
        if (user.getLogin() == null || user.getLogin().contains(" ") || user.getLogin().isEmpty()) {
            throw new ValidationException("Логин не должен быть пустым/содержать пробелы");
        }

        if (user.getBirthday().isAfter(LocalDateTime.now().toLocalDate())) {
            throw new ValidationException("Дата рождения не может быть в будущем");
        }

        if (checkingExistEmail(user)){
            throw new ValidationException("Пользователь с указанной электронной почтой уже зарегистрирован");
        }

        if (!EmailValidator.getInstance().isValid(user.getEmail())) {
            throw new ValidationException("Адрес электронной почты не соответствует стандартному формату");
        }
        return user;
    }

    public User createUser(User user) {
        if (user.getName() == null) {
            user.setName(user.getLogin());
        }
        userValidation(user);
        user.setId(getUserId());
        users.put(user.getId(), user);
        log.info("Пользователь: " + user + " добавлен");
        return user;
    }

    public User updateUser(User user) {
        if (!users.containsKey(user.getId())) {
            throw new ValidationException("Пользователь " + user.getLogin() + " не найден");
        }
        log.info("Данные  пользователя  до  обновления: " + users.get(user.getId()));
        userValidation(user);
        users.put(user.getId(), user);
        log.info("Данные пользователя после обновления: " + users.get(user.getId()));
        return user;
    }

}
