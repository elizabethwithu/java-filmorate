package ru.yandex.practicum.filmorate.exceptions;

import ru.yandex.practicum.filmorate.model.Film;

public class GetFilmException extends RuntimeException {
    private static final String message = "Фильм %s отсутствует в памяти программы.";

    public GetFilmException(Film film) {
        super(
                String.format(message, film)
        );
    }
}
