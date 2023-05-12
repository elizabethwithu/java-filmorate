package ru.yandex.practicum.filmorate.model;

import lombok.*;
import org.jetbrains.annotations.NotNull;

import javax.validation.constraints.NotBlank;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Mpa {
    @NonNull
    Integer id;
    @NotBlank
    String name;

    public Mpa(@NotNull Integer id) {
        this.id = id;
    }
}
