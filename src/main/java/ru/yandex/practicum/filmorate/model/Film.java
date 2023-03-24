package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.filmorate.validation.FilmReleaseDate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;

import static ru.yandex.practicum.filmorate.validation.FilmValidationFailedMessage.*;

@Builder
@AllArgsConstructor
@Data
public class Film {
    @NotBlank(message = NAME_IS_BLANK)
    private String name;

    @NotBlank(message = DESCRIPTION_IS_NULL)
    @Size(max = 200, message = DESCRIPTION_TOO_LONG)
    private String description;

    @NotNull(message = RELEASE_DATE_IS_NULL)
    @FilmReleaseDate(message = RELEASE_DATE_BEFORE_1895)
    private LocalDate releaseDate;

    @NotNull(message = DURATION_IS_EMPTY)
    @Positive(message = DURATION_IS_NEGATIVE)
    private int duration;

    private Integer id;
}

