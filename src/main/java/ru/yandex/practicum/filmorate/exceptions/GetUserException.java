package ru.yandex.practicum.filmorate.exceptions;

import ru.yandex.practicum.filmorate.model.User;

public class GetUserException extends RuntimeException {
    private static final String message = "Пользователь %s отсутствует в памяти программы.";

    public GetUserException(User user) {
        super(
                String.format(message, user)
        );
    }
}
