package ru.yandex.practicum.filmorate.impl;

import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.GenreDao;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.NotValidParameterException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.util.List;

@Component
public class GenreDaoImpl implements GenreDao {
    private final JdbcTemplate jdbcTemplate;

    public GenreDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Genre findByGenreId(int id) {
        String sql = "select * from GENRE where GENRE_ID=?";
        try {
            return jdbcTemplate.queryForObject(sql, (ResultSet rs, int rowNum) -> Genre.builder()
                    .id(rs.getInt(1))
                    .name(rs.getString(2))
                    .build(), id);
        } catch (IncorrectResultSizeDataAccessException e) {
            throw new NotValidParameterException();
        }
    }

    @Override
    public List<Genre> findAllGenres() {
        String sql = "select * from GENRE";
        try {
            return jdbcTemplate.query(sql,(ResultSet rs, int rowNum) -> Genre.builder()
                    .id(rs.getInt(1))
                    .name(rs.getString(2))
                    .build());
        } catch (IncorrectResultSizeDataAccessException e) {
        throw new NotFoundException("Список жанров пуст.");
        }
    }
}
