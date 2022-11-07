package ru.yandex.practicum.filmorate.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class UserServiceTest {

    @Test
    @DisplayName("1) Добавление объекта User: Логин пустой")
    void createUserWithLoginEmptyTest() {
        User user = new User();
        user.setId(1);
        user.setName("Nick Name");
        user.setLogin("");
        user.setEmail("mail@mail.ru");
        user.setBirthday(LocalDate.of(1946, 8, 20));
        UserService userService = new UserService();
        assertEquals(0, userService.findAll().size());
        try {
            userService.createUser(user);
        } catch (ValidationException e) {
            System.out.println("Ошибка валидации: у пользователя отсуствует логин");
        }
        assertEquals(0, userService.findAll().size(), "Пользователь был добавлен, с пустым логином");
    }

    @Test
    @DisplayName("2) Добавление объекта User: Логин содержит пробел")
    void createUserLoginContainsSpaceTest() {
        User user = new User();
        user.setId(1);
        user.setName("Nick Name");
        user.setLogin("dol ore");
        user.setEmail("mail@mail.ru");
        user.setBirthday(LocalDate.of(1946, 8, 20));
        UserService userService = new UserService();
        assertEquals(0, userService.findAll().size());
        try {
            userService.createUser(user);
        } catch (ValidationException e) {
            System.out.println("Ошибка валидации: логин пользователя содержит пробелы");
        }
        assertEquals(0, userService.findAll().size(),
                "Пользователь был добавлен, при наличии пробела в логине");
    }

    @Test
    @DisplayName("3) Добавление объекта User: Дата рождения в будущем")
    void createUserBirthdateInFutureTest() {
        User user = new User();
        user.setId(1);
        user.setName("Nick Name");
        user.setLogin("dolore");
        user.setEmail("mail@mail.ru");
        user.setBirthday(LocalDate.of(2046, 8, 20));
        UserService userService = new UserService();
        assertEquals(0, userService.findAll().size());
        try {
            userService.createUser(user);
        } catch (ValidationException e) {
            System.out.println("Ошибка валидации: Дата рождения в будущем");
        }
        assertEquals(0, userService.findAll().size(),
                "Добавлен пользователь, с датой рождения в будущем");
    }

    @Test
    @DisplayName("4) Добавление объекта User: Неправильный E-mail адрес")
    void createUserIncorrectEmailTest() {
        User user = new User();
        user.setId(1);
        user.setName("Nick Name");
        user.setLogin("dolore");
        user.setEmail("это--неправильный?эмейл@");
        user.setBirthday(LocalDate.of(1946, 8, 20));
        UserService userService = new UserService();
        assertEquals(0, userService.findAll().size());
        try {
            userService.createUser(user);
        } catch (ValidationException e) {
            System.out.println("Ошибка валидации: Неправильный E-mail адрес");
        }
        assertEquals(0, userService.findAll().size(),
                "Добавлен пользователь с неправильным адресом электронной почты");
    }

    @Test
    @DisplayName("5) Добавление объекта User: E-mail адрес не заполнен")
    void createUserEmailEmptyTest() {
        User user = new User();
        user.setId(1);
        user.setName("Nick Name");
        user.setLogin("dolore");
        user.setBirthday(LocalDate.of(1946, 8, 20));
        UserService userService = new UserService();
        try {
            userService.createUser(user);
        } catch (ValidationException e) {
            System.out.println("Ошибка валидации: E-mail адрес не заполнен");
        }
        assertEquals(0, userService.findAll().size(),
                "Добавлен пользователь с неправильным адресом электронной почты");
    }

    @Test
    @DisplayName("6) Добавление объекта User: Поле Name не заполнено")
    void createUserWithoutNameTest() {
        User user = new User();
        user.setId(1);
        user.setLogin("dolore");
        user.setEmail("mail@mail.ru");
        user.setBirthday(LocalDate.of(1946, 8, 20));
        UserService userService = new UserService();
        // Проверяем, что коллекция пользователей пуста
        assertEquals(0, userService.findAll().size());
        userService.createUser(user);
        // Проверяем что пользователь добавлен, и в поле имя заполнено логином
        assertEquals("dolore", userService.findAll().iterator().next().getName(),
                "Пользователь не добавлен в коллекцию");
        assertEquals(1, userService.findAll().size(),
                "Пользователь не добавлен в коллекцию");
    }

    @Test
    @DisplayName("7) Проверка обновления пользователя User в коллекции")
    void updateUserTest() {
        User user = new User();
        user.setId(1);
        user.setLogin("dolore");
        user.setEmail("mail@mail.ru");
        user.setBirthday(LocalDate.of(1946, 8, 20));
        UserService userService = new UserService();
        userService.createUser(user);
        assertTrue(userService.findAll().contains(user), "Пользователь не добавлен в коллекцию");
        assertEquals(1, userService.findAll().size(),
                "В коллекции должен быть только один пользователь");

        // Обновляем поля объекта User
        User userUpdate = new User();
        userUpdate.setId(1);
        userUpdate.setLogin("doloreUpdate");
        userUpdate.setName("user update");
        userUpdate.setEmail("mail@yandex.ru");
        userUpdate.setBirthday(LocalDate.of(1986, 11, 17));
        userService.updateUser(userUpdate);
        assertEquals(1, userService.findAll().size(),
                "В коллекции должен быть только один пользователь");
        assertTrue(userService.findAll().contains(userUpdate), "Пользователь не обновлен");
    }

    @Test
    @DisplayName("8) Проверка обновления неизвестного пользователя User в коллекции")
    void updateUnknownUserTest() {
        User user = new User();
        user.setId(1);
        user.setLogin("dolore");
        user.setEmail("mail@mail.ru");
        user.setBirthday(LocalDate.of(1946, 8, 20));
        UserService userService = new UserService();
        userService.createUser(user);
        assertTrue(userService.findAll().contains(user), "Пользователь не добавлен в коллекцию");
        assertEquals(1, userService.findAll().size(),
                "В коллекции должен быть только один пользователь");
        User unknownUser = new User();
        unknownUser.setId(2);
        unknownUser.setLogin("doloreUpdate");
        unknownUser.setName("user update");
        unknownUser.setEmail("mail@yandex.ru");
        unknownUser.setBirthday(LocalDate.of(1986, 11, 17));
        try {
            userService.updateUser(unknownUser);
        } catch (ValidationException e) {
            System.out.println("Необходимый пользователь для обновления отсутствует в коллекции");
        }
        assertEquals(1, userService.findAll().size(),
                "В коллекции должен быть только один пользователь");
        assertTrue(userService.findAll().contains(user),
                "Обновлен несуществующий пользователь в коллекции");
    }

    @Test
    @DisplayName("9) Обновляем пользователя, логин содержит пробелы")
    void updateUserContainsSpaceTest() {
        User user = new User();
        user.setId(1);
        user.setLogin("dolore");
        user.setEmail("mail@mail.ru");
        user.setBirthday(LocalDate.of(1946, 8, 20));
        UserService userService = new UserService();
        userService.createUser(user);
        assertTrue(userService.findAll().contains(user), "Пользователь не добавлен в коллекцию");
        assertEquals(1, userService.findAll().size(),
                "В коллекции должен быть только один пользователь");
        User tempUser = new User();
        tempUser.setId(1);
        tempUser.setLogin("dolore Update");
        tempUser.setName("user update");
        tempUser.setEmail("mail@yandex.ru");
        tempUser.setBirthday(LocalDate.of(1986, 11, 17));
        try {
            userService.updateUser(tempUser);
        } catch (ValidationException e) {
            System.out.println("Необходимый пользователь для обновления отсутствует в коллекции");
        }
        assertTrue(userService.findAll().contains(user),
                "Ошибочное обновление: некорректные данные полей: логин содержит пробелы");
    }

    @Test
    @DisplayName("10) Обновляем пользователя, e-mail отсутствует")
    void updateUserEmailEmptyTest() {
        User user = new User();
        user.setId(1);
        user.setLogin("dolore");
        user.setEmail("mail@mail.ru");
        user.setBirthday(LocalDate.of(1946, 8, 20));
        UserService userService = new UserService();
        userService.createUser(user);
        assertTrue(userService.findAll().contains(user), "Пользователь не добавлен в коллекцию");
        assertEquals(1, userService.findAll().size(),
                "В коллекции должен быть только один пользователь");
        User tempUser = new User();
        tempUser.setId(1);
        tempUser.setLogin("doloreUpdate");
        tempUser.setName("user update");
        tempUser.setEmail("");
        tempUser.setBirthday(LocalDate.of(1986, 11, 17));
        try {
            userService.updateUser(tempUser);
        } catch (ValidationException e) {
            System.out.println("Необходимый пользователь для обновления отсутствует в коллекции");
        }
        assertTrue(userService.findAll().contains(user),
                "Ошибочное обновление: некорректные данные полей: e-mail отсутствует");
    }

    @Test
    @DisplayName("11) Обновляем пользователя, e-mail не соотвествует стандарту")
    void updateUserIncorrectEmailTest() {
        User user = new User();
        user.setId(1);
        user.setLogin("dolore");
        user.setEmail("mail@mail.ru");
        user.setBirthday(LocalDate.of(1946, 8, 20));
        UserService userService = new UserService();
        userService.createUser(user);
        assertTrue(userService.findAll().contains(user), "Пользователь не добавлен в коллекцию");
        assertEquals(1, userService.findAll().size(),
                "В коллекции должен быть только один пользователь");
        User tempUser = new User();
        tempUser.setId(1);
        tempUser.setLogin("doloreUpdate");
        tempUser.setName("user update");
        tempUser.setEmail("это--неправильный?эмейл@");
        tempUser.setBirthday(LocalDate.of(1986, 11, 17));
        try {
            userService.updateUser(tempUser);
        } catch (ValidationException e) {
            System.out.println("Необходимый пользователь для обновления отсутствует в коллекции");
        }
        assertTrue(userService.findAll().contains(user),
                "Ошибочное обновление: некорректные данные полей: e-mail не соотвествует стандарту");
    }

    @Test
    @DisplayName("12) Обновляем пользователя, дата рождения указана в будущем")
    void updateUserBirthdateInFutureTest() {
        User user = new User();
        user.setId(1);
        user.setLogin("dolore");
        user.setEmail("mail@mail.ru");
        user.setBirthday(LocalDate.of(1946, 8, 20));
        UserService userService = new UserService();
        userService.createUser(user);
        assertTrue(userService.findAll().contains(user), "Пользователь не добавлен в коллекцию");
        assertEquals(1, userService.findAll().size(),
                "В коллекции должен быть только один пользователь");
        User tempUser = new User();
        tempUser.setId(1);
        tempUser.setLogin("doloreUpdate");
        tempUser.setName("user update");
        tempUser.setEmail("new_mail@yandex.ru");
        tempUser.setBirthday(LocalDate.of(2186, 11, 17));
        try {
            userService.updateUser(tempUser);
        } catch (ValidationException e) {
            System.out.println("Необходимый пользователь для обновления отсутствует в коллекции");
        }
        assertTrue(userService.findAll().contains(user),
                "Ошибочное обновление: некорректные данные полей: дата рождения указана в будущем");
    }

}