package ru.yandex.practicum.filmorate.dao.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.MpaDao;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.NotValidParameterException;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.util.List;

@RequiredArgsConstructor
@Component
public class MpaDaoImpl implements MpaDao {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Mpa findMpaById(Integer id) {
        String sql = "select * from MPA_RATING where RATING_ID=?";
        try {
            return jdbcTemplate.queryForObject(sql, (ResultSet rs, int rowNum) -> Mpa.builder()
                    .id(rs.getInt(1))
                    .name(rs.getString(2))
                    .build(), id);
        } catch (IncorrectResultSizeDataAccessException e) {
            throw new NotValidParameterException();
        }
    }

    @Override
    public List<Mpa> findAllMpa() {
        String sql = "select * from MPA_RATING";
        try {
            return jdbcTemplate.query(sql, (ResultSet rs, int rowNum) -> Mpa.builder()
                    .id(rs.getInt(1))
                    .name(rs.getString(2))
                    .build());
        } catch (IncorrectResultSizeDataAccessException e) {
            throw new NotFoundException("Список жанров пуст.");
        }
    }
}
