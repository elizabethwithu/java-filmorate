package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class FilmDaoTest {
    private final FilmDao filmDao;
    private final UserDao userDao;
    Film testFilm;
    User testUser;

    @BeforeEach
    void init() {
        testFilm = Film.builder()
            .name("Матрица")
            .description("Научно-фантастический боевик")
            .releaseDate(LocalDate.of(1999,7,3))
            .duration(120)
            .mpa(Mpa.builder().id(1).build())
            .build();

        testUser = User.builder()
                .login("nicky")
                .name("nick")
                .email("mail@mail.ru")
                .birthday(LocalDate.of(2000,3, 20))
                .build();
    }

    @Test
    public void createFilmAndSetId() {
        int resultId = filmDao.create(testFilm);

        assertEquals(1, resultId);
    }

    @Test
    public void removeFilm() {
        int resultId = filmDao.create(testFilm);

        filmDao.remove(resultId);

        final NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> filmDao.findFilmById(resultId)
        );
        assertEquals("Фильм с запрашиваемым id отсутствует.", exception.getMessage());
    }

    @Test
    public void updateFilm() {
        int resultId = filmDao.create(testFilm);
        Film updateFilm = Film.builder()
                .id(resultId)
                .name("Двухсотлетний человек")
                .description("Научно-фантастический боевик")
                .releaseDate(LocalDate.of(1999,7,3))
                .duration(120)
                .mpa(Mpa.builder().id(1).build())
                .build();
        filmDao.update(updateFilm);

        Film newNameFilm = filmDao.findFilmById(resultId);

        assertEquals("Двухсотлетний человек", newNameFilm.getName());
    }

    @Test
    public void findAll() {
        filmDao.create(testFilm);

        Collection<Film> films = filmDao.findAll();

        assertEquals(1, films.size());
    }

    @Test
    public void findAllWithEmptyList() {
        Collection<Film> films = filmDao.findAll();

        assertTrue(films.isEmpty());
    }

    @Test
    public void findFilmById() {
        int resultId = filmDao.create(testFilm);

        Film film = filmDao.findFilmById(resultId);

        assertEquals("Матрица", film.getName());
        assertEquals("Научно-фантастический боевик", film.getDescription());
    }

    @Test
    public void findFilmByWrongId() {
        final NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> filmDao.findFilmById(5)
        );

        assertEquals("Фильм с запрашиваемым id отсутствует.", exception.getMessage());
    }

    @Test
    public void addLike() {
        int filmId = filmDao.create(testFilm);
        int userId = userDao.create(testUser);

        filmDao.addLike(filmId, userId);

        List<Film> films = filmDao.findTopFilmsByLikesCount(1);

        assertEquals(1, films.size());
        assertEquals(1, films.get(0).getId());
    }

    @Test
    public void removeLike() {
        int filmId = filmDao.create(testFilm);
        int userId = userDao.create(testUser);

        filmDao.addLike(filmId, userId);
        filmDao.removeLike(filmId, userId);

        List<Film> films = filmDao.findTopFilmsByLikesCount(1);

        assertTrue(films.isEmpty());
    }

    @Test
    public void findTopFilmsByLikesCount() {
        User testFriend = User.builder()
                .login("alex")
                .name("alexey")
                .email("yandex@yandex.ru")
                .birthday(LocalDate.of(2000,3, 20))
                .build();
        Film film = Film.builder()
                .name("Мадагаскар")
                .description("Тайная жизнь африканских животных")
                .releaseDate(LocalDate.of(2005,7,3))
                .duration(120)
                .mpa(Mpa.builder().id(1).build())
                .build();
        int friendId = userDao.create(testFriend);
        int testFilmId = filmDao.create(testFilm);
        int filmId = filmDao.create(film);
        int userId = userDao.create(testUser);

        filmDao.addLike(testFilmId, userId);
        filmDao.addLike(testFilmId, friendId);
        filmDao.addLike(filmId, userId);

        List<Film> films = filmDao.findTopFilmsByLikesCount(3);

        assertEquals(testFilmId, films.get(0).getId());
        assertEquals(filmId, films.get(1).getId());
    }

    @Test
    public void findTopFilmsWithoutLikes() {
        Film film = Film.builder()
                .name("Мадагаскар")
                .description("Тайная жизнь африканских животных")
                .releaseDate(LocalDate.of(2005,7,3))
                .duration(120)
                .mpa(Mpa.builder().id(1).build())
                .build();
        filmDao.create(film);
        filmDao.create(testFilm);

        List<Film> films = filmDao.findTopFilmsWithoutLikes(3);

        assertEquals(2, films.size());
    }

    @Test
    public void addGenreToFilm() {
        int testFilmId = filmDao.create(testFilm);
        filmDao.addGenreToFilm(testFilmId, 1);

        List<Genre> genres = filmDao.findGenreByFilmId(testFilmId);

        assertEquals("Комедия", genres.get(0).getName());
    }

    @Test
    public void removeFilmsGenres() {
        int testFilmId = filmDao.create(testFilm);
        filmDao.addGenreToFilm(testFilmId, 1);

        filmDao.removeFilmsGenres(testFilmId);

        List<Genre> genres = filmDao.findGenreByFilmId(testFilmId);

        assertEquals(0, genres.size());
    }

    @Test
    public void findGenreByFilmId() {
        int testFilmId = filmDao.create(testFilm);
        filmDao.addGenreToFilm(testFilmId, 1);

        List<Genre> genres = filmDao.findGenreByFilmId(testFilmId);

        assertEquals("Комедия", genres.get(0).getName());
    }
}