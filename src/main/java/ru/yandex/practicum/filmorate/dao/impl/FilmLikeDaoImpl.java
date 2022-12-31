package ru.yandex.practicum.filmorate.dao.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.FilmLikeDao;
import ru.yandex.practicum.filmorate.model.Film;


@Slf4j
@Component
public class FilmLikeDaoImpl implements FilmLikeDao {
    JdbcTemplate jdbcTemplate;

    public FilmLikeDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void addLike(Film film, long userId) {
        long filmId = film.getId();
        String sqlQueryInsertLike = "MERGE into film_like (film_id, user_id) values ( ?, ? )";
        jdbcTemplate.update(sqlQueryInsertLike, filmId, userId);
    }

    @Override
    public void deleteLike(Film film, long userId) {
        long filmId = film.getId();
        String sqlQueryDeleteLike = "delete from film_like  where film_id = ? and user_id = ?";
        jdbcTemplate.update(sqlQueryDeleteLike, filmId, userId);
    }

    @Override
    public long findLikeFilm(long filmId) {
        String sqlQuerySelectLike = "SELECT COUNT(film_id) AS countLike FROM film_like WHERE film_id = ? GROUP BY film_id";
        SqlRowSet rs = jdbcTemplate.queryForRowSet(sqlQuerySelectLike, filmId);
        if (rs.next()) {
            return rs.getLong("countLike");
        }
        return 0;
    }

}
