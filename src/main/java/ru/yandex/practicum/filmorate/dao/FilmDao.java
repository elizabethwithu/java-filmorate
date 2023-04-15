package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;
import java.util.List;

public interface FilmDao {
    int create(Film film);

    void remove(Integer id);

    void update(Film film);

    Collection<Film> findAll();

    Film findFilmById(Integer id);

    void addLike(int filmId, int userId);

    void removeLike(int filmId, int userId);

    List<Film> findTopFilmsByLikesCount(int count);

    List<Film> findTopFilmsWithoutLikes(int count);

    void addGenreToFilm(int filmId, int genreId);

    void removeFilmsGenres(int filmId);

    List<Genre> findGenreByFilmId(int filmId);
}
