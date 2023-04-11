package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class FilmService { //добавление и удаление лайка, вывод 10 наиболее популярных фильмов по количеству лайков
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    public Film createFilm(Film film) {
        return filmStorage.create(film);
    }

    public void removeFilmById(Integer id) {
        filmStorage.remove(id);
    }

    public Film updateFilm(Film film) {
        return filmStorage.update(film);
    }

    public Collection<Film> findAllFilms() {
        return filmStorage.findAll();
    }

    public Film findFilmById(Integer id) {
        return filmStorage.findFilmById(id);
    }

    public void addLike(Integer filmId, Integer userId) {
        Film film = filmStorage.findFilmById(filmId);
        User user = userStorage.findUserById(userId);

        film.getIdUsersWhoLikedFilm().add(userId);
        log.debug("Пользователь c {} поставил лайк фильму с айди {}.", user, filmId);
    }

    public void removeLike(Integer filmId, Integer userId) {
        User user = userStorage.findUserById(userId);
        Set<Integer> idUsersWhoLikedFilm = filmStorage.findFilmById(filmId).getIdUsersWhoLikedFilm();

        if (idUsersWhoLikedFilm.isEmpty()) {
            throw new NotFoundException("Список фильмов пуст.");
        }
        idUsersWhoLikedFilm.remove(userId);
        log.debug("Пользователь {} удалил лайк фильму с айди {}.", user, filmId);
    }

    public List<Film> findTopFilmByLikes(Integer count) {
        Collection<Film> films = filmStorage.findAll();

        if (films.isEmpty()) {
            throw new NotFoundException("Список фильмов пуст.");
        }
        List<Film> topFilms = films.stream()
                .sorted(Comparator.comparing(Film::getFilmsLikes).reversed())
                .limit(count)
                .collect(Collectors.toList());
        log.debug("Топ {} фильмов успешно отобран.", count);

        return topFilms;
    }
}
