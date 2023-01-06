package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.FilmLikeDao;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import ru.yandex.practicum.filmorate.validation.FilmValidation;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FilmService {
    private FilmStorage filmStorage;
    private UserStorage userStorage;
    private FilmLikeDao filmLikeDao;
    private final FilmValidation filmValidation;

    public FilmService(@Qualifier("FilmDbStorage") FilmStorage filmStorage,
                       @Qualifier("UserDbStorage") UserStorage userStorage,
                       FilmLikeDao filmLikeDao, FilmValidation filmValidation) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
        this.filmLikeDao = filmLikeDao;
        this.filmValidation = filmValidation;
    }

    public List<Film> findAllFilms() {
        return filmStorage.findAllFilms();
    }

    public Film createFilm(Film film) {
        filmValidation.validation(film);
        filmStorage.existFilm(film);
        return filmStorage.createFilm(film);
    }


    public Optional<Film> getFilmById(long filmId) {
        return filmStorage.getFilmById(filmId);
    }


    public Film updateFilm(Film film) {
        filmValidation.validation(film);
        return filmStorage.updateFilm(film);
    }

    public Optional<Film> putFilmLike(long filmId, long userId) {
        filmStorage.existFilm(filmId);
        userStorage.existUser(userId);
        filmStorage.getFilmById(filmId).ifPresent(film -> filmLikeDao.addLike(film, userId));
        filmStorage.getFilmById(filmId).ifPresent(filmStorage::updateFilm);
        log.info("Пользователь id=" + userId + " добавил лайк к фильму id=" + filmId);
        return filmStorage.getFilmById(filmId);
    }

    public List<Film> getPopularFilms(int count) {
        return filmStorage.findAllFilms().stream()
                .sorted((o1, o2) -> (int) (o2.getRate() - o1.getRate()))
                .limit(count)
                .collect(Collectors.toList());
    }

    public Optional<Film> deleteFilmLike(long filmId, long userId) {
        filmStorage.existFilm(filmId);
        userStorage.existUser(userId);
        filmStorage.getFilmById(filmId).ifPresent(film -> filmLikeDao.deleteLike(film, userId));
        filmStorage.getFilmById(filmId).ifPresent(filmStorage::updateFilm);
        log.info("Пользователь id=" + userId + " удалил лайк к фильму id=" + filmId);
        return filmStorage.getFilmById(filmId);
    }

}