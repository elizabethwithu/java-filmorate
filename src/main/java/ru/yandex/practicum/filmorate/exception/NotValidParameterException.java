package ru.yandex.practicum.filmorate.exception;

public class NotValidParameterException extends RuntimeException {
    public NotValidParameterException() {
        super("id должен быть больше ноля.");
    }

    public NotValidParameterException(String s) {
        super(s);
    }
}
