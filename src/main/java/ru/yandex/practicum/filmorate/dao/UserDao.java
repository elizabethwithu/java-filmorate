package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.List;

public interface UserDao {
    int create(User user);

    void remove(Integer id);

    void update(User user);

    Collection<User> findAll();

    User findUserById(Integer id);

    void addFriend(Integer userId, Integer friendId, boolean status);

    List<User> getUserFriends(int userId);

    void removeFriend(int userId, int friendId);

    List<User> getCommonFriends(int userId, int friendId);

    void checkUserUniqueness(User user);
}
