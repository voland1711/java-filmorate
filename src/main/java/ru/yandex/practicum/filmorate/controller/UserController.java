package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        log.info("Запрос на добавление пользователя: " + user);
        return userService.createUser(user);
    }

    @GetMapping
    public Collection<User> findAll() {
        log.info("Запрос для вывода всех пользователей");
        return userService.findAllUsers();
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        log.info("Запрос на обновление пользователя с id=" + user.getId() + " новые данные: " + user);
        return userService.updateUser(user);
    }

    @GetMapping("/{userId}")
    public User getUserById(@PathVariable Long userId) {
        return userService.getUserById(userId);
    }

    @GetMapping("/{userId}/friends")
    public List<User> getFriends(@PathVariable Long userId) {
        return userService.getFriends(userId);
    }

    @PutMapping("/{userId}/friends/{userFriendId}")
    public User addFriend(@PathVariable Long userId, @PathVariable Long userFriendId) {
        return userService.addFriend(userId, userFriendId);
    }

    @DeleteMapping("/{userId}/friends/{userFriendId}")
    public User deleteFriend(@PathVariable Long userId, @PathVariable Long userFriendId) {
        return userService.deleteFriend(userId, userFriendId);
    }

    @GetMapping("/{userId}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable long userId, @PathVariable long otherId) {
        return userService.getCommonFriends(userId, otherId);
    }
}


