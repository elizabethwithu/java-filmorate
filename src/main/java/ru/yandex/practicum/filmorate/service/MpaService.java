package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.MpaDao;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MpaService {
    private final MpaDao mpaDao;

    public Mpa findMpaById(Integer id) {
        return mpaDao.findMpaById(id);
    }

    public List<Mpa> findAllMpa() {
        return mpaDao.findAllMpa();
    }
}
