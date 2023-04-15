package ru.yandex.practicum.filmorate.controller;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class UserControllerTest {
    @Autowired
    MockMvc mockMvc;

    @SneakyThrows
    @Test
    void succeedCreateUser() {
        RequestBuilder requestBuilder = request(HttpMethod.POST,"http://localhost:8080/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{" +
                        "    \"login\": \"nicky\"," +
                        "    \"name\": \"Nick\"," +
                        "    \"email\": \"mail@mail.ru\"," +
                        "    \"birthday\": \"2000-08-20\"" +
                        " }");

        mockMvc.perform(requestBuilder)

                .andExpectAll(
                        MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON),
                        MockMvcResultMatchers.content().json(
                                "{" +
                                "              \"login\": \"nicky\"," +
                                "              \"name\": \"Nick\"," +
                                "              \"email\": \"mail@mail.ru\"," +
                                "              \"birthday\": \"2000-08-20\"," +
                                "              \"id\": 1" +
                                "            }")
                );
    }

    @SneakyThrows
    @Test
    void createUserWithWrongEmail() {
        RequestBuilder requestBuilder = request(HttpMethod.POST,"http://localhost:8080/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{" +
                        "    \"login\": \"nicky\"," +
                        "    \"name\": \"Nick\"," +
                        "    \"email\": \"mail.mail.ru\"," +
                        "    \"birthday\": \"2000-08-20\"" +
                        " }");

        mockMvc.perform(requestBuilder)

                .andExpectAll(
                        status().isBadRequest(),
                        MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON),
                        MockMvcResultMatchers.content().json(
                                "{" +
                                "              \"email\":\"Неверный формат почтового адреса.\"" +
                                "           }")
                );
    }

    @SneakyThrows
    @Test
    void createUserWithWrongLogin() {
        RequestBuilder requestBuilder = request(HttpMethod.POST,"http://localhost:8080/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{" +
                        "    \"login\": \"nicky nicky\"," +
                        "    \"name\": \"Nick\"," +
                        "    \"email\": \"mail@mail.ru\"," +
                        "    \"birthday\": \"2000-08-20\"" +
                        " }");

        mockMvc.perform(requestBuilder)

                .andExpectAll(
                        status().isBadRequest(),
                        MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON),
                        MockMvcResultMatchers.content().json(
                                "{" +
                                        "      \"login\":\"Логин содержит пробелы.\"" +
                                        "   }")
                );
    }

    @SneakyThrows
    @Test
    void createUserWithEmptyName() {
        RequestBuilder requestBuilder = request(HttpMethod.POST,"http://localhost:8080/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{" +
                        "    \"login\": \"mickyy\"," +
                        "    \"name\": \"\"," +
                        "    \"email\": \"email@mail.ru\"," +
                        "    \"birthday\": \"2000-08-20\"" +
                        " }");

        mockMvc.perform(requestBuilder)

                .andExpectAll(
                        MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON),
                        MockMvcResultMatchers.content().json(
                                "{" +
                                "              \"login\": \"mickyy\"," +
                                "              \"name\": \"mickyy\"," +
                                "              \"email\": \"email@mail.ru\"," +
                                "              \"birthday\": \"2000-08-20\"" +
                                "            }")
                );
    }

    @SneakyThrows
    @Test
    void createUserWithWrongBirthday() {
        RequestBuilder requestBuilder = request(HttpMethod.POST,"http://localhost:8080/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{" +
                        "    \"login\": \"nicky\"," +
                        "    \"name\": \"Nick\"," +
                        "    \"email\": \"mail@mail.ru\"," +
                        "    \"birthday\": \"2090-08-20\"" +
                        " }");

        mockMvc.perform(requestBuilder)

                .andExpectAll(
                        status().isBadRequest(),
                        MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON),
                        MockMvcResultMatchers.content().json(
                                "{" +
                                        "      \"birthday\":\"Дата рождения еще не наступила.\"" +
                                        "   }")
                );
    }

    @SneakyThrows
    @Test
    void createUserWithEmptyEmail() {
        RequestBuilder requestBuilder = request(HttpMethod.POST,"http://localhost:8080/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{" +
                        "    \"login\": \"nicky\"," +
                        "    \"name\": \"Nick\"," +
                        "    \"email\": \"\"," +
                        "    \"birthday\": \"2000-08-20\"" +
                        " }");

        mockMvc.perform(requestBuilder)

                .andExpectAll(
                        status().isBadRequest(),
                        MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON),
                        MockMvcResultMatchers.content().json(
                                "{" +
                                        "      \"email\":\"Почтовый адрес пустой.\"" +
                                        "   }")
                );
    }

    @SneakyThrows
    @Test
    void succeedUpdateUser() {
        RequestBuilder requestBuilder = request(HttpMethod.POST,"http://localhost:8080/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{" +
                        "    \"login\": \"nicky\"," +
                        "    \"name\": \"Nick\"," +
                        "    \"email\": \"mail@mail.ru\"," +
                        "    \"birthday\": \"2000-08-20\"" +
                        " }");
        RequestBuilder nextRequestBuilder = request(HttpMethod.PUT,"http://localhost:8080/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{" +
                        "    \"login\": \"nicky\"," +
                        "    \"name\": \"Alex\"," +
                        "    \"email\": \"mail@mail.ru\"," +
                        "    \"birthday\": \"2000-08-20\"," +
                        "    \"id\": 1" +
                        " }");

        mockMvc.perform(requestBuilder);
        mockMvc.perform(nextRequestBuilder);
    }

    @SneakyThrows
    @Test
    void updateUnknownUser() {
        RequestBuilder requestBuilder = request(HttpMethod.PUT,"http://localhost:8080/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{" +
                        "    \"login\": \"Sasha\"," +
                        "    \"name\": \"Alex\"," +
                        "    \"email\": \"alex@mail.ru\"," +
                        "    \"birthday\": \"2020-08-20\"," +
                        "    \"id\": 12" +
                        " }");

        mockMvc.perform(requestBuilder)

                .andExpectAll(
                        status().isNotFound(),
                        MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON)
                );
    }

    @SneakyThrows
    @Test
    void updateUserWithEmptyLogin() {
        RequestBuilder requestBuilder = request(HttpMethod.PUT,"http://localhost:8080/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{" +
                        "    \"login\": \"\"," +
                        "    \"name\": \"Nick\"," +
                        "    \"email\": \"mail@mail.ru\"," +
                        "    \"birthday\": \"2000-08-20\"," +
                        "    \"id\": 1" +
                        " }");

        mockMvc.perform(requestBuilder)
                .andExpectAll(
                        status().isBadRequest(),
                        MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON),
                        MockMvcResultMatchers.content().json(
                                "{" +
                                "              \"login\":\"Логин пустой.\"" +
                                "           }")
                );
    }

    @SneakyThrows
    @Test
    void updateUserWithWrongEmail() {
        RequestBuilder requestBuilder = request(HttpMethod.PUT,"http://localhost:8080/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{" +
                        "    \"login\": \"nicky nicky\"," +
                        "    \"name\": \"Nick\"," +
                        "    \"email\": \"mail.mail.ru\"," +
                        "    \"birthday\": \"2000-08-20\"" +
                        " }");

        mockMvc.perform(requestBuilder)

                .andExpectAll(
                        status().isBadRequest(),
                        MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON),
                        MockMvcResultMatchers.content().json(
                                "{" +
                                "              \"email\":\"Неверный формат почтового адреса.\"" +
                                "           }")
                );
    }

    @SneakyThrows
    @Test
    void updateUserWithWrongLogin() {
        RequestBuilder requestBuilder = request(HttpMethod.PUT,"http://localhost:8080/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{" +
                        "    \"login\": \"nicky nicky\"," +
                        "    \"name\": \"Nick\"," +
                        "    \"email\": \"mail@mail.ru\"," +
                        "    \"birthday\": \"2000-08-20\"" +
                        " }");

        mockMvc.perform(requestBuilder)

                .andExpectAll(
                        status().isBadRequest(),
                        MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON),
                        MockMvcResultMatchers.content().json(
                                "{" +
                                        "      \"login\":\"Логин содержит пробелы.\"" +
                                        "   }")
                );
    }

    @SneakyThrows
    @Test
    void updateUserWithWrongBirthday() {
        RequestBuilder requestBuilder = request(HttpMethod.POST,"http://localhost:8080/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{" +
                        "    \"login\": \"nicky nicky\"," +
                        "    \"name\": \"Nick\"," +
                        "    \"email\": \"mail@mail.ru\"," +
                        "    \"birthday\": \"2070-08-20\"" +
                        " }");

        mockMvc.perform(requestBuilder)

                .andExpectAll(
                        status().isBadRequest(),
                        MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON),
                        MockMvcResultMatchers.content().json(
                                "{" +
                                        "      \"birthday\":\"Дата рождения еще не наступила.\"" +
                                        "   }")
                );
    }

    @SneakyThrows
    @Test
    void succeedFindAllUsers() {
        RequestBuilder requestBuilder = request(HttpMethod.POST,"http://localhost:8080/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{" +
                        "    \"login\": \"nicky\"," +
                        "    \"name\": \"Nick\"," +
                        "    \"email\": \"mail@mail.ru\"," +
                        "    \"birthday\": \"2000-08-20\"" +
                        " }");

        mockMvc.perform(requestBuilder);

        RequestBuilder nextRequestBuilder = request(HttpMethod.GET,"http://localhost:8080/users");
        mockMvc.perform(nextRequestBuilder)

                .andExpectAll(
                        MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON),
                        MockMvcResultMatchers.content().json(
                                "[{" +
                                        "       \"login\": \"nicky\"," +
                                        "       \"name\": \"Nick\"," +
                                        "       \"email\": \"mail@mail.ru\"," +
                                        "       \"birthday\": \"2000-08-20\"," +
                                        "       \"id\": 1" +
                                        "   }]")
                );
    }

    @SneakyThrows
    @Test
    void findAllWhenUsersListIsEmpty() {
        RequestBuilder requestBuilder = request(HttpMethod.GET,"http://localhost:8080/users");

        mockMvc.perform(requestBuilder)

                .andExpectAll(
                        MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON),
                        MockMvcResultMatchers.content().json("[]")
                );
    }
}
