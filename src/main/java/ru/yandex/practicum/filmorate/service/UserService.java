package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.FriendsDao;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.*;


@Service
@Slf4j
public class UserService {
    private final UserStorage userStorage;
    private final FriendsDao friendsDao;

    public UserService(@Qualifier UserStorage userStorage, FriendsDao friendsDao) {
        this.userStorage = userStorage;
        this.friendsDao = friendsDao;
    }

    public Collection<User> findAllUsers() {
        return userStorage.findAllUsers();
    }

    public User createUser(User user) {
        return userStorage.createUser(user);
    }

    public User updateUser(User user) {
        return userStorage.updateUser(user);
    }

    public Optional<User> findUserById(long userId) {
        return userStorage.findUserById(userId);
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