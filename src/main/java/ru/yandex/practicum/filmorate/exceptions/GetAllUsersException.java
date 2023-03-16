package ru.yandex.practicum.filmorate.exceptions;

public class GetAllUsersException extends RuntimeException{
    private static final String message = "Список пользователей пуст";

    public GetAllUsersException() {
        super(message);
    }
}
