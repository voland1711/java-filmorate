package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

public interface FilmGenreDao {
    List<Genre> findByFilmId(Long filmId);

    void addByFilmsIdAndGenreIdIntoFilmGenre(Long filmId, List<Genre> genres);

    void deleteByFilmsIdFromFilmGenre(Long filmId);

}
