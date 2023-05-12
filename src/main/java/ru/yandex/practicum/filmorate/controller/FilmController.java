package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/films")
public class FilmController {
    private final FilmService filmService;

    @PostMapping
    public Film createFilm(@Valid @RequestBody Film film) {
        return filmService.createFilm(film);
    }

    @DeleteMapping("/{id}")
    public void removeFilm(@PathVariable Integer id) {
        filmService.removeFilmById(id);
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) throws NotFoundException {
        return filmService.updateFilm(film);
    }

    @GetMapping
    public Collection<Film> findAll() {
        return filmService.findAllFilms();
    }

    @GetMapping("/{id}")
    public Film findFilmById(@PathVariable Integer id) {
        return filmService.findFilmById(id);
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable Integer id, @PathVariable Integer userId) {
        filmService.addLike(id, userId);
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
