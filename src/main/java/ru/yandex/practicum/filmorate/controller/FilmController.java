package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.Collection;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping
    public Collection<Film> findAll() {
        log.info("Запрос для вывода коллекции фильмов");
        return filmService.findAll();
    }

    @PostMapping
    public Film createFilm(@RequestBody Film film) {
        log.info("Запрос на добавление в коллекцию фильма: " + film);
        return filmService.createFilm(film);
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) {
        log.info("Запрос на обновление фильма в коллекции с id=" + film.getId() + " новые данные: " + film);
        return filmService.updateFilm(film);
    }

}