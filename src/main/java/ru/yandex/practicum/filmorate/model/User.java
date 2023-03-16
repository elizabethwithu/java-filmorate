package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.filmorate.validation.UserBirthDate;
import ru.yandex.practicum.filmorate.validation.UserLogin;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

import static ru.yandex.practicum.filmorate.validation.UserValidationFailedMessage.*;

@Builder
@AllArgsConstructor
@Data
public class User {

    @NotNull(message = LOGIN_IS_NULL)
    @NotBlank(message = LOGIN_IS_BLANK)
    @UserLogin(message = LOGIN_CONTAINS_WHITESPACE)
    private String login;

    private String name;

    @NotNull(message = EMAIL_IS_NULL)
    @NotBlank(message = EMAIL_IS_BLANK)
    @Email(message = EMAIL_INCORRECT_FORMAT)
    private String email;

    @NotNull(message = BIRTHDATE_IS_NULL)
    @UserBirthDate(message = BIRTHDATE_IS_AFTER_NOW)
    private LocalDate birthday;

    private int id;
}
