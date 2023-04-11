package ru.yandex.practicum.filmorate.exception;

public class NotValidId extends RuntimeException {
    public NotValidId() {
        super("id должен быть больше ноля.");
    }
}
