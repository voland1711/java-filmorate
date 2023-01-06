package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Optional;

public interface UserStorage {
    List<User> findAllUsers();

    User createUser(User user);

    Optional<User> findUserById(long userId);

    User updateUser(User user);

    boolean existUser(long userId);
}
