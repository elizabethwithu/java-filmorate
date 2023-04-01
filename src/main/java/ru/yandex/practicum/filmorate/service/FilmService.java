package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.NotValidId;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FilmService { //добавление и удаление лайка, вывод 10 наиболее популярных фильмов по количеству лайков
    private final FilmStorage filmStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public void addLike(Integer filmId, Integer userId) {
        checkId(filmId, userId);
        findFilmById(filmId).getIdUsersWhoLikedFilm().add(userId);
        log.debug("Пользователь c id {} поставил лайк фильму с айди {}.", userId, filmId);
    }

    public void removeLike(Integer filmId, Integer userId) {
        checkId(filmId, userId);
        if (findFilmById(filmId).getIdUsersWhoLikedFilm().isEmpty()) {
            throw new NotFoundException("Список фильмов пуст.");
        }
        findFilmById(filmId).getIdUsersWhoLikedFilm().remove(userId);
        log.debug("Пользователь c id {} удалил лайк фильму с айди {}.", userId, filmId);
    }

    public List<Film> findTopFilmByLikes(Integer count) {
        if (filmStorage.findAll().isEmpty()) {
            throw new NotFoundException("Список фильмов пуст.");
        }
        log.debug("Топ {} фильмов успешно отобран.", count);
        return filmStorage.findAll().stream()
                .sorted(Comparator.comparing(Film::getFilmsLikes).reversed())
                .limit(count)
                .collect(Collectors.toList());
    }

    public Film findFilmById(Integer id) {
        if (id <= 0) {
            throw new NotValidId();
        }
        if (!filmStorage.getFilms().containsKey(id)) {
            throw new NotFoundException("Фильм с запрашиваемым id отсутствует.");
        }
        log.debug("Получен фильм с айди {}.", id);
        return filmStorage.getFilms().get(id);
    }

    private void checkId(Integer filmId, Integer userId) {
        if (filmId <= 0 || userId <= 0) {
            throw new NotValidId();
        }
    }
}
