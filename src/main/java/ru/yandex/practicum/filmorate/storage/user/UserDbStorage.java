package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Component
@Qualifier("UserDbStorage")
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;

    }


    @Override
    public List<User> findAllUsers() {
        String sqlQuerySelect = "SELECT * FROM users";
        return jdbcTemplate.query(sqlQuerySelect, this::rsToUser);
    }

    @Override
    public User createUser(User user) {
        checkingExistEmail(user.getEmail());
        checkingExistLogin(user.getLogin());
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        String sqlQueryInsert = "INSERT INTO users(user_login, user_email, user_name, user_birthday) VALUES (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sqlQueryInsert, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, user.getLogin());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getName());
            ps.setObject(4, user.getBirthday());
            return ps;
        }, keyHolder);
        long userid = Objects.requireNonNull(keyHolder.getKey()).intValue();
        user.setId(userid);
        return findUserById(userid).orElseThrow();
    }

    @Override
    public Optional<User> findUserById(long userId) {
        existUser(userId);
        String sqlQuerySelect = "SELECT * FROM users WHERE user_id = ?";
        SqlRowSet sqlRowSetSelect = jdbcTemplate.queryForRowSet(sqlQuerySelect, userId);
        if (sqlRowSetSelect.next()) {
            return Optional.of(jdbcTemplate.queryForObject(sqlQuerySelect, this::rsToUser, userId));
        }
        log.info("Пользователь в коллекции зарегистрированных не обнаружен");
        return Optional.empty();
    }

    @Override
    public User updateUser(User user) {
        existUser(user.getId());
        if (user.getName() == null || user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        String sqlQueryUpdate = "UPDATE users SET user_id = ?, user_login = ?, user_email = ?, user_name = ?, user_birthday = ?";
        jdbcTemplate.update(sqlQueryUpdate, user.getId(), user.getLogin(), user.getEmail(), user.getName(), user.getBirthday());
        return user;
    }

    @Override
    public boolean existUser(long userId) {
        String sqlQuerySelect = "SELECT * FROM users WHERE user_id = ?";
        if (jdbcTemplate.queryForRowSet(sqlQuerySelect, userId).next()) {
            return true;
        }
        throw new ObjectNotFoundException("Пользователь c идентификатором = " + userId + " в коллекции зарегистрированных не обнаружен");
    }

    // Проверка на существование электронной почты в коллекции зарегистрированных пользователей
    public boolean checkingExistEmail(String email) {
        String sqlQuerySelect = "SELECT * FROM users WHERE user_email = ?";
        SqlRowSet userRowsEmail = jdbcTemplate.queryForRowSet(sqlQuerySelect, email);
        if (userRowsEmail.next()) {
            throw new ValidationException("Пользователь с указанной электронной почтой уже зарегистрирован");
        }
        return false;
    }

    // Проверка на существование логина в коллекции зарегистрированных пользователей
    public boolean checkingExistLogin(String login) {
        String sqlQuerySelect = "SELECT * FROM users WHERE user_login = ?";
        SqlRowSet userRowsLogin = jdbcTemplate.queryForRowSet(sqlQuerySelect, login);
        if (userRowsLogin.next()) {
            throw new ValidationException("Пользователь с указанным логином уже зарегистрирован");
        }
        return false;
    }

    private User rsToUser(ResultSet rs, int i) throws SQLException {
        return User.builder()
                .id(rs.getLong("user_id"))
                .login(rs.getString("user_login"))
                .email(rs.getString("user_email"))
                .name(rs.getString("user_name"))
                .birthday(rs.getDate("user_birthday").toLocalDate()).build();
    }
}
