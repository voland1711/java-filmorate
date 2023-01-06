package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;
import java.util.Optional;

public interface MpaDao {
    Optional<Mpa> getMpaById(int mpaId);

    List<Mpa> findAllMpa();

    boolean existMpa(int mpaId);
}
