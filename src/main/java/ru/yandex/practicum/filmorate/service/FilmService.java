package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.*;

@Service
@Slf4j
public class FilmService {
    private int filmId = 0;
    private static final LocalDate DEAD_LINE_DATE = LocalDate.of(1895, 12, 28);
    private final Map<Integer, Film> films = new HashMap<>();

    public Collection<Film> findAll() {
        return films.values();
    }

    private int getFilmId() {
        filmId++;
        return filmId;
    }

    private void filmValidation(Film film) {
        if (StringUtils.isBlank(film.getName())) {
            throw new ValidationException("Название фильма не может быть пустым");
        }
        if (StringUtils.isBlank(film.getDescription()) && StringUtils.isEmpty(film.getDescription())) {
            throw new ValidationException("Описание фильма не может быть пустым, иметь значение null");
        }
        if (film.getDescription().length() > 200) {
            throw new ValidationException("Превышена максимальная длина описания — 200 символов");
        }
        if (film.getReleaseDate() == null) {
            throw new ValidationException("Дата релиза — не может быть null");
        }

        if (film.getReleaseDate().isBefore(DEAD_LINE_DATE)) {
            throw new ValidationException("Дата релиза — раньше 28 декабря 1895 года");
        }

        System.out.println("film.getDuration() = " + film.getDuration());
        if (film.getDuration() <= 0) {
            throw new ValidationException("Продолжительность фильма должна быть положительной");
        }
    }

    public Film createFilm(Film film) {
        filmValidation(film);
        film.setId(getFilmId());
        films.put(film.getId(), film);
        log.info("Фильм: " + film + " добавлен в коллекцию");
        return film;
    }

    public Film updateFilm(Film film) {
        filmValidation(film);
        if (!films.containsKey(film.getId())) {
            throw new ValidationException("Фильм: " + film.getName() + " - отсуствует в коллекции");
        }
        log.info("Данные  фильма  для обновления коллекции: " + films.get(film.getId()));
        films.put(film.getId(), film);
        log.info("Данные фильма после обновления коллекции: " + films.get(film.getId()));
        return film;
    }

}
