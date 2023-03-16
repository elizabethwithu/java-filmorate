package ru.yandex.practicum.filmorate.validation;

public interface FilmValidationFailedMessage {

    String NAME_IS_BLANK = "Название пустое.";
    String NAME_IS_NULL = "Название фильма не указано.";
    String DESCRIPTION_IS_NULL = "Описание фильма не указано.";
    String DESCRIPTION_TOO_LONG = "Длина описания превышает 200 символов.";
    String RELEASE_DATE_IS_NULL = "Дата релиза фильма не указана.";
    String RELEASE_DATE_BEFORE_1895 = "Дата релиза раньше 28 декабря 1895 года.";
    String DURATION_IS_EMPTY = "Продолжительность фильма не указана.";
    String DURATION_IS_NEGATIVE = "Продолжительность фильма отрицательная.";

}
