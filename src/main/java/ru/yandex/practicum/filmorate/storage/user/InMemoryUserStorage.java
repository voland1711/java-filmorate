package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDateTime;
import java.util.*;


@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {
    private Long userId = 0L;
    private final Map<Long, User> users = new HashMap<>();
    private final Set<String> usersUsedEmail = new HashSet<>();
    private final Set<String> listUsersRegistred = new HashSet<>();

    private Long getGeneratedUserId() {
        return ++userId;
    }

    private void userValidation(User user) {
        if (StringUtils.isBlank(user.getLogin()) && StringUtils.isEmpty(user.getLogin())) {
            throw new ValidationException("Логин не должен быть пустым/содержать пробелы");
        }
        String tempLogin = user.getLogin();

        if (StringUtils.contains(tempLogin, " ")) {
            throw new ValidationException("Логин не должен содержать пробелы");
        }

        if (listUsersRegistred.contains(tempLogin)) {
            throw new ValidationException("Пользователь с логином: '" + tempLogin + "' уже зарегистрирован");
        }

        if (user.getBirthday() == null) {
            throw new ValidationException("Дата рождения не может быть null или отсуствовать");
        }
        if (user.getBirthday().isAfter(LocalDateTime.now().toLocalDate())) {
            throw new ValidationException("Дата рождения не может быть в будущем");
        }

        if (!EmailValidator.getInstance().isValid(user.getEmail())) {
            throw new ValidationException("Адрес электронной почты не соответствует стандартному формату");
        }

        if (checkingExistEmail(user)) {
            throw new ValidationException("Пользователь с указанной электронной почтой уже зарегистрирован");
        }
        log.info("Валидация пользователя " + user + " прошла успешно");
    }

    // Проверка на существование электронной почты в коллекции зарегистрированных пользователей
    private boolean checkingExistEmail(User user) {
        return usersUsedEmail.contains(user.getEmail());
    }

    @Override
    public Collection<User> findAllUsers() {
        return users.values();
    }

    @Override
    public User createUser(User user) {
        userValidation(user);
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        user.setId(getGeneratedUserId());
        users.put(user.getId(), user);
        usersUsedEmail.add(user.getEmail());
        log.info("Пользователь: " + user + " добавлен");
        return user;
    }

    @Override
    public boolean existUser(long userId) {
        log.info("Поступил запрос на существование пользователя с id=" + userId);
        if (!users.containsKey(userId)) {
            throw new ObjectNotFoundException("Пользователь с id = " + userId + " в коллекции не найден");
        }
        log.info("Пользователь с id=" + userId + " существует");
        return true;
    }

    @Override
    public User getUserById(long userId) {
        existUser(userId);
        log.info("Поступил запрос на данные пользователя с id=" + userId);
        return users.get(userId);
    }

    @Override
    public User updateUser(User user) {
        existUser(user.getId());
        log.info("Данные  пользователя  до  обновления: " + users.get(user.getId()));
        userValidation(user);
        users.put(user.getId(), user);
        log.info("Данные пользователя после обновления: " + users.get(user.getId()));
        return user;
    }

}
