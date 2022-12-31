package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Film;


public interface FilmLikeDao {

    void addLike(Film film, long userId);

    void deleteLike(Film film, long userId);

    long findLikeFilm(long filmId);

}
