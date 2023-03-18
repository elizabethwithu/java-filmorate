package ru.yandex.practicum.filmorate.validation;

public class FilmValidationFailedMessage {
    public static final String NAME_IS_BLANK = "Название пустое.";
    public static final String DESCRIPTION_IS_NULL = "Описание фильма не указано.";
    public static final String DESCRIPTION_TOO_LONG = "Длина описания превышает 200 символов.";
    public static final String RELEASE_DATE_IS_NULL = "Дата релиза фильма не указана.";
    public static final String RELEASE_DATE_BEFORE_1895 = "Дата релиза раньше 28 декабря 1895 года.";
    public static final String DURATION_IS_EMPTY = "Продолжительность фильма не указана.";
    public static final String DURATION_IS_NEGATIVE = "Продолжительность фильма отрицательная.";

}
