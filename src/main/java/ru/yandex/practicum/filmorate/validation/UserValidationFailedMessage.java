package ru.yandex.practicum.filmorate.validation;

public interface UserValidationFailedMessage {
    String LOGIN_CONTAINS_WHITESPACE = "Логин содержит пробелы.";
    String LOGIN_IS_BLANK = "Логин пустой.";
    String LOGIN_IS_NULL = "Логин не указан.";
    String EMAIL_IS_NULL = "Почтовый адрес не указан.";
    String EMAIL_INCORRECT_FORMAT = "Неверный формат почтового адреса.";
    String EMAIL_IS_BLANK = "Почтовый адрес пустой.";
    String BIRTHDATE_IS_NULL = "Дата рождения не указана.";
    String BIRTHDATE_IS_AFTER_NOW = "Дата рождения еще не наступила.";

}
