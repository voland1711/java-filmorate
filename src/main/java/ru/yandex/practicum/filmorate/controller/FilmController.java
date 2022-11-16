package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.Collection;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @PostMapping
    public Film createFilm(@RequestBody Film film) {
        log.info("Запрос на добавление в коллекцию фильма: " + film);
        return filmService.createFilm(film);
    }

    @GetMapping
    public Collection<Film> findAllFilms() {
        log.info("Запрос для вывода коллекции фильмов");
        return filmService.findAllFilms();
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) {
        log.info("Запрос на обновление фильма в коллекции с id=" + film.getId() + " новые данные: " + film);
        return filmService.updateFilm(film);
    }

    @GetMapping("/{filmId}")
    public Film getFilmById(@PathVariable Long filmId) {
        return filmService.getFilmById(filmId);
    }

    @PutMapping("/{filmId}/like/{userId}")
    public Film putFilmLike(@PathVariable Long filmId, @PathVariable Long userId) {
        return filmService.putFilmLike(filmId, userId);
    }

    @DeleteMapping("/{filmId}/like/{userId}")
    public Film deleteFilmLike(@PathVariable Long filmId, @PathVariable Long userId) {
        return filmService.deleteFilmLike(filmId, userId);
    }

    @GetMapping("/popular")
    public List<Film> getPopularFilms(@RequestParam(defaultValue = "10", name = "count") int count) {
        return filmService.getPopularFilms(count);
    }

}