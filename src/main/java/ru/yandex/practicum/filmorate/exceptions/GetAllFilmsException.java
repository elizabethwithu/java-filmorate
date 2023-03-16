package ru.yandex.practicum.filmorate.exceptions;

public class GetAllFilmsException extends RuntimeException {
    private static final String message = "Список фильмов пуст";

    public GetAllFilmsException() {
        super(message);
    }
}
