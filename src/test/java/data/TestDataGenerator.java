package data;

import io.restassured.response.Response;

import java.lang.constant.Constable;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;

public class TestDataGenerator {

    /**
     * Генерация уникального id на основе текущего времени - возвращает текущие миллисекунды
     */
    public static Integer generateId() {
        return (int) System.currentTimeMillis();
    }

    /**
     * Метод авторизации
     * @param login
     * @param password
     * @return authCookie
     */
    public static String auth(String login, String password) {
        String requestBody = "{ \"username\": \"" + login + "\", \"password\": \"" + password + "\" }";
        Response response = given()
                .header("Content-Type", "application/json")
                .body(requestBody) // Передаем тело запроса
                .when()
                .post(baseURI + "/api/v1/auth/login");

        if (response.statusCode() != 200) {
            throw new RuntimeException("Authorization failed with status code: " + response.statusCode());
        }

        String authCookie = response.getCookie("JSESSIONID"); // Укажите имя cookie из ответа
        if (authCookie == null) {
            throw new RuntimeException("No auth cookie found in the response!");
        }
        return authCookie;
    }

    /* создапть пользователя
    Получить куки
    где-то их хранить, чтобю доступ ьыл всем
    используя куки
    - создать трекет 1 и трекер 2
    - созадть проект 1 и проект 2
    - создать юзер 1 и юзер 2

    и тд и тп
    */

}
