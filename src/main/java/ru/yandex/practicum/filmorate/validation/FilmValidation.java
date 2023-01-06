package ru.yandex.practicum.filmorate.validation;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

@Slf4j
@Component
public class FilmValidation {
    private static final LocalDate DEAD_LINE_DATE = LocalDate.of(1895, 12, 28);

    public void validation(Film film) {
        if (StringUtils.isBlank(film.getName())) {
            throw new ValidationException("Название фильма не может быть пустым");
        }
        if (StringUtils.isBlank(film.getDescription())) {
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

        if (film.getDuration() <= 0) {
            throw new ValidationException("Продолжительность фильма должна быть положительной");
        }
        
        log.info("Валидация фильма " + film + " прошла успешно");
    }
}
