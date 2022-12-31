package ru.yandex.practicum.filmorate.service;

import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.MpaDao;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@Builder
public class MpaService {
    private final MpaDao mpaDao;

    public MpaService(MpaDao mpaDao) {
        this.mpaDao = mpaDao;
    }

    public List<Mpa> findAllMpa() {
        return mpaDao.findAllMpa();
    }

    public Optional<Mpa> getMpaById(int mpaId) {
        return mpaDao.getMpaById(mpaId);
    }


}
