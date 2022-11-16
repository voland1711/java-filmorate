package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FilmService {
    private FilmStorage filmStorage;
    private UserStorage userStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public Collection<Film> findAllFilms() {
        return filmStorage.findAllFilms();
    }

    public Film createFilm(Film film) {
        return filmStorage.createFilm(film);
    }


    public Film getFilmById(long filmId) {
        return filmStorage.getFilmById(filmId);
    }

    public List<Film> getPopularFilms(int count) {
        return filmStorage.findAllFilms().stream()
                .sorted((o1, o2) -> o2.getLike().size() - o1.getLike().size())
                .limit(count)
                .collect(Collectors.toList());
    }

    public Film updateFilm(Film film) {
        return filmStorage.updateFilm(film);
    }

    public Film putFilmLike(long filmId, long userId) {
        filmStorage.existFilm(filmId);
        userStorage.existUser(userId);
        filmStorage.getFilmById(filmId).getLike().add(userId);
        log.info("Пользователь id=" + userId + " добавил лайк к фильму id=" + filmId);
        return filmStorage.getFilmById(filmId);
    }

    public Film deleteFilmLike(long filmId, long userId) {
        filmStorage.existFilm(filmId);
        userStorage.existUser(userId);
        filmStorage.getFilmById(filmId).getLike().remove(userId);
        log.info("Пользователь id=" + userId + " удалил лайк к фильму id=" + filmId);
        return filmStorage.getFilmById(filmId);
    }

}