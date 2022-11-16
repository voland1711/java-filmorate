package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {
    private Long filmId = 0L;
    private static final LocalDate DEAD_LINE_DATE = LocalDate.of(1895, 12, 28);
    private final Map<Long, Film> films = new HashMap<>();

    private Long getFilmId() {
        return ++filmId;
    }

    @Override
    public Collection<Film> findAllFilms() {
        return films.values();
    }

    public Film createFilm(Film film) {
        filmValidation(film);
        film.setId(getFilmId());
        films.put(film.getId(), film);
        log.info("Фильм: " + film + " добавлен в коллекцию");
        return film;
    }

    public Film updateFilm(Film film) {
        existFilm(film.getId());
        filmValidation(film);
        log.info("Данные  фильма  для обновления коллекции: " + films.get(film.getId()));
        films.put(film.getId(), film);
        log.info("Данные фильма после обновления коллекции: " + films.get(film.getId()));
        return film;
    }

    @Override
    public Film getFilmById(long filmId) {
        existFilm(filmId);
        log.info("Фильм с id=" + filmId + " отправлен пользователю");
        return films.get(filmId);
    }

    @Override
    public boolean existFilm(long filmId) {
        log.info("Поступил запрос на проверку существавания фильма с id=" + filmId);
        if (!films.containsKey(filmId)) {
            log.error("Фильм существует");
            throw new ObjectNotFoundException("Фильм в коллекции не найден");
        }
        return true;
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
        log.info("Валидация фильма " + film + " прошла успешно");
    }
}
