package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.NotValidParameterException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class UserDaoTest {
    private final UserDao userDao;
    User testUser;
    User testFriend;

    @BeforeEach
    void init() {
        testUser = User.builder()
                .login("nicky")
                .name("nick")
                .email("mail@mail.ru")
                .birthday(LocalDate.of(2000,3, 20))
                .build();

        testFriend = User.builder()
                .login("alex")
                .name("alexey")
                .email("yandex@yandex.ru")
                .birthday(LocalDate.of(2000,3, 20))
                .build();
    }

    @Test
    public void createUserAndSetId() {
        int resultId = userDao.create(testUser);

        assertEquals(1, resultId);
    }

    @Test
    public void removeUser() {
        int resultId = userDao.create(testUser);

        userDao.remove(resultId);

        final NotFoundException exception = assertThrows(
                NotFoundException.class,
                    () -> userDao.findUserById(resultId)
            );

        assertEquals("Пользователь с запрашиваемым id не зарегистрирован.", exception.getMessage());
    }

    @Test
    public void findUserById() {
        userDao.create(testUser);
        User user = userDao.findUserById(1);

        assertEquals(testUser.getLogin(), user.getLogin());
        assertEquals(testUser.getEmail(),  user.getEmail());
        assertEquals(testUser.getBirthday(), user.getBirthday());
    }

    @Test
    public void findUserByNotExistId() {
        final NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> userDao.findUserById(5)
        );

        assertEquals("Пользователь с запрашиваемым id не зарегистрирован.", exception.getMessage());
    }

    @Test
    public void updateUser() {
        int resultId = userDao.create(testUser);

        User updateUser = User.builder()
                .id(resultId)
                .login("testUpdateUserWithNormalId")
                .email("testUpdateUserWithNormalId@ya.ru")
                .birthday(LocalDate.now())
                .build();

        userDao.update(updateUser);

        User user = userDao.findUserById(1);

        assertEquals("testUpdateUserWithNormalId", user.getLogin());
    }

    @Test
    public void findAll() {
        userDao.create(testUser);

        Collection<User> users = userDao.findAll();
        assertEquals(1, users.size());
    }

    @Test
    public void findAllWithEmptyCollection() {
        Collection<User> users = userDao.findAll();

        assertTrue(users.isEmpty());
    }

    @Test
    public void addFriendRequest() {
        int userId = userDao.create(testUser);
        int friendId = userDao.create(testFriend);

        userDao.addFriend(userId, friendId, false);
        List<User> friends = userDao.getUserFriends(userId);

        assertEquals(1, friends.size());
    }

    @Test
    public void addFriendConfirmation() {
        int userId = userDao.create(testUser);
        int friendId = userDao.create(testFriend);

        userDao.addFriend(userId, friendId, true);
        List<User> userFriends = userDao.getUserFriends(userId);
        List<User> friendFriends = userDao.getUserFriends(friendId);


        assertEquals(1, userFriends.size());
        assertEquals(1, friendFriends.size());
    }

    @Test
    public void getUserFriends() {
        int userId = userDao.create(testUser);
        int friendId = userDao.create(testFriend);

        userDao.addFriend(userId, friendId, false);
        List<User> userFriends = userDao.getUserFriends(userId);

        assertEquals(1, userFriends.size());
        assertEquals(friendId, userFriends.get(0).getId());
    }

    @Test
    public void getUserFriendsWithEmptyList() {
        int userId = userDao.create(testUser);

        List<User> userFriends = userDao.getUserFriends(userId);

        assertTrue(userFriends.isEmpty());
    }

    @Test
    public void removeFriend() {
        int userId = userDao.create(testUser);
        int friendId = userDao.create(testFriend);
        userDao.addFriend(userId, friendId, true);

        userDao.removeFriend(userId, friendId);
        List<User> userFriends = userDao.getUserFriends(userId);

        assertTrue(userFriends.isEmpty());
    }

    @Test
    public void getCommonFriends() {
        User commonFriend = User.builder()
                .login("misha")
                .email("rambler@rambler.ru")
                .birthday(LocalDate.of(2000,9, 10))
                .build();
        int userId = userDao.create(testUser);
        int friendId = userDao.create(testFriend);
        int commonFriendId = userDao.create(commonFriend);

        userDao.addFriend(userId, commonFriendId, true);
        userDao.addFriend(friendId, commonFriendId, true);

        List<User> commonFriends = userDao.getCommonFriends(userId, friendId);
        assertEquals(1, commonFriends.size());
        assertEquals(3, commonFriends.get(0).getId());
    }

    @Test
    public void getCommonFriendsWithEmptyList() {
        int userId = userDao.create(testUser);
        int friendId = userDao.create(testFriend);

        List<User> friends = userDao.getCommonFriends(userId, friendId);
        assertTrue(friends.isEmpty());
    }

    @Test
    public void checkUserUniqueness() {
        userDao.create(testUser);
        testFriend.setLogin(testUser.getLogin());

        final NotValidParameterException exception = assertThrows(
                NotValidParameterException.class,
                () -> userDao.checkUserUniqueness(testFriend)
        );

        assertEquals("Пользователь с похожими логином или почтой уже зарегистрирован, id = 1", exception.getMessage());
    }
}
