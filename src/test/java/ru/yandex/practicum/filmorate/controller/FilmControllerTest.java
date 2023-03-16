package ru.yandex.practicum.filmorate.controller;

import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.yandex.practicum.filmorate.model.Film;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class FilmControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    private FilmController controller;

    @AfterEach
    void init() {
        controller.getFilms().clear();
        controller.setId(1);
    }

    @SneakyThrows
    @Test
    void succeedCreateFilm() {
        RequestBuilder requestBuilder = request(HttpMethod.POST,"http://localhost:8080/films")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                        "{" +
                            "\"name\": \"Матрица\"," +
                            "\"description\": \"Научно-фантастический боевик\"," +
                            "\"releaseDate\": \"1999-07-03\"," +
                            "\"duration\": 136" +
                        "}"
                );

        mockMvc.perform(requestBuilder)

                .andExpectAll(
                        MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON),
                        MockMvcResultMatchers.content().json(
                                "{" +
                                "              \"name\": \"Матрица\"," +
                                "              \"description\": \"Научно-фантастический боевик\"," +
                                "              \"releaseDate\": \"1999-07-03\"," +
                                "              \"duration\": 136," +
                                "              \"id\": 1" +
                                "           }")
                );

        assertNotNull(controller.films.get(1));
    }

    @SneakyThrows
    @Test
    void createFilmWithEmptyName() {
        RequestBuilder requestBuilder = request(HttpMethod.POST,"http://localhost:8080/films")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                        "{" +
                        "   \"name\": \"\"," +
                        "   \"description\": \"Научно-фантастический боевик\"," +
                        "   \"releaseDate\": \"1999-07-03\"," +
                        "   \"duration\": 136" +
                        "}");

        mockMvc.perform(requestBuilder)

                .andExpectAll(
                        MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON),
                        MockMvcResultMatchers.content().json(
                                "{" +
                                "              \"name\": \"Название пустое.\"" +
                                "           }")
                );

        assertTrue(controller.films.isEmpty());
    }

    @SneakyThrows
    @Test
    void createFilmWithWrongDescription() {
        RequestBuilder requestBuilder = request(HttpMethod.POST,"http://localhost:8080/films")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{" +
                        "    \"name\": \"Матрица\"," +
                        "    \"description\": \"Мир Матрицы — это иллюзия, существующая только в бесконечном сне обреченного человечества. Холодный мир будущего, в котором люди — всего лишь батарейки в компьютерных системах.  Холодный мир будущего, в котором люди — всего лишь батарейки в компьютерных системах. \"," +
                        "    \"releaseDate\": \"1999-07-03\"," +
                        "    \"duration\": 136" +
                        "                        }");

        mockMvc.perform(requestBuilder)

                .andExpectAll(
                        MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON),
                        MockMvcResultMatchers.content().json(
                                "{" +
                                "              \"description\": \"Длина описания превышает 200 символов.\"" +
                                "           }")
                );

        assertTrue(controller.films.isEmpty());
    }

    @SneakyThrows
    @Test
    void createFilmWithWrongReleaseDate() {
        RequestBuilder requestBuilder = request(HttpMethod.POST,"http://localhost:8080/films")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{" +
                        "    \"name\": \"Матрица\"," +
                        "    \"description\": \"Научно-фантастический боевик\"," +
                        "    \"releaseDate\": \"1700-07-03\"," +
                        "    \"duration\": 136" +
                        " }");

        mockMvc.perform(requestBuilder)

                .andExpectAll(
                        MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON),
                        MockMvcResultMatchers.content().json(
                                "{" +
                                "              \"releaseDate\": \"Дата релиза раньше 28 декабря 1895 года.\"" +
                                "           }")
                );

        assertTrue(controller.films.isEmpty());
    }

    @SneakyThrows
    @Test
    void createFilmWithWrongDuration() {
        RequestBuilder requestBuilder = request(HttpMethod.POST,"http://localhost:8080/films")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{" +
                        "    \"name\": \"Матрица\"," +
                        "    \"description\": \"Научно-фантастический боевик\"," +
                        "    \"releaseDate\": \"1999-07-03\"," +
                        "    \"duration\": -7" +
                        " }");

        mockMvc.perform(requestBuilder)

                .andExpectAll(
                        MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON),
                        MockMvcResultMatchers.content().json(
                                "{" +
                                "              \"duration\": \"Продолжительность фильма отрицательная.\"" +
                                "           }")
                );

        assertTrue(controller.films.isEmpty());
    }

    @SneakyThrows
    @Test
    void createFilmWithEmptyDescription() {
        RequestBuilder requestBuilder = request(HttpMethod.POST,"http://localhost:8080/films")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{" +
                        "    \"name\": \"Матрица\"," +
                        "    \"description\": \"\"," +
                        "    \"releaseDate\": \"1999-07-03\"," +
                        "    \"duration\": 136" +
                        " }");

        mockMvc.perform(requestBuilder)

                .andExpectAll(
                        MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON),
                        MockMvcResultMatchers.content().json(
                                "{" +
                                        "      \"description\": \"Описание фильма не указано.\"" +
                                        "   }")
                );

        assertTrue(controller.films.isEmpty());
    }

    @SneakyThrows
    @Test
    void succeedUpdateFilm() {
        RequestBuilder requestBuilder = request(HttpMethod.POST,"http://localhost:8080/films")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{" +
                        "    \"name\": \"Матрица\"," +
                        "    \"description\": \"Научно-фантастический боевик\"," +
                        "    \"releaseDate\": \"1999-07-03\"," +
                        "    \"duration\": 136" +
                        " }");

        mockMvc.perform(requestBuilder);

        RequestBuilder nextRequestBuilder = request(HttpMethod.PUT,"http://localhost:8080/films")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{" +
                        "    \"name\": \"Матрица 2\"," +
                        "    \"description\": \"Научно-фантастический боевик\"," +
                        "    \"releaseDate\": \"1999-07-03\"," +
                        "    \"duration\": 184," +
                        "    \"id\": 1" +
                        " }");
        mockMvc.perform(nextRequestBuilder);
        Film film = controller.films.get(1);

        assertEquals("Матрица 2", film.getName());
        assertEquals(184, film.getDuration());
    }

    @SneakyThrows
    @Test
    void updateWithEmptyName() {
        RequestBuilder requestBuilder = request(HttpMethod.PUT,"http://localhost:8080/films")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{" +
                        "    \"name\": \"\"," +
                        "    \"description\": \"Научно-фантастический боевик\"," +
                        "    \"releaseDate\": \"1999-07-03\"," +
                        "    \"duration\": 136" +
                        " }");

        mockMvc.perform(requestBuilder)

                .andExpectAll(
                        MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON),
                        MockMvcResultMatchers.content().json(
                                "{" +
                                        "      \"name\": \"Название пустое.\"" +
                                        "   }")
                );
    }

    @SneakyThrows
    @Test
    void updateWithWrongDescription() {
        RequestBuilder requestBuilder = request(HttpMethod.PUT,"http://localhost:8080/films")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{" +
                        "    \"name\": \"Матрица\"," +
                        "    \"description\": \"Научно-фантастический боевик\"," +
                        "    \"releaseDate\": \"1999-07-03\"," +
                        "    \"duration\": -7" +
                        " }");

        mockMvc.perform(requestBuilder)

                .andExpectAll(
                        MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON),
                        MockMvcResultMatchers.content().json(
                                "{" +
                                        "      \"duration\": \"Продолжительность фильма отрицательная.\"" +
                                        "   }")
                );
    }

    @SneakyThrows
    @Test
    void updateWithWrongReleaseDate() {
        RequestBuilder requestBuilder = request(HttpMethod.PUT,"http://localhost:8080/films")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{" +
                        "    \"name\": \"Матрица\"," +
                        "    \"description\": \"Научно-фантастический боевик\"," +
                        "    \"releaseDate\": \"1700-07-03\"," +
                        "    \"duration\": 136" +
                        " }");

        mockMvc.perform(requestBuilder)

                .andExpectAll(
                        MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON),
                        MockMvcResultMatchers.content().json(
                                "{" +
                                        "      \"releaseDate\": \"Дата релиза раньше 28 декабря 1895 года.\"" +
                                        "   }")
                );
    }

    @SneakyThrows
    @Test
    void updateWithWrongDuration() {
        RequestBuilder requestBuilder = request(HttpMethod.PUT,"http://localhost:8080/films")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{" +
                        "    \"name\": \"Матрица\"," +
                        "    \"description\": \"Научно-фантастический боевик\"," +
                        "    \"releaseDate\": \"1999-07-03\"," +
                        "    \"duration\": -7" +
                        " }");

        mockMvc.perform(requestBuilder)

                .andExpectAll(
                        MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON),
                        MockMvcResultMatchers.content().json(
                                "{" +
                                        "      \"duration\": \"Продолжительность фильма отрицательная.\"" +
                                        "   }")
                );
    }

    @SneakyThrows
    @Test
    void updateWithEmptyDescription() {
        RequestBuilder requestBuilder = request(HttpMethod.PUT,"http://localhost:8080/films")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{" +
                        "    \"name\": \"Матрица\"," +
                        "    \"description\": \"\"," +
                        "    \"releaseDate\": \"1999-07-03\"," +
                        "    \"duration\": 136" +
                        " }");

        mockMvc.perform(requestBuilder)
                .andExpectAll(
                        MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON),
                        MockMvcResultMatchers.content().json(
                                "{" +
                                        "      \"description\": \"Описание фильма не указано.\"" +
                                        "   }")
                );
    }

    @SneakyThrows
    @Test
    void updateWithUnknownFilm() {
        RequestBuilder requestBuilder = request(HttpMethod.PUT,"http://localhost:8080/films")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{" +
                        "    \"name\": \"Матрица\"," +
                        "    \"description\": \"Матрица\"," +
                        "    \"releaseDate\": \"1999-07-03\"," +
                        "    \"duration\": 136" +
                        " }");

        mockMvc.perform(requestBuilder)

                .andExpectAll(
                        status().isInternalServerError(),
                        MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON),
                        MockMvcResultMatchers.content().json("{" +
                                        "\"updateFilm\":\"Фильм Film(" +
                                                         "name=Матрица, " +
                                                         "description=Матрица, " +
                                                         "releaseDate=1999-07-03, " +
                                                         "duration=136, id=0) " +
                                                         "отсутствует в памяти программы.\"" +
                                                         "}")
                );
    }

    @SneakyThrows
    @Test
    void succeedFindAllFilms() {
        RequestBuilder requestBuilder = request(HttpMethod.POST,"http://localhost:8080/films")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{" +
                        "    \"name\": \"Матрица\"," +
                        "    \"description\": \"Научно-фантастический боевик\"," +
                        "    \"releaseDate\": \"1999-07-03\"," +
                        "    \"duration\": 136" +
                        " }");
        mockMvc.perform(requestBuilder);
        RequestBuilder nextRequestBuilder = request(HttpMethod.POST,"http://localhost:8080/films")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{" +
                        "    \"name\": \"Двухсотлетний человек\"," +
                        "    \"description\": \"Начало нового тысячелетия\"," +
                        "    \"releaseDate\": \"2000-04-15\"," +
                        "    \"duration\": 132" +
                        " }");
        mockMvc.perform(nextRequestBuilder);

        RequestBuilder getRequestBuilder = request(HttpMethod.GET,"http://localhost:8080/films")
                .contentType(MediaType.APPLICATION_JSON)
                .content("[{" +
                        "     \"name\": \"Матрица\"," +
                        "     \"description\": \"Научно-фантастический боевик\"," +
                        "     \"releaseDate\": \"1999-07-03\"," +
                        "     \"duration\": 136," +
                        "     \"id\": 1" +
                        "  }," +
                        "  {" +
                        "     \"name\": \"Двухсотлетний человек\"," +
                        "     \"description\": \"Начало нового тысячелетия; происходит прорыв в глобальных технологиях. \" +" +
                        "     \"Люди уже не заводят дома собак и кошек: они покупают себе роботов.\"," +
                        "     \"releaseDate\": \"2000-04-15\"," +
                        "     \"duration\": 132," +
                        "     \"id\": 2" +
                        " }]");
        mockMvc.perform(getRequestBuilder);


        assertEquals(2, controller.getFilms().size());
    }

    @SneakyThrows
    @Test
    void findAllWithEmptyFilmsList() {
        RequestBuilder requestBuilder = request(HttpMethod.GET, "http://localhost:8080/films");
        mockMvc.perform(requestBuilder)

                .andExpectAll(
                        status().isBadRequest(),
                        MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON),
                        MockMvcResultMatchers.content().json("{" +
                                "\"findAll\":\"Список фильмов пуст\"" +
                                "}")
                );
    }
}
