package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.Collection;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final UserStorage userStorage;

    @Autowired
    public UserController(UserService userService, UserStorage userStorage) {
        this.userService = userService;
        this.userStorage = userStorage;
    }

    @PostMapping()
    public User createUser(@Valid @RequestBody User user) {
        return userStorage.create(user);
    }

    @PutMapping()
    public User updateUser(@Valid @RequestBody User user) {
        return userStorage.update(user);
    }

    @GetMapping()
    public Collection<User> findAll() {
        return userStorage.findAll();
    }

    @DeleteMapping("/{id}")
    public void removeUser(@PathVariable Integer id) {
        userStorage.remove(id);
    }

    @GetMapping("/{id}")
    public User findUserById(@PathVariable Integer id) {
        return userService.findUserById(id);
    }

    @PutMapping("/{id}/friends/{friendId}") //добавление в друзья
    public void addFriend(@PathVariable Integer id, @PathVariable Integer friendId) {
        userService.addFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}") //удаление из друзей
    public void removeFriend(@PathVariable Integer id, @PathVariable Integer friendId) {
        userService.removeFriend(id, friendId);
    }

    @GetMapping("/{id}/friends") //список друзей
    public List<User> findFriends(@Positive @PathVariable Integer id) {
        return userService.findUsersFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}") //список друзей, общих с другим пользователем
    public List<User> findMutualFriends(@PathVariable Integer id, @PathVariable Integer otherId) {
        return userService.getMutualFriends(id, otherId);
    }
}
