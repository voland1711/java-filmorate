package ru.yandex.practicum.filmorate.validation;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDateTime;

@Slf4j
@Component
public class UserValidation {

    public void userValidation(User user) {

        if (StringUtils.isBlank(user.getLogin()) && StringUtils.isEmpty(user.getLogin())) {
            throw new ValidationException("Логин не должен быть пустым/содержать пробелы");
        }
        String tempLogin = user.getLogin();

        if (StringUtils.contains(tempLogin, " ")) {
            throw new ValidationException("Логин не должен содержать пробелы");
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

    }
}
