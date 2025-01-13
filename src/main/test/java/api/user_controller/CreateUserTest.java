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
    String requestBody = "{\n" +
            "  \"id\": 666,\n" +
            "  \"username\": \"katyuna\",\n" +
            "  \"password\": \"changeme\",\n" +
            "  \"passwordConfirm\": \"changeme\",\n" +
            "  \"roles\": [\n" +
            "    {\n" +
            "      \"id\": 0,\n" +
            "      \"name\": \"ROLE_ADMIN\",\n" +
            "      \"users\": [\n" +
            "        \"string\"\n" +
            "      ],\n" +
            "      \"authority\": \"string\"\n" +
            "    }\n" +
            "  ],\n" +
            "  \"enabled\": true,\n" +
            "  \"credentialsNonExpired\": true,\n" +
            "  \"accountNonExpired\": true,\n" +
            "  \"accountNonLocked\": true,\n" +
            "  \"authorities\": [\n" +
            "    {\n" +
            "      \"authority\": \"string\"\n" +
            "    }\n" +
            "  ]\n" +
            "}";

    @Feature("Create user")

    @Tag("positive")
    @Test
    @Order(1)
    @DisplayName("Create user ROLE_ADMIN")
    @Description("Create user with role ROLE_ADMIN")

       public void createUserWithRoleAdmin() {

        Allure.step("Send request");
        Response response = given()
                .header("Content-Type", "application/json")
                .body(requestBody)
                .when()
                .post(baseURI +"/api/v1/user/admin");
        Allure.step("Check status-code");
        response.then().statusCode(400);
    }
}
