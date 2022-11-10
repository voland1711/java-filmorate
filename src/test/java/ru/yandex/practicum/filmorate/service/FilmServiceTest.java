package ru.yandex.practicum.filmorate.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class FilmServiceTest {

    @Test
    @DisplayName("Добавление объекта Film: Название фильма пустое")
    void createFilmWithNameEmptyTest() {
        Film film = new Film();
        film.setName("");
        film.setDescription("Description");
        film.setReleaseDate(LocalDate.of(1967, 3, 25));
        film.setDuration(100);
        FilmService filmService = new FilmService();
        assertEquals(0, filmService.findAll().size());
        try {
            filmService.createFilm(film);
        } catch (ValidationException e) {
            System.out.println("Ошибка валидации: Попытка добавить фильм в коллекцию с пустым названием");
        }
        assertEquals(0, filmService.findAll().size(), "В коллекцию добавлен фильм без названия");
    }

    @Test
    @DisplayName("Добавление объекта Film: Описание фильма более 200 символов")
    void createFilmDescriptionIsLongTest() {
        Film film = new Film();
        film.setName("Friday");
        film.setDescription("DescriptionDescri ptionDescriptionDescriptionDesc riptionDescriptionDescriptionDescripti" +
                "onptionDescriptionDescriptionDesc ription DescriptionD escription Description ptionDescriptionDescпр" +
                "riptionDesc ription DescriptionD escription Description");
        film.setReleaseDate(LocalDate.of(1967, 3, 25));
        film.setDuration(100);
        FilmService filmService = new FilmService();
        assertEquals(0, filmService.findAll().size());
        try {
            filmService.createFilm(film);
        } catch (ValidationException e) {
            System.out.println("Ошибка валидации: Попытка добавить фильм в коллекцию с описанием более 200 символов");
        }
        assertEquals(0, filmService.findAll().size(), "В коллекцию добавлен фильм с длиноя описания" +
                " более 200 символов");
    }

    @Test
    @DisplayName("Добавление объекта Film: Продолжительность фильма не положительное число")
    void createFilmLengthIsNegativeTest() {
        Film film = new Film();
        film.setName("Name");
        film.setDescription("Description");
        film.setReleaseDate(LocalDate.of(1967, 3, 25));
        film.setDuration(0);
        FilmService filmService = new FilmService();
        assertEquals(0, filmService.findAll().size());
        try {
            filmService.createFilm(film);
        } catch (ValidationException e) {
            System.out.println("Ошибка валидации: Попытка добавить фильм с нулевой длительностью");
        }
        assertEquals(0, filmService.findAll().size(),
                "В коллекцию добавлен фильм, у которого нулевая продолжительность");
    }

    @Test
    @DisplayName("Добавление объекта Film: Дата релиза — ранее 28 декабря 1895 года")
    void createFilmReleaseDateInPastTest() {
        Film film = new Film();
        film.setName("Name");
        film.setDescription("Description");
        film.setReleaseDate(LocalDate.of(1895, 1, 1));
        film.setDuration(100);
        FilmService filmService = new FilmService();
        assertEquals(0, filmService.findAll().size());
        try {
            filmService.createFilm(film);
        } catch (ValidationException e) {
            System.out.println("Ошибка валидации: Дата релиза — раньше 28 декабря 1895 года");
        }
        assertEquals(0, filmService.findAll().size(),
                "Добавлен фильм с датой релиза — ранее 28 декабря 1895 года");
    }

    @Test
    @DisplayName("Добавление объекта Film: Данные корректны")
    void createFilmTest() {
        Film film = new Film();
        film.setName("Name");
        film.setDescription("Description");
        film.setReleaseDate(LocalDate.of(1895, 12, 28));
        film.setDuration(100);
        FilmService filmService = new FilmService();
        assertEquals(0, filmService.findAll().size());
        try {
            filmService.createFilm(film);
        } catch (ValidationException e) {
            System.out.println("Ошибка при добавлении фильма с валидными данными");
        }
        assertEquals(1, filmService.findAll().size(),
                "Фильм с валидными даннными не добавлен в коллекцию");
    }

    @Test
    @DisplayName("Добавление объекта Film: название фильма имеет значение null")
    void createFilmNameIsNullTest() {
        Film film = new Film();
        film.setName(null);
        film.setDescription("Description");
        film.setReleaseDate(LocalDate.of(1895, 12, 28));
        film.setDuration(100);
        FilmService filmService = new FilmService();
        assertEquals(0, filmService.findAll().size());
        try {
            filmService.createFilm(film);
        } catch (ValidationException e) {
            System.out.println("Ошибка при валидации: название фильма не может быть null");
        }
        assertEquals(0, filmService.findAll().size(),
                "Фильм, не прошедший валидацию, добавлен в коллекцию");
    }

    @Test
    @DisplayName("Добавление объекта Film: Описание фильма имеет значение null")
    void createFilmDescriptionIsNullTest() {
        Film film = new Film();
        film.setName("Name");
        film.setDescription(null);
        film.setReleaseDate(LocalDate.of(1895, 12, 28));
        film.setDuration(100);
        FilmService filmService = new FilmService();
        assertEquals(0, filmService.findAll().size());
        try {
            filmService.createFilm(film);
        } catch (ValidationException e) {
            System.out.println("Ошибка при валидации: Описание фильма не может быть null");
        }
        assertEquals(0, filmService.findAll().size(),
                "Фильм, не прошедший валидацию, добавлен в коллекцию");
    }

    @Test
    @DisplayName("Добавление объекта Film: Продолжительность фильма не заполнена")
    void createFilmDurationIsNullTest() {
        Film film = new Film();
        film.setName("Name");
        film.setDescription("Description");
        film.setReleaseDate(LocalDate.of(1895, 12, 28));
        FilmService filmService = new FilmService();
        assertEquals(0, filmService.findAll().size());
        try {
            filmService.createFilm(film);
        } catch (ValidationException e) {
            System.out.println("Ошибка при валидации: Продолжительность фильма не сооответствует требования");
        }
        assertEquals(0, filmService.findAll().size(),
                "Фильм, не прошедший валидацию, добавлен в коллекцию");
    }

    @Test
    @DisplayName("Добавление объекта Film: Дата релиза имеет значение null")
    void createFilmReleaseDateIsNullTest() {
        Film film = new Film();
        film.setName("Name");
        film.setDescription("Description");
        film.setReleaseDate(null);
        film.setDuration(100);
        FilmService filmService = new FilmService();
        assertEquals(0, filmService.findAll().size());
        try {
            filmService.createFilm(film);
        } catch (ValidationException e) {
            System.out.println("Ошибка при валидации: Дата релиза фильма не может быть null");
        }
        assertEquals(0, filmService.findAll().size(),
                "Фильм, не прошедший валидацию, добавлен в коллекцию");
    }

    @Test
    @DisplayName("Проверка обновления объекта Film в коллекции")
    void updateFilmTest() {
        Film film = new Film();
        film.setName("Name");
        film.setDescription("Description");
        film.setReleaseDate(LocalDate.of(1895, 12, 28));
        film.setDuration(100);
        FilmService filmService = new FilmService();
        assertEquals(0, filmService.findAll().size());
        try {
            filmService.createFilm(film);
        } catch (ValidationException e) {
            System.out.println("Ошибка при добавлении фильма с валидными данными");
        }
        assertEquals(1, filmService.findAll().size(),
                "Фильм с валидными данными не добавлен в коллекцию");

        // Обновляем поля объекта Film
        Film filmUpdate = new Film();
        filmUpdate.setId(1);
        filmUpdate.setName("New-Name");
        filmUpdate.setDescription("New-Description");
        filmUpdate.setReleaseDate(LocalDate.of(2016, 1, 21));
        filmUpdate.setDuration(108);
        filmService.updateFilm(filmUpdate);
        assertEquals(1, filmService.findAll().size(),
                "В коллекции должен быть только один фильм");
        assertTrue(filmService.findAll().contains(filmUpdate), "Фильм не обновлен");

    }

    @Test
    @DisplayName("Проверка обновления неизвестного фильма")
    void updateUnknownFilmTest() {
        Film film = new Film();
        film.setName("Name");
        film.setDescription("Description");
        film.setReleaseDate(LocalDate.of(1895, 12, 28));
        film.setDuration(100);
        FilmService filmService = new FilmService();
        assertEquals(0, filmService.findAll().size());
        try {
            filmService.createFilm(film);
        } catch (ValidationException e) {
            System.out.println("Ошибка при добавлении фильма с валидными данными");
        }
        assertEquals(1, filmService.findAll().size(),
                "Фильм с валидными данными не добавлен в коллекцию");

        // Обновляем поля объекта Film
        Film filmUpdate = new Film();
        filmUpdate.setId(999);
        filmUpdate.setName("New-Name");
        filmUpdate.setDescription("New-Description");
        filmUpdate.setReleaseDate(LocalDate.of(2016, 1, 21));
        filmUpdate.setDuration(108);
        try {
            filmService.updateFilm(filmUpdate);
        } catch (ValidationException e) {
            System.out.println("Необходимый фильм для обновления отсутствует в коллекции");
        }
        assertEquals(1, filmService.findAll().size(),
                "В коллекции должен быть только один фильм");
        assertTrue(filmService.findAll().contains(film), "Фильм ошибочно обновлен");
    }

    @Test
    @DisplayName("Проверка обновления фильма c пустым названием")
    void updateFilmWithNameEmptyTest() {
        Film film = new Film();
        film.setName("Name");
        film.setDescription("Description");
        film.setReleaseDate(LocalDate.of(1895, 12, 28));
        film.setDuration(100);
        FilmService filmService = new FilmService();
        assertEquals(0, filmService.findAll().size());
        try {
            filmService.createFilm(film);
        } catch (ValidationException e) {
            System.out.println("Ошибка при добавлении фильма с валидными данными");
        }
        assertEquals(1, filmService.findAll().size(),
                "Фильм с валидными данными не добавлен в коллекцию");

        // Обновляем поля объекта Film
        Film filmUpdate = new Film();
        filmUpdate.setId(1);
        filmUpdate.setName("");
        filmUpdate.setDescription("New-Description");
        filmUpdate.setReleaseDate(LocalDate.of(2016, 1, 21));
        filmUpdate.setDuration(108);
        try {
            filmService.updateFilm(filmUpdate);
        } catch (ValidationException e) {
            System.out.println("Попытка обновления фильма с пустым Названием");
        }
        assertEquals(1, filmService.findAll().size(),
                "В коллекции должен быть только один фильм");
        assertTrue(filmService.findAll().contains(film), "Фильм ошибочно обновлен");
    }

    @Test
    @DisplayName("Проверка обновления фильма с описанием более 200 символов")
    void updateFilmDescriptionIsLongTest() {
        Film film = new Film();
        film.setName("Name");
        film.setDescription("Description");
        film.setReleaseDate(LocalDate.of(1895, 12, 28));
        film.setDuration(100);
        FilmService filmService = new FilmService();
        assertEquals(0, filmService.findAll().size());
        try {
            filmService.createFilm(film);
        } catch (ValidationException e) {
            System.out.println("Ошибка при добавлении фильма с валидными данными");
        }
        assertEquals(1, filmService.findAll().size(),
                "Фильм с валидными данными не добавлен в коллекцию");

        // Обновляем поля объекта Film
        Film filmUpdate = new Film();
        filmUpdate.setId(1);
        filmUpdate.setName("New-Name");
        filmUpdate.setDescription("DescriptionDescri ptionDescriptionDescriptionDesc riptionDescriptionDescriptionDescripti" +
                "onptionDescriptionDescriptionDesc ription DescriptionD escription Description ptionDescriptionDescпр" +
                "riptionDesc ription DescriptionD escription Description");
        filmUpdate.setReleaseDate(LocalDate.of(2016, 1, 21));
        filmUpdate.setDuration(108);
        try {
            filmService.updateFilm(filmUpdate);
        } catch (ValidationException e) {
            System.out.println("Попытка обновления фильма с описанием более 200 символов");
        }
        assertEquals(1, filmService.findAll().size(),
                "В коллекции должен быть только один фильм");
        assertTrue(filmService.findAll().contains(film), "Фильм ошибочно обновлен");
    }

    @Test
    @DisplayName("Проверка обновления фильма с отрицательной продолжительностю")
    void updateFilmLengthIsNegativeTest() {
        Film film = new Film();
        film.setName("Name");
        film.setDescription("Description");
        film.setReleaseDate(LocalDate.of(1895, 12, 28));
        film.setDuration(100);
        FilmService filmService = new FilmService();
        assertEquals(0, filmService.findAll().size());
        try {
            filmService.createFilm(film);
        } catch (ValidationException e) {
            System.out.println("Ошибка при добавлении фильма с валидными данными");
        }
        assertEquals(1, filmService.findAll().size(),
                "Фильм с валидными данными не добавлен в коллекцию");

        // Обновляем поля объекта Film
        Film filmUpdate = new Film();
        filmUpdate.setId(1);
        filmUpdate.setName("New Name");
        filmUpdate.setDescription("New Description");
        filmUpdate.setReleaseDate(LocalDate.of(2016, 1, 21));
        filmUpdate.setDuration(-108);
        try {
            filmService.updateFilm(filmUpdate);
        } catch (ValidationException e) {
            System.out.println("Попытка обновления фильма с отрицательной продолжительностью");
        }
        assertEquals(1, filmService.findAll().size(),
                "В коллекции должен быть только один фильм");
        assertTrue(filmService.findAll().contains(film), "Фильм ошибочно обновлен");
    }

    @Test
    @DisplayName("Проверка обновления объекта Film: Дата релиза — ранее 28 декабря 1895 года")
    void updateFilmReleaseDateInPastTest() {
        Film film = new Film();
        film.setName("Name");
        film.setDescription("Description");
        film.setReleaseDate(LocalDate.of(1895, 12, 28));
        film.setDuration(100);
        FilmService filmService = new FilmService();
        assertEquals(0, filmService.findAll().size());
        try {
            filmService.createFilm(film);
        } catch (ValidationException e) {
            System.out.println("Ошибка при добавлении фильма с валидными данными");
        }
        assertEquals(1, filmService.findAll().size(),
                "Фильм с валидными данными не добавлен в коллекцию");

        // Обновляем поля объекта Film
        Film filmUpdate = new Film();
        filmUpdate.setId(1);
        filmUpdate.setName("New Name");
        filmUpdate.setDescription("New-Description");
        filmUpdate.setReleaseDate(LocalDate.of(1885, 1, 21));
        filmUpdate.setDuration(108);
        try {
            filmService.updateFilm(filmUpdate);
        } catch (ValidationException e) {
            System.out.println("Ошибка валидации: Дата релиза — раньше 28 декабря 1895 года");
        }
        assertEquals(1, filmService.findAll().size(),
                "В коллекции должен быть только один фильм");
        assertTrue(filmService.findAll().contains(film), "Фильм ошибочно обновлен");
    }

}