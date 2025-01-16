package api.user_controller;

import base.BaseTest;
import data.TestDataGenerator;
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
     * Get test data: list of user ids
     */
    public static List<Integer> getUsersIds() {
        int size = 10; // User amount per page
        int page = 0;  // Page number
        System.out.println("-> Getting test parameters -> user ids with /api/v1/user.");
        Response response = given()
                .header("Content-Type", "application/json")
                .header("Cookie", "JSESSIONID=" + authCookie)
                .queryParam("size", size)
                .queryParam("page", page)
                .when()
                .get(baseURI + "/api/v1/user");

        JsonPath jsonPath = response.jsonPath();
        List<Integer> userIdsList = jsonPath.getList("id", Integer.class);
        System.out.println("-> Test parameters have gotten, user ids list: " + userIdsList + ".");
        return userIdsList;
    }

    @Feature("Get user by Id")

    @ParameterizedTest
    @MethodSource("getUsersIds")
    @Tag("positive")
    @Order(1)
    @DisplayName("Get users by Ids correctly.")
    @Description("Get user by Id parametrized test with existing Ids. User ids got from /api/v1/user.")
    public void getUserByIdWithExistingIds(int id) {
        System.out.println("-> Start parametrized test: Get user by Id with id = " + id + ".");
        Allure.step("Sending GET-user-by-id request with id = " + id + ".");
        Response response = given()
                .header("Content-Type", "application/json")
                .header("Cookie", "JSESSIONID=" + authCookie)
                //.log().all()
                .when()
                .get(baseURI + "/api/v1/user/" + id);

        System.out.println("--- Checking response status code.");
        Allure.step("Verify status-code is 200.");
        response.then().statusCode(200);
        System.out.println("--- Response code: " + response.getStatusCode());
    }

    @Test
    @Tag("negative")
    @Order(2)
    @DisplayName("Get users by Ids with not existing Id.")
    @Description("Get user by Id test with not existing Id. User id got from generator.")
    public void getUserByIdWithNotExistingId() {
        Integer id = TestDataGenerator.generateId();
        System.out.println("-> Start test: Get user by Id with not existing id = " + id + ".");
        Allure.step("Sending GET-user-by-id request with not existing id = " + id + ".");
        Response response = given()
                .header("Content-Type", "application/json")
                .header("Cookie", "JSESSIONID=" + authCookie)
                //.log().all()
                .when()
                .get(baseURI + "/api/v1/user/" + id);
        System.out.println("--- Checking response status code.");
        Allure.step("Verify status-code is 400.");
        response.then().statusCode(400);
        System.out.println("--- Response code: " + response.getStatusCode());
    }
}