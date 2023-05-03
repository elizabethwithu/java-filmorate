package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.dao.UserDao;

import java.util.*;


@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {
    private final UserDao userDao;

    public User createUser(User user) {
        userDao.checkUserUniqueness(user);
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        int id = userDao.create(user);
        user.setId(id);
        log.debug("Пользователь {} успешно создан.", user);
        return user;
    }

    public User updateUser(User user) {
        userDao.findUserById(user.getId());
        userDao.update(user);
        log.debug("Пользователь {} успешно обновлен.", user);
        return user;
    }

    public Collection<User> findAllUsers() {
        Collection<User> users = userDao.findAll();

        log.debug("Текущее количество пользователей: {}", users.size());
        return users;
    }

    public void removeUserById(Integer id) {
        User user = userDao.findUserById(id);
        userDao.remove(id);
        log.debug("Пользователь {} успешно удален.", user);
    }

    public User findUserById(Integer id) {
        User user = userDao.findUserById(id);
        log.debug("Получен пользователь {}.", user);
        return user;
    }

    public void addFriend(Integer userId, Integer friendId) {
        User user = userDao.findUserById(userId);
        User friend = userDao.findUserById(friendId);

        if (findUsersFriends(friendId).contains(user)) {
            userDao.addFriend(userId, friendId, true);
            log.debug("Пользователь {} подтвердил дружбу с пользователем {}", user, friend);
            return;
        }
        userDao.addFriend(userId, friendId, false);
        log.debug("Пользователь {} добавил в друзья пользователя {}.", user, friend);
    }

    public void removeFriend(Integer userId, Integer friendId) {
        User user = userDao.findUserById(userId);
        User friend = userDao.findUserById(friendId);

        userDao.removeFriend(userId, friendId);
        log.debug("Пользователи c {} и {} удалены из друзей друг друга.", user, friend);
    }

    public List<User> getMutualFriends(Integer user, Integer friend) {
        List<User> mutualFriends = userDao.getCommonFriends(user, friend);
        log.debug("Найдены общие друзья пользователей c id {} и {}.", user, friend);

        return mutualFriends;
    }

    public List<User> findUsersFriends(Integer id) {
        List<User> usersFriends = userDao.getUserFriends(id);
        log.debug("Составлен список друзей пользователя c id {}.", id);

        return usersFriends;
    }
}
