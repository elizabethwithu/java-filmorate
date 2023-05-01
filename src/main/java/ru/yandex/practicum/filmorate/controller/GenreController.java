package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.GenreService;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class GenreController {
    private final GenreService genreService;

    @GetMapping("/genres")
    public List<Genre> findAllGenres() {
        return genreService.findAllGenres();
    }

    @GetMapping("/genres/{id}")
    public Genre findGenreById(@PathVariable Integer id) {
        return genreService.findGenreById(id);
    }
}
