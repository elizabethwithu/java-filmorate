package ru.yandex.practicum.filmorate.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UserLoginValidator implements ConstraintValidator<UserLogin, String> {

    @Override
    public boolean isValid(final String valueToValidate, final ConstraintValidatorContext context) {
        return valueToValidate != null
                && !valueToValidate.contains(" ");
    }
}
