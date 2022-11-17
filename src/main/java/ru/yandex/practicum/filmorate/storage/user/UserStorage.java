package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface UserStorage {
    Collection<User> findAllUsers();

    User createUser(User user);

    boolean existUser(long userId);

    User getUserById(long userId);

    User updateUser(User user);
}
