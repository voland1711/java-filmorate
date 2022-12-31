package ru.yandex.practicum.filmorate.dao.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.GenreDao;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
public class GenreDaoImpl implements GenreDao {
    private final JdbcTemplate jdbcTemplate;

    public GenreDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<Genre> getGenreById(int genreId) {
        log.info("Поступил запрос на жанр фильма с id {} ", genreId);
        existGenre(genreId);
        String sqlQuerySelect = "SELECT * FROM genre WHERE genre_id = ?";
        SqlRowSet genreRows = jdbcTemplate.queryForRowSet(sqlQuerySelect, genreId);
        if (genreRows.next()) {
            return Optional.of(jdbcTemplate.queryForObject(sqlQuerySelect, this::rsToMpa, genreId));
        }
        return Optional.empty();

    }

    @Override
    public List<Genre> findAllGenre() {
        String sqlQuery = "SELECT * FROM genre";
        return jdbcTemplate.query(sqlQuery, this::rsToMpa);
    }

    @Override
    public boolean existGenre(int genreId) {
        String sqlQuery = "SELECT * from genre WHERE genre_id = ?";
        if (jdbcTemplate.queryForRowSet(sqlQuery, genreId).next()) {
            return true;
        }
        throw new ObjectNotFoundException("Жанр с id = " + genreId + " не существует");
    }

    private Genre rsToMpa(ResultSet rs, int i) throws SQLException {
        return Genre.builder()
                .id(rs.getInt("genre_id"))
                .name(rs.getString("genre_name")).build();
    }
}
