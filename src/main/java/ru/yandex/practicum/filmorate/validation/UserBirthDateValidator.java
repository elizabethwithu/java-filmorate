package ru.yandex.practicum.filmorate.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class UserBirthDateValidator implements ConstraintValidator<UserBirthDate, LocalDate> {

    @Override
    public boolean isValid(final LocalDate valueToValidate, final ConstraintValidatorContext context) {
        return valueToValidate.isBefore(LocalDate.now());
    }
}
