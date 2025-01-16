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
import utils.AssertionClient;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;

public class CreateUserTest extends BaseTest {

    String requestBody = "";

    @Feature("Create user")

    @Test
    @Tag("positive")
    @Order(1)
    @DisplayName("Create user with role ROLE_ADMIN")
    @Description("Create user with role ROLE_ADMIN")

    public void createUserWithRoleAdmin() {
        System.out.println("-> Start test: Sending POST-create-user request.");
        Allure.step("Send create user request");
        Response response = restClient.post("/api/v1/user/admin", requestBody, authCookie);
        Allure.step("Verify status-code is 200.");
        AssertionClient.checkStatusCode(response, 200);
        Allure.step("Verify that response body not null.");
        AssertionClient.checkResponseBodyIsNotNull(response);
    }
}
