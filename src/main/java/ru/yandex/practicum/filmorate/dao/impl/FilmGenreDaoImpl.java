package ru.yandex.practicum.filmorate.dao.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.FilmGenreDao;
import ru.yandex.practicum.filmorate.dao.GenreDao;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.*;

@Slf4j
@Component
public class FilmGenreDaoImpl implements FilmGenreDao {
    private final JdbcTemplate jdbcTemplate;
    private final GenreDao genreDao;

    public FilmGenreDaoImpl(JdbcTemplate jdbcTemplate, GenreDao genreDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.genreDao = genreDao;
    }

    @Override
    public List<Genre> findByFilmId(Long filmId) {
        List<Genre> genres = new ArrayList<>();
        String sqlQueryGenreSelect = "SELECT fg.genre_id, g.genre_name FROM film_genre AS fg " +
                " INNER JOIN genre AS g ON fg.film_id = ?  AND fg.genre_id = g.genre_id";
        SqlRowSet genresRows = jdbcTemplate.queryForRowSet(sqlQueryGenreSelect, filmId);
        while (genresRows.next()) {
            int genreId = genresRows.getInt("genre_id");
            genreDao.getGenreById(genreId).ifPresent(genres::add);
        }
        return genres;
    }

    @Override
    public void addByFilmsIdAndGenreIdIntoFilmGenre(Long filmId, List<Genre> genres) {
        for (Genre genre : genres) {
            String sqlQueryGenreInsert = "merge into film_genre (film_id, genre_id) values (?, ?)";
            jdbcTemplate.update(sqlQueryGenreInsert, filmId, genre.getId());
        }
    }

    @Override
    public void deleteByFilmsIdFromFilmGenre(Long filmId) {
        String sqlQueryGenreDelete = "delete from film_genre where film_id = ?";
        jdbcTemplate.update(sqlQueryGenreDelete, filmId);
    }
}
