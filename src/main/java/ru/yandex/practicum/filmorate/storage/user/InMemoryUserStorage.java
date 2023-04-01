package ru.yandex.practicum.filmorate.storage.user;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.NotValidId;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;

@Slf4j
@Data
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
        if (id <= 0) {
            throw new NotValidId();
        }
        if (users.containsKey(id)) {
            users.remove(id);
            log.debug("Пользователь с id {} успешно удален.", id);
        } else {
            throw new NotFoundException("Пользователь с запрашиваемым id не зарегестрирован.");
        }
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
    public HashMap<Integer, User> getUsers() {
        return users;
    }
}
