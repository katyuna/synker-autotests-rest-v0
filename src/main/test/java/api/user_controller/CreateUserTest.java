package api.user_controller;

import base.BaseTest;
import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;

public class CreateUserTest extends BaseTest {
    @Feature("")

    @Test
    @Order(1)
    @DisplayName("")
    @Description("r")

    public void testCreateUser() {
        Response response = given()
                .header("Content-Type", "application/json")
                .body("")
                .when()
                .post(baseURI +"/user");
        Allure.step("Check status-code");
        response.then().statusCode(200);


    }

}
