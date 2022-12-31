package ru.yandex.practicum.filmorate.service;

import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.GenreDao;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@Builder
public class GenreService {
    private final GenreDao genreDao;

    public GenreService(GenreDao genreDao) {
        this.genreDao = genreDao;
    }

    public List<Genre> findAllGenre() {
        return genreDao.findAllGenre();
    }

    public Optional<Genre> getGenreById(int genreId) {
        return genreDao.getGenreById(genreId);
    }


}