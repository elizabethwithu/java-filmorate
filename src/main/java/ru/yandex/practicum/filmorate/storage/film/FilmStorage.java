package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.HashMap;

public interface FilmStorage { //определены методы добавления, удаления и модификации объектов
    Film create(Film film);

    void remove(Integer id);

    Film update(Film film);

    Collection<Film> findAll();

    HashMap<Integer, Film> getFilms();
}
