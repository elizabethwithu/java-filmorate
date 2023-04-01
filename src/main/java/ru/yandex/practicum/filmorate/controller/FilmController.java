package ru.yandex.practicum.filmorate.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private final FilmService filmService;
    private final FilmStorage filmStorage;

    @Autowired
    public FilmController(FilmService filmService, FilmStorage filmStorage) {
        this.filmService = filmService;
        this.filmStorage = filmStorage;
    }

    @PostMapping()
    public Film createFilm(@Valid @RequestBody Film film) {
        return filmStorage.create(film);
    }

    @DeleteMapping("/{id}")
    public void removeFilm(@PathVariable Integer id) {
       filmStorage.remove(id);
    }

    @PutMapping()
    public Film updateFilm(@Valid @RequestBody Film film) throws NotFoundException {
        return filmStorage.update(film);
    }

    @GetMapping()
    public Collection<Film> findAll() {
        return filmStorage.findAll();
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable Integer id, @PathVariable Integer userId) {
        filmService.addLike(id, userId);
    }

    @GetMapping("/{id}")
    public Film findFilmById(@PathVariable Integer id) {
        return filmService.findFilmById(id);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void removeLike(@PathVariable Integer id, @PathVariable Integer userId) {
        filmService.removeLike(id, userId);
    }

    @GetMapping("/popular")
    public List<Film> findTopFilms(@RequestParam(defaultValue = "10", required = false) Integer count) {
        return filmService.findTopFilmByLikes(count);
    }
}
