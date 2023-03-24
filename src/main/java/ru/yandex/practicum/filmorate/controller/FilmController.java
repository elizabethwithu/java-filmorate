package ru.yandex.practicum.filmorate.controller;


import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.util.Collection;
import java.util.HashMap;

@Slf4j
@Data
@RestController
public class FilmController {
    protected final HashMap<Integer, Film> films = new HashMap<>();
    private int id = 1;

    @PostMapping("/films")
    public Film createFilm(@Valid @RequestBody Film film) {
        film.setId(id++);
        films.put(film.getId(), film);
        log.debug("Фильм {} успешно добавлен.", film);

        return film;
    }

    @PutMapping("/films")
    public Film updateFilm(@Valid @RequestBody Film film) throws NotFoundException {
        if (films.containsKey(film.getId())) {
            films.replace(film.getId(), film);
            log.debug("Фильм {} успешно обновлен.", film);
            } else {
                throw new NotFoundException(film);
            }

        return film;
    }

    @GetMapping("/films")
    public Collection<Film> findAll() {
        log.debug("Текущее количество фильмов: {}", films.size());
        return films.values();
    }
}
