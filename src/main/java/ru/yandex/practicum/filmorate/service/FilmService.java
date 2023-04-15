package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.MpaDao;
import ru.yandex.practicum.filmorate.dao.UserDao;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.dao.FilmDao;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class FilmService {
    private final FilmDao filmDao;
    private final UserDao userDao;
    private final MpaDao mpaDao;

    public Film createFilm(Film film) {
        int filmId = filmDao.create(film);
        film.setId(filmId);
        film.setMpa(mpaDao.findMpaByFilmId(filmId));
        List<Genre> genres = film.getGenres();

        setGenresForFilm(genres, film);
        log.debug("Фильм {} успешно добавлен.", film);
        return film;
    }

    public void removeFilmById(Integer id) {
        Film film = filmDao.findFilmById(id);
        filmDao.remove(id);
        filmDao.removeFilmsGenres(id);
        log.debug("Фильм {} успешно удален.", film);
    }

    public Film updateFilm(Film film) {
        filmDao.findFilmById(film.getId());
        filmDao.update(film);
        film.setMpa(mpaDao.findMpaByFilmId(film.getId()));
        List<Genre> genres = film.getGenres();

        setGenresForFilm(genres, film);
        log.debug("Фильм {} успешно обновлен.", film);
        return film;
    }

    public Collection<Film> findAllFilms() {
        Collection<Film> films = filmDao.findAll();
        for (Film film : films) {
            film.setMpa(mpaDao.findMpaByFilmId(film.getId()));
            film.setGenres(filmDao.findGenreByFilmId(film.getId()));
        }
        log.debug("Текущее количество фильмов: {}", films.size());
        return films;
    }

    public Film findFilmById(Integer id) {
        Film film = filmDao.findFilmById(id);
        film.setMpa(mpaDao.findMpaByFilmId(id));
        film.setGenres(filmDao.findGenreByFilmId(id));

        log.debug("Получен фильм {}.", film);
        return film;
    }

    public void addLike(Integer filmId, Integer userId) {
        Film film = filmDao.findFilmById(filmId);
        User user = userDao.findUserById(userId);

        filmDao.addLike(filmId, userId);
        log.debug("Пользователь c {} поставил лайк фильму {}.", user, film);
    }

    public void removeLike(Integer filmId, Integer userId) {
        User user = userDao.findUserById(userId);
        Film film = filmDao.findFilmById(filmId);
        filmDao.removeLike(filmId, userId);
        log.debug("Пользователь {} удалил лайк фильму с {}.", user, film);
    }

    public List<Film> findTopFilmByLikes(Integer count) {
        List<Film> topFilms = filmDao.findTopFilmsByLikesCount(count);
        if (topFilms.isEmpty()) {
            topFilms = filmDao.findTopFilmsWithoutLikes(count);
        }
        for (Film film : topFilms) {
            film.setMpa(mpaDao.findMpaByFilmId(film.getId()));
            film.setGenres(filmDao.findGenreByFilmId(film.getId()));
        }
        log.debug("Топ {} фильмов успешно отобран.", count);

        return topFilms;
    }

    private static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Set<Object> seen = ConcurrentHashMap.newKeySet();
        return t -> seen.add(keyExtractor.apply(t));
    }

    private void setGenresForFilm(List<Genre> genres, Film film) {
        if (genres != null) {
            genres = genres.stream().filter(distinctByKey(Genre::getId)).collect(Collectors.toList());

            filmDao.removeFilmsGenres(film.getId());
            for (Genre genre : genres) {
                filmDao.addGenreToFilm(film.getId(), genre.getId());
            }
            film.setGenres(filmDao.findGenreByFilmId(film.getId()));
        }
    }
}
