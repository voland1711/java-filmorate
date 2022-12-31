package ru.yandex.practicum.filmorate.dao.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.MpaDao;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
public class MpaDaoImpl implements MpaDao {

    private final JdbcTemplate jdbcTemplate;

    public MpaDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<Mpa> getMpaById(int mpaId) {
        existMpa(mpaId);
        String sqlQuery = "SELECT * FROM film_mpa WHERE film_mpa_id = ?";
        SqlRowSet mpaRows = jdbcTemplate.queryForRowSet(sqlQuery, mpaId);
        if (mpaRows.next()) {
            Mpa mpa = new Mpa(
                    mpaRows.getInt("film_mpa_id"),
                    mpaRows.getString("mpa_name"));
            log.info("Для фильма с id = {}, рейтинг = {}", mpa.getId(), mpa.getName());
            return Optional.of(mpa);
        } else {
            log.info("Рейтинг MPA с идентификатором {} не найден.", mpaId);
            return Optional.empty();
        }
    }

    @Override
    public List<Mpa> findAllMpa() {
        log.info("Поступил запрос на получение всех видов рейтинга");
        String sqlQuery = "SELECT * FROM film_mpa";
        return jdbcTemplate.query(sqlQuery, this::rsToMpa);
    }

    // проверяем на существование в БД рейтинга с переданным id
    @Override
    public boolean existMpa(int mpaId) {
        log.info("Поступил запрос на получение названия рейтинга c id = {}", mpaId);
        String sqlQuery = "SELECT * from film_mpa WHERE film_mpa_id = ?";
        if (jdbcTemplate.queryForRowSet(sqlQuery, mpaId).next()) {
            return true;
        }
        throw new ObjectNotFoundException("Рейтинг с идентификатором = " + mpaId + " не существует");
    }

    private Mpa rsToMpa(ResultSet rs, int i) throws SQLException {
        return new Mpa(rs.getInt("film_mpa_id"), rs.getString("mpa_name"));

    }

}
