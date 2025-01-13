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

public class CreateUserTest extends BaseTest {

    String requestBody = "";

    @Feature("Create user")

    @Test
    @Tag("positive")
    @Order(1)
    @DisplayName("Create user ROLE_ADMIN")
    @Description("Create user with role ROLE_ADMIN")

    public void createUserWithRoleAdmin() {

        Allure.step("Send request");
        Response response = given()
                .header("Content-Type", "application/json")
                .header("Cookie", "JSESSIONID=" + authCookie)
                .body(requestBody)
                .when()
                .post(baseURI + "/api/v1/user/admin");
        Allure.step("Verify status-code is 200");
        response.then().statusCode(200);
    }
}
