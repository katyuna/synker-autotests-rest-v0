package api.user_controller;

import base.BaseTest;
import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;

public class GetUserListTest extends BaseTest {

    @Feature("Get user list")

    @Test
    @Tag("positive")
    @Order(1)
    @DisplayName("Get user list")
    @Description("Get user list")
    public void getUserList(){
        int size = 10; // User amount per page
        int page = 0;  // Page number
        System.out.println("--- Start test: Get user list with parameters: size = " + size +", page = " + page+".");
        Allure.step("Sending get user list request.");
        Response response = given()
                .header("Content-Type", "application/json")
                .header("Cookie", "JSESSIONID=" + authCookie)
                .queryParam("size", size)
                .queryParam("page", page)
                .log().all()
                .when()
                .get(baseURI + "/api/v1/user");

        System.out.println("--- Response Body: " + response.getBody().asString());
        System.out.println("--- Checking response status code.");
        Allure.step("Verify status-code is 200.");
        response.then().statusCode(200);
        System.out.println("--- Response code: " + response.getStatusCode());
    }
}
