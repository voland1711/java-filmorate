package ru.yandex.practicum.filmorate.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

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
        FilmService filmService = new FilmService(new InMemoryFilmStorage(), new InMemoryUserStorage());
        assertEquals(0, filmService.findAllFilms().size());
        Assertions.assertThrows(ValidationException.class, () -> filmService.createFilm(film));
        assertEquals(0, filmService.findAllFilms().size(), "В коллекцию добавлен фильм без названия");
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
        FilmService filmService = new FilmService(new InMemoryFilmStorage(), new InMemoryUserStorage());
        assertEquals(0, filmService.findAllFilms().size());
        Assertions.assertThrows(ValidationException.class, () -> filmService.createFilm(film));
        assertEquals(0, filmService.findAllFilms().size(), "В коллекцию добавлен фильм с длиноя описания" +
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
        FilmService filmService = new FilmService(new InMemoryFilmStorage(), new InMemoryUserStorage());
        assertEquals(0, filmService.findAllFilms().size());
        Assertions.assertThrows(ValidationException.class, () -> filmService.createFilm(film));
        assertEquals(0, filmService.findAllFilms().size(),
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
        FilmService filmService = new FilmService(new InMemoryFilmStorage(), new InMemoryUserStorage());
        assertEquals(0, filmService.findAllFilms().size());
        Assertions.assertThrows(ValidationException.class, () -> filmService.createFilm(film));
        assertEquals(0, filmService.findAllFilms().size(),
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
        FilmService filmService = new FilmService(new InMemoryFilmStorage(), new InMemoryUserStorage());
        assertEquals(0, filmService.findAllFilms().size());
        filmService.createFilm(film);
        assertEquals(1, filmService.findAllFilms().size(),
                "Фильм с валидными даннными не добавлен в коллекцию");
    }

    @Test
    @DisplayName("Добавление объекта Film: Название фильма имеет значение null")
    void createFilmNameIsNullTest() {
        Film film = new Film();
        film.setName(null);
        film.setDescription("Description");
        film.setReleaseDate(LocalDate.of(1895, 12, 28));
        film.setDuration(100);
        FilmService filmService = new FilmService(new InMemoryFilmStorage(), new InMemoryUserStorage());
        assertEquals(0, filmService.findAllFilms().size());
        Assertions.assertThrows(ValidationException.class, () -> filmService.createFilm(film));
        assertEquals(0, filmService.findAllFilms().size(),
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
        FilmService filmService = new FilmService(new InMemoryFilmStorage(), new InMemoryUserStorage());
        assertEquals(0, filmService.findAllFilms().size());
        Assertions.assertThrows(ValidationException.class, () -> filmService.createFilm(film));
        assertEquals(0, filmService.findAllFilms().size(),
                "Фильм, не прошедший валидацию, добавлен в коллекцию");
    }

    @Test
    @DisplayName("Добавление объекта Film: Продолжительность фильма не заполнена")
    void createFilmDurationIsNullTest() {
        Film film = new Film();
        film.setName("Name");
        film.setDescription("Description");
        film.setReleaseDate(LocalDate.of(1895, 12, 28));
        FilmService filmService = new FilmService(new InMemoryFilmStorage(), new InMemoryUserStorage());
        assertEquals(0, filmService.findAllFilms().size());
        Assertions.assertThrows(ValidationException.class, () -> filmService.createFilm(film));
        assertEquals(0, filmService.findAllFilms().size(),
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
        FilmService filmService = new FilmService(new InMemoryFilmStorage(), new InMemoryUserStorage());
        assertEquals(0, filmService.findAllFilms().size());
        Assertions.assertThrows(ValidationException.class, () -> filmService.createFilm(film));
        assertEquals(0, filmService.findAllFilms().size(),
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
        FilmService filmService = new FilmService(new InMemoryFilmStorage(), new InMemoryUserStorage());
        assertEquals(0, filmService.findAllFilms().size());
        filmService.createFilm(film);
        assertEquals(1, filmService.findAllFilms().size(),
                "Фильм с валидными данными не добавлен в коллекцию");

        // Обновляем поля объекта Film
        Film filmUpdate = new Film();
        filmUpdate.setId(1L);
        filmUpdate.setName("New-Name");
        filmUpdate.setDescription("New-Description");
        filmUpdate.setReleaseDate(LocalDate.of(2016, 1, 21));
        filmUpdate.setDuration(108);
        filmService.updateFilm(filmUpdate);
        assertEquals(1, filmService.findAllFilms().size(), "В коллекции должен быть только один фильм");
        assertTrue(filmService.findAllFilms().contains(filmUpdate), "Фильм не обновлен");

    }

    @Test
    @DisplayName("Проверка обновления неизвестного фильма")
    void updateUnknownFilmTest() {
        Film film = new Film();
        film.setName("Name");
        film.setDescription("Description");
        film.setReleaseDate(LocalDate.of(1895, 12, 28));
        film.setDuration(100);
        FilmService filmService = new FilmService(new InMemoryFilmStorage(), new InMemoryUserStorage());
        assertEquals(0, filmService.findAllFilms().size());
        filmService.createFilm(film);
        assertEquals(1, filmService.findAllFilms().size(),
                "Фильм с валидными данными не добавлен в коллекцию");

        // Обновляем поля объекта Film
        Film filmUpdate = new Film();
        filmUpdate.setId(999L);
        filmUpdate.setName("New-Name");
        filmUpdate.setDescription("New-Description");
        filmUpdate.setReleaseDate(LocalDate.of(2016, 1, 21));
        filmUpdate.setDuration(108);
        Assertions.assertThrows(ObjectNotFoundException.class, () -> filmService.updateFilm(filmUpdate));
        assertEquals(1, filmService.findAllFilms().size(), "В коллекции должен быть только один фильм");
        assertTrue(filmService.findAllFilms().contains(film), "Фильм ошибочно обновлен");
    }

    @Test
    @DisplayName("Проверка обновления фильма c пустым названием")
    void updateFilmWithNameEmptyTest() {
        Film film = new Film();
        film.setName("Name");
        film.setDescription("Description");
        film.setReleaseDate(LocalDate.of(1895, 12, 28));
        film.setDuration(100);
        FilmService filmService = new FilmService(new InMemoryFilmStorage(), new InMemoryUserStorage());
        assertEquals(0, filmService.findAllFilms().size());
        filmService.createFilm(film);
        assertEquals(1, filmService.findAllFilms().size(),
                "Фильм с валидными данными не добавлен в коллекцию");

        // Обновляем поля объекта Film
        Film filmUpdate = new Film();
        filmUpdate.setId(1L);
        filmUpdate.setName("");
        filmUpdate.setDescription("New-Description");
        filmUpdate.setReleaseDate(LocalDate.of(2016, 1, 21));
        filmUpdate.setDuration(108);
        Assertions.assertThrows(ValidationException.class, () -> filmService.updateFilm(filmUpdate));
        assertEquals(1, filmService.findAllFilms().size(), "В коллекции должен быть только один фильм");
        assertTrue(filmService.findAllFilms().contains(film), "Фильм ошибочно обновлен");
    }

    @Test
    @DisplayName("Проверка обновления фильма с описанием более 200 символов")
    void updateFilmDescriptionIsLongTest() {
        Film film = new Film();
        film.setName("Name");
        film.setDescription("Description");
        film.setReleaseDate(LocalDate.of(1895, 12, 28));
        film.setDuration(100);
        FilmService filmService = new FilmService(new InMemoryFilmStorage(), new InMemoryUserStorage());
        assertEquals(0, filmService.findAllFilms().size());
        filmService.createFilm(film);
        assertEquals(1, filmService.findAllFilms().size(),
                "Фильм с валидными данными не добавлен в коллекцию");

        // Обновляем поля объекта Film
        Film filmUpdate = new Film();
        filmUpdate.setId(1L);
        filmUpdate.setName("New-Name");
        filmUpdate.setDescription("DescriptionDescri ptionDescriptionDescriptionDesc riptionDescriptionDescription" +
                "Descript ionptionDescriptionDescriptionDesc ription DescriptionD escription Description ptionDescr" +
                "iptionDescпр" + "riptionDesc ription DescriptionD escription Description");
        filmUpdate.setReleaseDate(LocalDate.of(2016, 1, 21));
        filmUpdate.setDuration(108);
        Assertions.assertThrows(ValidationException.class, () -> filmService.updateFilm(filmUpdate));
        assertEquals(1, filmService.findAllFilms().size(), "В коллекции должен быть только один фильм");
        assertTrue(filmService.findAllFilms().contains(film), "Фильм ошибочно обновлен");
    }

    @Test
    @DisplayName("Проверка обновления фильма с отрицательной продолжительностю")
    void updateFilmLengthIsNegativeTest() {
        Film film = new Film();
        film.setName("Name");
        film.setDescription("Description");
        film.setReleaseDate(LocalDate.of(1895, 12, 28));
        film.setDuration(100);
        FilmService filmService = new FilmService(new InMemoryFilmStorage(), new InMemoryUserStorage());
        assertEquals(0, filmService.findAllFilms().size());
        filmService.createFilm(film);
        assertEquals(1, filmService.findAllFilms().size(),
                "Фильм с валидными данными не добавлен в коллекцию");

        // Обновляем поля объекта Film
        Film filmUpdate = new Film();
        filmUpdate.setId(1L);
        filmUpdate.setName("New Name");
        filmUpdate.setDescription("New Description");
        filmUpdate.setReleaseDate(LocalDate.of(2016, 1, 21));
        filmUpdate.setDuration(-108);
        Assertions.assertThrows(ValidationException.class, () -> filmService.updateFilm(filmUpdate));
        assertEquals(1, filmService.findAllFilms().size(), "В коллекции должен быть только один фильм");
        assertTrue(filmService.findAllFilms().contains(film), "Фильм ошибочно обновлен");
    }

    @Test
    @DisplayName("Проверка обновления объекта Film: Дата релиза — ранее 28 декабря 1895 года")
    void updateFilmReleaseDateInPastTest() {
        Film film = new Film();
        film.setName("Name");
        film.setDescription("Description");
        film.setReleaseDate(LocalDate.of(1895, 12, 28));
        film.setDuration(100);
        FilmService filmService = new FilmService(new InMemoryFilmStorage(), new InMemoryUserStorage());
        assertEquals(0, filmService.findAllFilms().size());
        filmService.createFilm(film);
        assertEquals(1, filmService.findAllFilms().size(),
                "Фильм с валидными данными не добавлен в коллекцию");

        // Обновляем поля объекта Film
        Film filmUpdate = new Film();
        filmUpdate.setId(1L);
        filmUpdate.setName("New Name");
        filmUpdate.setDescription("New-Description");
        filmUpdate.setReleaseDate(LocalDate.of(1885, 1, 21));
        filmUpdate.setDuration(108);
        Assertions.assertThrows(ValidationException.class, () -> filmService.updateFilm(filmUpdate));
        assertEquals(1, filmService.findAllFilms().size(), "В коллекции должен быть только один фильм");
        assertTrue(filmService.findAllFilms().contains(film), "Фильм ошибочно обновлен");
    }

}