package ru.yandex.practicum.filmorate.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class FilmDurationValidator implements ConstraintValidator<FilmDuration, Integer> {

    @Override
    public boolean isValid(final Integer valueToValidate, final ConstraintValidatorContext context) {
        return valueToValidate >= 0;
        }
}
