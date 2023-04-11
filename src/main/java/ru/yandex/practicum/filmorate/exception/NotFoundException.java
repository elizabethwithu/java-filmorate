package ru.yandex.practicum.filmorate.exception;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

public class NotFoundException extends RuntimeException {
    private static final String message = "%s отсутствует в памяти программы.";

    public NotFoundException(User user) {
        super(
                String.format(message, user)
        );
    }

    public NotFoundException(Film film) {
        super(
                String.format(message, film)
        );
    }

    public NotFoundException(String s) {
        super(s);
    }
}
