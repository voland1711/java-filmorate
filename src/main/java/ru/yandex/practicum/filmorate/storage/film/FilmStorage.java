package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface FilmStorage {
    Collection<Film> findAllFilms();

    Film createFilm(Film film);

    Film updateFilm(Film film);

    Film getFilmById(long filmId);

    boolean existFilm(long filmId);
}
