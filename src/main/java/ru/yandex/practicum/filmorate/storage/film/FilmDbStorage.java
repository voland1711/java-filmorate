package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.FilmGenreDao;
import ru.yandex.practicum.filmorate.dao.FilmLikeDao;
import ru.yandex.practicum.filmorate.dao.GenreDao;
import ru.yandex.practicum.filmorate.dao.MpaDao;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

@Slf4j
@Component
@Qualifier("FilmDbStorage")
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;
    private final MpaDao mpaDao;
    private final GenreDao genreDao;
    private final FilmGenreDao filmGenreDao;
    private final FilmLikeDao filmLikeDao;

    public FilmDbStorage(JdbcTemplate jdbcTemplate,
                         MpaDao mpaDao,
                         GenreDao genreDao,
                         FilmGenreDao filmGenreDao,
                         FilmLikeDao filmLikeDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.mpaDao = mpaDao;
        this.genreDao = genreDao;
        this.filmGenreDao = filmGenreDao;
        this.filmLikeDao = filmLikeDao;
    }

    @Override
    public List<Film> findAllFilms() {
        String sqlQuery = "SELECT * FROM films";
        return jdbcTemplate.query(sqlQuery, this::rsToFilm);
    }

    @Override
    public Film createFilm(Film film) {
        // если объект передан, но поля заполнены null;
        if (film.getMpa().isPresent() && !film.getMpa().get().equals(new Mpa(null, null))) {
            film.setMpa(mpaDao.getMpaById(film.getMpa().get().getId()));
        }

        String sql = "insert into films(name_films, description, release_date, duration, film_mpa_id) " +
                "values (?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, film.getName());
            ps.setString(2, film.getDescription());
            ps.setObject(3, film.getReleaseDate());
            ps.setInt(4, film.getDuration());
            ps.setObject(5, film.getMpa().isEmpty() ? null : film.getMpa().get().getId());
            return ps;
        }, keyHolder);
        long filmId = Objects.requireNonNull(keyHolder.getKey()).intValue();
        film.setId(filmId);
        if (film.getGenres().isPresent()) {
            filmGenreDao.addByFilmsIdAndGenreIdIntoFilmGenre(filmId, film.getGenres().get());
        }
        return getFilmById(filmId)
                .orElseThrow(() -> new ObjectNotFoundException("Ошибка при добавлении фильма в коллекцию"));
    }

    @Override
    public Film updateFilm(Film film) {
        existFilm(film.getId());
        if (film.getMpa().isPresent()) {
            film.setMpa(mpaDao.getMpaById(film.getMpa().get().getId()));
        }
        Set<Genre> tempGenres = new HashSet<>();
        filmGenreDao.deleteByFilmsIdFromFilmGenre(film.getId());

        if (film.getGenres().isPresent()) {
            for (Genre genre : film.getGenres().get()) {
                tempGenres.add(genreDao.getGenreById(genre.getId()).get());
            }
        }
        film.setGenres(Optional.of(new ArrayList<>(tempGenres)));
        filmGenreDao.deleteByFilmsIdFromFilmGenre(film.getId());
        filmGenreDao.addByFilmsIdAndGenreIdIntoFilmGenre(film.getId(), new ArrayList<>(tempGenres));
        film.setRate(findLikeFilm(film.getId()));
        String sql = "update films set name_films = ?, description = ?, release_date = ?, duration = ?, rate = ?, " +
                "film_mpa_id = ? where film_id = ?";
        jdbcTemplate.update(sql, film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration(),
                film.getRate(), film.getMpa().get().getId(), film.getId());

        return film;
    }

    @Override
    public Optional<Film> getFilmById(long filmId) {
        log.info("Поступил запрос на получение рейтинга для фильма с id {} ", filmId);
        existFilm(filmId);
        String sqlQuerySelect = "SELECT * FROM films WHERE film_id = ?";
        return Optional.of(jdbcTemplate.queryForObject(sqlQuerySelect, this::rsToFilm, filmId));
    }

    @Override
    public boolean existFilm(long filmId) {
        String sqlQuerySelect = "SELECT * from films WHERE film_id = ?";
        if (jdbcTemplate.queryForRowSet(sqlQuerySelect, filmId).next()) {
            return true;
        }
        throw new ObjectNotFoundException("Фильм с идентификатором = " + filmId + " не существует");
    }

    @Override
    public boolean existFilm(Film film) {
        String sqlQuerySelect = "SELECT * from films WHERE name_films = ?";
        if (jdbcTemplate.queryForRowSet(sqlQuerySelect, film.getName()).next()) {
            throw new ObjectNotFoundException("Фильм " + film.getName() + " уже добавлен в коллекцию");
        }
        return false;
    }

    private Film rsToFilm(ResultSet rs, int i) throws SQLException {
        List<Genre> tempListGenre = filmGenreDao.findByFilmId(rs.getLong("film_id"));
        long filmId = rs.getLong("film_id");
        long rate = findLikeFilm(filmId);

        return Film.builder().id(filmId)
                .name(rs.getString("name_films"))
                .description(rs.getString("description"))
                .releaseDate(rs.getDate("release_date").toLocalDate())
                .duration(rs.getInt("duration"))
                .rate(rate)
                .mpa(mpaDao.getMpaById(rs.getInt("film_mpa_id")))
                .genres(Optional.ofNullable(tempListGenre))
                .build();
    }

    private long findLikeFilm(long filmId) {
        return filmLikeDao.findLikeFilm(filmId);
    }
}
