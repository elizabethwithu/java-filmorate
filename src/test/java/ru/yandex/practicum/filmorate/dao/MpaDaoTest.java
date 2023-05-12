package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.NotValidParameterException;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class MpaDaoTest {
    private final MpaDao mpaDao;

    @Test
    void findMpaById() {
        Mpa rating = mpaDao.findMpaById(1);

        assertEquals("G", rating.getName());
    }

    @Test
    void findMpaByWrongId() {
        final NotValidParameterException exception = assertThrows(
                NotValidParameterException.class, () -> mpaDao.findMpaById(-1)
        );
        assertEquals("id должен быть больше ноля.", exception.getMessage());
    }

    @Test
    void findAll() {
        List<Mpa> ratings = mpaDao.findAllMpa();

        assertEquals(5, ratings.size());
    }
}
