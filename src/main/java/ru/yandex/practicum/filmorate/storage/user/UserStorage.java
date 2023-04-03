package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface UserStorage { //определены методы добавления, удаления и модификации объектов
    User create(User user);

    void remove(Integer id);

    User update(User user);

    Collection<User> findAll();

    User findUserById(Integer id);
}
