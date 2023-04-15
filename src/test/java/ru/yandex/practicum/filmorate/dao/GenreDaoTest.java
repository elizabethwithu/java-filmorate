package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.NotValidParameterException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class GenreDaoTest {
    private final GenreDao genreDao;

    @Test
    void findGenreById() {
        Genre genre = genreDao.findByGenreId(1);

        assertEquals("Комедия", genre.getName());
    }

    @Test
    void findGenreByWrongId() {
        final NotValidParameterException exception = assertThrows(
                NotValidParameterException.class, () -> genreDao.findByGenreId(-1)
        );
        assertEquals("id должен быть больше ноля.", exception.getMessage());
    }

    @Test
    void getGenres() {
        List<Genre> genres = genreDao.findAllGenres();

        assertEquals(6, genres.size());
    }
}
