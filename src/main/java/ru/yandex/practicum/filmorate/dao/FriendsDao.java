package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface FriendsDao {
    List<User> getFriends(long userId);

    List<User> getCommonFriends(long userId, long otherId);

    void addFriend(long userId, long userFriendId);

    void deleteFriend(long userId, long userFriendId);
}
