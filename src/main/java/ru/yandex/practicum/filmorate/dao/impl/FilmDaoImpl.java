package ru.yandex.practicum.filmorate.dao.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.FilmDao;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Component
public class FilmDaoImpl implements FilmDao {
    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public int create(Film film) {
        String query = "insert into FILMS (FILM_NAME, DESCRIPTION, RELEASE_DATE, DURATION, RATING_ID) " +
                "       values (?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(conn -> {
                    PreparedStatement preparedStatement = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

                    preparedStatement.setString(1, film.getName());
                    preparedStatement.setString(2, film.getDescription());
                    preparedStatement.setString(3, film.getReleaseDate().toString());
                    preparedStatement.setInt(4, film.getDuration());
                    preparedStatement.setInt(5, film.getMpa().getId());

                    return preparedStatement;
                },
                keyHolder
        );

        return Objects.requireNonNull(keyHolder.getKey()).intValue();
    }

    @Override
    public void remove(Integer id) {
        String query = "delete from FILMS where FILM_ID = ?";

        jdbcTemplate.update(query, id);
    }

    @Override
    public void update(Film film) {
            String query = "update FILMS set FILM_NAME = ?," +
                    "                        DESCRIPTION = ?, " +
                    "                        RELEASE_DATE = ?," +
                    "                        DURATION = ?," +
                    "                        RATING_ID = ?" +
                    "                    where FILM_ID = ?";

            jdbcTemplate.update(
                    query,
                    film.getName(),
                    film.getDescription(),
                    film.getReleaseDate(),
                    film.getDuration(),
                    film.getMpa().getId(),
                    film.getId()
            );
    }

    @Override
    public Collection<Film> findAll() {
        String query = "select F.*, MR.RATING_NAME " +
                "       from FILMS F " +
                "       join MPA_RATING MR on MR.RATING_ID = F.RATING_ID";

        return jdbcTemplate.query(query, FILM_ROW_MAPPER);
    }

    @Override
    public Film findFilmById(Integer id) {
        String query = "select F.*, MR.RATING_NAME " +
                "       from FILMS F " +
                "       join MPA_RATING MR on MR.RATING_ID = F.RATING_ID " +
                "       where FILM_ID = ?";

        try {
            return jdbcTemplate.queryForObject(query, FILM_ROW_MAPPER, id);
        } catch (IncorrectResultSizeDataAccessException e) {
            throw new NotFoundException("Фильм с запрашиваемым id отсутствует.");
        }
    }

    @Override
    public void addLike(int filmId, int userId) {
        String query = "insert into FAVORITE_FILMS (USER_ID, FILM_ID) values (?,?)";

        jdbcTemplate.update(query, userId, filmId);
    }

    public void removeLike(int filmId, int userId) {
        String query = "delete from FAVORITE_FILMS where FILM_ID = ? and USER_ID = ?";

        jdbcTemplate.update(query, filmId, userId);
    }

    public List<Film> findTopFilmsByLikesCount(int count) {
        String query = "select F.*, MR.RATING_NAME " +
                "       from FILMS F join MPA_RATING MR on MR.RATING_ID = F.RATING_ID " +
                "       left join FAVORITE_FILMS ff on f.FILM_ID = ff.FILM_ID " +
                "       group by f.FILM_ID, FILM_NAME " +
                "       order by COUNT(USER_ID) DESC limit ?";

        return jdbcTemplate.query(query, FILM_ROW_MAPPER, count);
    }

    public void addGenresToFilm(int filmId, List<Genre> genres) {
        String query = "insert into FILM_GENRE (FILM_ID, GENRE_ID) values (?,?)";

        jdbcTemplate.batchUpdate(
                query,
                new BatchPreparedStatementSetter() {
                    public void setValues(@NotNull PreparedStatement ps, int i) throws SQLException {
                        ps.setInt(1, filmId);
                        ps.setInt(2, genres.get(i).getId());
                    }

                    public int getBatchSize() {
                        return genres.size();
                    }
                });
    }

    @Override
    public void removeFilmsGenres(int filmId) {
        jdbcTemplate.update("delete from FILM_GENRE where FILM_ID=?",
                filmId);
    }

    @Override
    public List<Genre> findGenresByFilmId(int filmId) {
        String sql = "select G.* " +
                "     from GENRE G " +
                "     join FILM_GENRE FG on G.GENRE_ID = FG.GENRE_ID where FG.FILM_ID=?";
        return jdbcTemplate.query(sql,
                (ResultSet rs, int rowNum) -> Genre.builder()
                        .id(rs.getInt(1))
                        .name(rs.getString(2))
                        .build(), filmId);
    }

    @Override
    public Map<Integer, List<Genre>> findGenresByFilmIdIn(List<Integer> filmIds) {
        SqlParameterSource params = new MapSqlParameterSource("filmIds", filmIds);
        String sql = "select FG.FILM_ID, G.GENRE_ID, G.GENRE_NAME " +
                "     from FILM_GENRE FG " +
                "     join GENRE G on G.GENRE_ID = FG.GENRE_ID " +
                "     where FG.FILM_ID IN (:filmIds)";
        SqlRowSet rowSet = namedParameterJdbcTemplate.queryForRowSet(sql, params);

        Map<Integer, List<Genre>> filmGenresMap = new HashMap<>();
        while (rowSet.next()) {
            int id = rowSet.getInt(1);

            List<Genre> genreList = filmGenresMap.get(id);
            if (genreList == null) {
                genreList = new ArrayList<>();
            }
            Genre genre = Genre.builder()
                    .id(rowSet.getInt(2))
                    .name(rowSet.getString(3))
                    .build();
            genreList.add(genre);

            filmGenresMap.put(id, genreList);
        }

        return filmGenresMap;
    }

    public static final RowMapper<Film> FILM_ROW_MAPPER = (ResultSet rs, int rowNum) -> Film.builder()
            .id(rs.getInt(1))
            .name(rs.getString(2))
            .description(rs.getString(3))
            .releaseDate(Objects.requireNonNull(rs.getDate(4)).toLocalDate())
            .duration(rs.getInt(5))
            .mpa(Mpa.builder()
                    .id(rs.getInt(6))
                    .name(rs.getString(7))
                    .build())
            .build();
}
