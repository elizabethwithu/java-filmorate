package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;

public interface UserStorage { //определены методы добавления, удаления и модификации объектов
    User create(User user);

    void remove(Integer id);

    User update(User user);

    Collection<User> findAll();

    HashMap<Integer, User> getUsers();
}
