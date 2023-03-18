package ru.yandex.practicum.filmorate.controller;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.Collection;
import java.util.HashMap;

@Slf4j
@Data
@RestController
public class UserController {
    protected final HashMap<Integer, User> users = new HashMap<>();
    private int id = 1;

    @PostMapping("/users")
    public User createUser(@Valid @RequestBody User user) {
        if(user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        user.setId(id++);
        users.put(user.getId(), user);
        log.debug("Пользователь {} успешно создан.", user);

        return user;
    }

    @PutMapping("/users")
    public User updateUser(@Valid @RequestBody User user) {
        if (users.containsKey(user.getId())) {
            users.replace(user.getId(), user);
            log.debug("Пользатель {} успешно обновлен.", user);
        } else {
            throw new NotFoundException(user);
        }

        return user;
    }

    @GetMapping("/users")
    public Collection<User> findAll() {
        log.debug("Текущее количество пользователей: {}", users.size());
        return users.values();
    }
}
