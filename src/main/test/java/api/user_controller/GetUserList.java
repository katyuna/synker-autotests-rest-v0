package api.user_controller;

import base.BaseTest;
import io.qameta.allure.Allure;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;

public class GetUserList extends BaseTest {

    @Test
    public void getUserList(){
        int size = 10; // Количество пользователей на странице
        int page = 1;  // Номер страницы
        System.out.println("--- Start test: Get user list with parameters: size = " + size +", page = " + page+".");
        Allure.step("Sending get user list request.");
        Response response = given()
                .header("Content-Type", "application/json")
                .header("Cookie", "JSESSIONID=" + authCookie)
                .queryParam("size", size)
                .queryParam("page", page)
                .log().all() // Логируем запрос
                .when()
                .get(baseURI + "/api/v1/user");

        System.out.println("--- Response Body: " + response.getBody().asString());
        System.out.println("--- Checking response status code.");
        Allure.step("Checking response status code.");
        response.then().statusCode(200);
        System.out.println("--- Response code: " + response.getStatusCode());

    }
}
