package ru.yandex.practicum.filmorate.model;

import lombok.*;
import ru.yandex.practicum.filmorate.validation.UserLogin;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static ru.yandex.practicum.filmorate.validation.UserValidationFailedMessage.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class User {
    @NotBlank(message = LOGIN_IS_BLANK)
    @UserLogin(message = LOGIN_CONTAINS_WHITESPACE)
    private String login;

    private String name;

    @NotBlank(message = EMAIL_IS_BLANK)
    @Email(message = EMAIL_INCORRECT_FORMAT)
    private String email;

    @NotNull(message = BIRTHDATE_IS_NULL)
    @Past(message = BIRTHDATE_IS_AFTER_NOW)
    private LocalDate birthday;

    private Integer id;

    private Set<Integer> friends = new HashSet<>();
}
