package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.NotValidId;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage { //логика хранения, обновления и поиска объектов
    protected final HashMap<Integer, User> users = new HashMap<>();

    private static Integer id = 1;

    private static Integer getNextId() {
        return id++;
    }

    @Override
    public User create(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        user.setId(getNextId());
        users.put(user.getId(), user);
        log.debug("Пользователь {} успешно создан.", user);
        return user;
    }

    @Override
    public void remove(Integer id) {
        User user = findUserById(id);
        users.remove(id);
        log.debug("Пользователь {} успешно удален.", user);
    }

    @Override
    public User update(User user) {
        if (users.containsKey(user.getId())) {
            users.replace(user.getId(), user);
        } else {
            throw new NotFoundException(user);
        }
        log.debug("Пользатель {} успешно обновлен.", user);
        return user;
    }

    @Override
    public Collection<User> findAll() {
        log.debug("Текущее количество пользователей: {}", users.values().size());
        return users.values();
    }

    @Override
    public User findUserById(Integer id) {
        if (id <= 0) {
            throw new NotValidId();
        }
        if (!users.containsKey(id)) {
            throw new NotFoundException("Пользователь с запрашиваемым id не зарегестрирован.");
        }
        log.debug("Найден пользователь c id {}.", id);
        return users.get(id);
    }
}
