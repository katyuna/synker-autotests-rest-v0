package api.user_controller;

import base.BaseTest;
import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;

public class GetUserByIdTest extends BaseTest {

    /**
     * Получить список id пользователей
     */
    public static List<Integer> getUsersIds() {
        int size = 10; // User amount per page
        int page = 0;  // Page number
        Response response = given()
                .header("Content-Type", "application/json")
                .header("Cookie", "JSESSIONID=" + authCookie)
                .queryParam("size", size)
                .queryParam("page", page)
                .log().all()
                .when()
                .get(baseURI + "/api/v1/user");

        JsonPath jsonPath = response.jsonPath();
        List<Integer> userIdsList = jsonPath.getList("id", Integer.class);

        return userIdsList;
    }


    @Feature("Get user by Id")

    @ParameterizedTest
    @MethodSource("getUsersIds")
    @Tag("positive")
    @Order(1)
    @DisplayName("Get user by Id")
    @Description("Get user by Id")
    public void getUserById(int id) {
        System.out.println("--- Start parametrized test: Get user by Id with id = " + id);
        Allure.step("Sending get user by id request.");
        Response response = given()
                .header("Content-Type", "application/json")
                .header("Cookie", "JSESSIONID=" + authCookie)
                .log().all()
                .when()
                .get(baseURI + "/api/v1/user/" + id);

        System.out.println("--- Checking response status code.");
        Allure.step("Verify status-code is 200.");
        response.then().statusCode(200);
        System.out.println("--- Response code: " + response.getStatusCode());
    }
}
