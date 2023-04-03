package ru.yandex.practicum.filmorate.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Getter
@RequiredArgsConstructor
@Service
public class UserService { //добавление в друзья, удаление из друзей, вывод списка общих друзей
    private final UserStorage userStorage;

    public void addFriend(Integer userId, Integer friendId) {
        User user = userStorage.findUserById(userId);
        User friend = userStorage.findUserById(friendId);

        user.getFriends().add(friendId);
        friend.getFriends().add(userId);
        log.debug("Пользатели c id {} и {} друзья.", userId, friendId);
    }

    public void removeFriend(Integer userId, Integer friendId) {
        User user = userStorage.findUserById(userId);
        User friend = userStorage.findUserById(friendId);

        if (findUsersFriends(userId).isEmpty()) {
            log.debug("Список друзей пользователя c id {}  пуст.", userId);
            return;
        }
        if (!user.getFriends().contains(friendId)) {
            throw new NotFoundException(userStorage.findUserById(friendId));
        }
        user.getFriends().remove(friendId);
        friend.getFriends().remove(userId);
        log.debug("Пользатели c id {} и {} удалены из друзей друг друга.", userId, friendId);
    }

    public List<User> getMutualFriends(Integer user, Integer friend) {
        List<User> mutualFriends = findUsersFriends(user).stream()
                .filter(findUsersFriends(friend)::contains)
                .collect(Collectors.toList());

        log.debug("Найдены общие друзья пользователей c id {} и {}.", user, friend);
        return mutualFriends;
    }

    public List<User> findUsersFriends(Integer id) {
        Set<Integer> users = userStorage.findUserById(id).getFriends();
        List<User> usersFriends = new ArrayList<>();

        if (!users.isEmpty()) {
            for (Integer integer:users) {
                usersFriends.add(userStorage.findUserById(integer));
            }
        }

        log.debug("Составлен список друзей пользователя c id {}.", id);
        return usersFriends;
    }
}
