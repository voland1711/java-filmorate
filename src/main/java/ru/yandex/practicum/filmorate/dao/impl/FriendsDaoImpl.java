package ru.yandex.practicum.filmorate.dao.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.FriendsDao;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class FriendsDaoImpl implements FriendsDao {
    private final JdbcTemplate jdbcTemplate;
    private final UserStorage userStorage;
    private LocalDate localDateEvent;

    public FriendsDaoImpl(JdbcTemplate jdbcTemplate, @Qualifier("UserDbStorage") UserStorage userStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.userStorage = userStorage;
    }

    @Override
    public void addFriend(long userId, long userFriendId) {
        String sqlQueryFriendshipsInsert =
                "merge into friends (user_id, friend_user_id, friendships_status_id, event_timestamp) " +
                        "values (?, ?, ?, ?)";
        localDateEvent = getEventDatestamp();
        if (existAgreeFriendships(userId, userFriendId) && existAgreeFriendships(userFriendId, userId)) {
            throw new ValidationException("Пользователи в настоящее время уже являются друзьями.");
        }
        if (existAgreeFriendships(userId, userFriendId) && !existAgreeFriendships(userFriendId, userId)) {
            throw new ValidationException("Повторное добавление в друзья");
        }

        if (!existAgreeFriendships(userId, userFriendId) && existAgreeFriendships(userFriendId, userId)) {
            jdbcTemplate.update(sqlQueryFriendshipsInsert, userId, userFriendId, 1, localDateEvent);
            jdbcTemplate.update(sqlQueryFriendshipsInsert, userFriendId, userId, 1, localDateEvent);
            log.info("{} пользователи {} и {} подтвердили дружбу", localDateEvent, userId, userFriendId);
            return;
        }
        jdbcTemplate.update(sqlQueryFriendshipsInsert, userId, userFriendId, 0, localDateEvent);
        log.info("{} пользователь {} добавил в друзья пользователя {}.", localDateEvent, userId, userFriendId);
    }

    @Override
    public List<User> getFriends(long userId) {
        String sqlQuerySelectFriendsById = "SELECT u.* from friends AS f" +
                " LEFT JOIN users AS u ON f.friend_user_id = u.user_id WHERE f.user_id = ?";
        return commonsFriends(jdbcTemplate.queryForRowSet(sqlQuerySelectFriendsById, userId));
    }

    @Override
    public void deleteFriend(long userId, long userFriendId) {
        String sqlQueryDelete = "DELETE FROM friends WHERE user_id = ? and friend_user_id = ?";
        String sqlQueryFriendshipsStatusUpdate = "UPDATE friends SET friendships_status_id = ?, event_timestamp = ? " +
                " WHERE friend_user_id = ? and user_id = ?";
        localDateEvent = getEventDatestamp();
        // если подтвержденная дружба
        if (existAgreeFriendships(userFriendId, userId)) {
            jdbcTemplate.update(sqlQueryFriendshipsStatusUpdate, 0, localDateEvent, userId, userFriendId);
            log.info("{} Дружба была подтвержденная, производим смену статуса у пользователя {}",
                    getEventDatestamp(), userFriendId);
        }
        jdbcTemplate.update(sqlQueryDelete, userId, userFriendId);
        log.info("{} Пользователь {} удалил из друзей пользователя {}", getEventDatestamp(), userId, userFriendId);
    }

    @Override
    public List<User> getCommonFriends(long userId, long otherId) {
        return getFriends(userId).stream().filter(getFriends(otherId)::contains).collect(Collectors.toList());
    }

    private boolean existAgreeFriendships(long userId, long userFriendId) {
        String sqlQuerySelectFriends = "SELECT * from friends AS f where f.user_id = ? AND f.friend_user_id = ?";
        SqlRowSet friendsRows = jdbcTemplate.queryForRowSet(sqlQuerySelectFriends, userId, userFriendId);
        return friendsRows.next();
    }

    private List<User> commonsFriends(SqlRowSet sqlRowSet) {
        List<User> tempUsers = new ArrayList<>();
        while (sqlRowSet.next()) {
            int tempUserId = sqlRowSet.getInt("user_id");
            userStorage.findUserById(tempUserId).ifPresent(tempUsers::add);
        }
        return tempUsers;
    }

    private LocalDate getEventDatestamp() {
        return LocalDate.parse(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
    }


}
