package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.NotValidId;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.HashMap;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage { //логика хранения, обновления и поиска объектов
    private final HashMap<Integer, Film> films = new HashMap<>();
    private static Integer id = 1;

    private static Integer getNextId() {
        return id++;
    }

    @Override
    public Film create(Film film) {
        film.setId(getNextId());
        films.put(film.getId(), film);
        log.debug("Фильм {} успешно добавлен.", film);
        return film;
    }

    @Override
    public void remove(Integer id) {
        if (id <= 0) {
            throw new NotValidId();
        }
        if (films.containsKey(id)) {
            films.remove(id);
            log.debug("Фильм {} успешно удален.", id);
        } else {
            throw new NotFoundException("Фильм с запрашиваемым id отсутствует.");
        }
    }

    @Override
    public Film update(Film film) {
        if (films.containsKey(film.getId())) {
            films.replace(film.getId(), film);
            log.debug("Фильм {} успешно обновлен.", film);
        } else {
            throw new NotFoundException(film);
        }
        return film;
    }

    @Override
    public Collection<Film> findAll() {
        log.debug("Текущее количество фильмов: {}", films.values().size());
        return films.values();
    }

    @Override
    public HashMap<Integer, Film> getFilms() {
        return films;
    }

}
