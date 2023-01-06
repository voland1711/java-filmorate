package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.FriendsDao;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import ru.yandex.practicum.filmorate.validation.UserValidation;

import java.util.*;

@Service
@Slf4j
public class UserService {
    private final UserStorage userStorage;
    private final FriendsDao friendsDao;
    private final UserValidation userValidation;

    public UserService(@Qualifier("UserDbStorage") UserStorage userStorage, FriendsDao friendsDao, UserValidation userValidation) {
        this.userStorage = userStorage;
        this.friendsDao = friendsDao;
        this.userValidation = userValidation;
    }

    public Collection<User> findAllUsers() {
        return userStorage.findAllUsers();
    }

    public User createUser(User user) {
        userValidation.userValidation(user);
        return userStorage.createUser(user);
    }

    public User updateUser(User user) {
        userValidation.userValidation(user);
        return userStorage.updateUser(user);
    }

    public User findUserById(long userId) {
        return userStorage.findUserById(userId).orElseThrow(() -> new ObjectNotFoundException("Пользователь не найден"));
    }

    public Collection<User> getFriends(long userId) {
        userStorage.existUser(userId);
        return friendsDao.getFriends(userId);
    }

    public List<User> getCommonFriends(long userId, long otherId) {
        userStorage.existUser(userId);
        userStorage.existUser(otherId);
        return friendsDao.getCommonFriends(userId, otherId);
    }

    public void addFriend(long userId, long userFriendId) {
        userStorage.existUser(userId);
        userStorage.existUser(userFriendId);
        friendsDao.addFriend(userId, userFriendId);
    }

    public void deleteFriend(long userId, long userFriendId) {
        userStorage.existUser(userId);
        userStorage.existUser(userFriendId);
        friendsDao.deleteFriend(userId, userFriendId);
    }

}