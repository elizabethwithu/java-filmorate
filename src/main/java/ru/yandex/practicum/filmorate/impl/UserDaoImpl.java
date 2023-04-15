package ru.yandex.practicum.filmorate.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.UserDao;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.NotValidParameterException;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Slf4j
@Component
public class UserDaoImpl implements UserDao {
    private final JdbcTemplate jdbcTemplate;

    public UserDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public int create(User user) {
        String query = "insert into USERS (LOGIN, USER_NAME, EMAIL, BIRTHDAY) values (?,?,?,?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        try {
            jdbcTemplate.update(conn -> {
                        PreparedStatement preparedStatement = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

                        preparedStatement.setString(1, user.getLogin());
                        preparedStatement.setString(2, user.getName());
                        preparedStatement.setString(3, user.getEmail());
                        preparedStatement.setString(4, user.getBirthday().toString());

                        return preparedStatement;
                    },
                    keyHolder
            );

            return Objects.requireNonNull(keyHolder.getKey()).intValue();
        } catch (DataIntegrityViolationException e) {
            throw new NotValidParameterException("Пользователь с похожим логином/почтой уже зарегистрированы");
        }
    }

    @Override
    public void remove(Integer id) {
        String query = "delete from USERS where USER_ID = ?";

        jdbcTemplate.update(query, id);
    }

    @Override
    public void update(User user) {
        String query = "update USERS set LOGIN = ?," +
                "                        USER_NAME = ?, " +
                "                        EMAIL = ?," +
                "                        BIRTHDAY = ?" +
                "                    where USER_ID = ?";

        try {
            jdbcTemplate.update(
                    query,
                    user.getLogin(),
                    user.getName(),
                    user.getEmail(),
                    user.getBirthday(),
                    user.getId());
        } catch (DataIntegrityViolationException e) {
            throw new NotValidParameterException("Пользователь с похожим логином/почтой уже зарегистрированы");
        }
    }

    @Override
    public Collection<User> findAll() {
        String query = "select * from USERS";

        try {
            return jdbcTemplate.query(query, USER_ROW_MAPPER);
        } catch (IncorrectResultSizeDataAccessException e) {
            throw new NotFoundException("Список пользователей пуст.");
        }
    }

    @Override
    public User findUserById(Integer id) {
        String query = "select * from USERS where USER_ID = ?";

        try {
            return jdbcTemplate.queryForObject(query, USER_ROW_MAPPER, id);
        } catch (IncorrectResultSizeDataAccessException e) {
            throw new NotFoundException("Пользователь с запрашиваемым id не зарегистрирован.");
        }
    }

    @Override
    public void addFriend(Integer userId, Integer friendId, boolean status) {
        String query = "insert into FRIENDS (USER_SENDER_ID, FRIEND_ID, STATUS) values (?, ?, ?)";
        jdbcTemplate.update(
                query,
                userId,
                friendId,
                status
        );
    }

    @Override
    public List<User> getUserFriends(int userId) {
        String  query = "select * from USERS where USER_ID in (select FRIEND_ID from FRIENDS where USER_SENDER_ID = ?)" +
                "or USER_ID in (select USER_SENDER_ID from FRIENDS where FRIEND_ID = ? and STATUS = true)";

        return jdbcTemplate.query(query, USER_ROW_MAPPER, userId, userId);
    }

    @Override
    public void removeFriend(int userId, int friendId) {
        String query = "delete from FRIENDS " +
                "where USER_SENDER_ID = ? and FRIEND_ID = ? or FRIEND_ID = ? and USER_SENDER_ID = ?";
        jdbcTemplate.update(query, userId, friendId, friendId, userId);

    }

    @Override
    public List<User> getCommonFriends(int userId, int friendId) {
        String query = "select * from USERS where (USER_ID in (select FRIEND_ID from FRIENDS where USER_SENDER_ID = ?)" +
                       "or USER_ID in (select USER_SENDER_ID from FRIENDS where FRIEND_ID = ? and STATUS = true))" +
                       "and (USER_ID in (select FRIEND_ID from FRIENDS where USER_SENDER_ID = ?)" +
                       "or USER_ID in (select USER_SENDER_ID from FRIENDS where FRIEND_ID = ? and STATUS = true))";

        return jdbcTemplate.query(query, USER_ROW_MAPPER, friendId, userId, friendId, userId);

    }

    @Override
    public void checkUserUniqueness(User user) {
        String sqlQuery = "select * from USERS " +
                "where (email=?) or (login=?);";
        SqlRowSet usersRow = jdbcTemplate.queryForRowSet(sqlQuery, user.getEmail(), user.getLogin());

        if (usersRow.next()) {
            int id = Integer.parseInt(Objects.requireNonNull(usersRow.getString("user_id")));
            throw new NotValidParameterException(String.format("Пользователь с похожими логином или почтой уже зарегистрирован, id = %d", id));
        }
    }

    private static final RowMapper<User> USER_ROW_MAPPER = (ResultSet rs, int rowNum) -> User.builder()
            .id(rs.getInt(1))
            .login(rs.getString(2))
            .name(rs.getString(3))
            .email(rs.getString(4))
            .birthday(Objects.requireNonNull(rs.getDate(5).toLocalDate()))
            .build();
}
