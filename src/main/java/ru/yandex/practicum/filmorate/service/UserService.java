package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;


@Service
@Slf4j
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
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

    public User getUserById(long userId) {
        return userStorage.getUserById(userId);
    }

    public List<User> getFriends(long userId) {
        List<User> tempUsers = new ArrayList<>();
        userStorage.existUser(userId);
        userStorage.getUserById(userId).getListFriendId().
                forEach(friendId -> tempUsers.add(userStorage.getUserById(friendId)));
        return tempUsers;
    }

    public List<User> getCommonFriends(long userId, long otherId) {
        List<User> tempCommonUsers = new ArrayList<>();
        if (userStorage.getUserById(userId).getListFriendId().isEmpty() &&
                userStorage.getUserById(otherId).getListFriendId().isEmpty()) {
            return Collections.EMPTY_LIST;
        }
        for (Long tempUserId : userStorage.getUserById(userId).getListFriendId()) {
            if (userStorage.getUserById(otherId).getListFriendId().contains(tempUserId)) {
                tempCommonUsers.add(getUserById(tempUserId));
            }
        }
        return tempCommonUsers;
    }

    public User addFriend(long userId, long userFriendId) {
        if (userStorage.existUser(userId) && userStorage.existUser(userFriendId)) {
            userStorage.getUserById(userId).getListFriendId().add(userFriendId);
            userStorage.getUserById(userFriendId).getListFriendId().add(userId);
        } else {
            throw new ValidationException("Пользователь с id = " + userId + " в коллекции не найден");
        }
        return userStorage.getUserById(userId);
    }

    public User deleteFriend(long userId, long userFriendId) {
        System.out.println("userId" + userId);
        System.out.println("userFriendId" + userFriendId);
        if (userStorage.existUser(userId) && userStorage.existUser(userFriendId)) {
            userStorage.getUserById(userId).getListFriendId().remove(userFriendId);
            userStorage.getUserById(userFriendId).getListFriendId().remove(userId);
        }
        return userStorage.getUserById(userId);
    }

}