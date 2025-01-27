package api.user_controller;

import base.BaseTest;
import data.TestDataGenerator;
import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import utils.AssertionClient;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CheckAuthTest extends BaseTest {

    @Feature("Check Authorization")

    /**
     * Test method
     */
    @Test
    @Disabled
    @Tag("positive")
    @Order(1)
    @DisplayName("Check authorization when user logged in.")
    @Description("Send GET request to fetch the user logged in")
    public void checkAuthorizationWhenUserLoggedInTest (){
        System.out.println("-> Start test: Check authorization when user logged in.");

        Response response = given()
                .header("Content-Type", "application/json")
                .header("Cookie", "JSESSIONID=" + authCookie)
                .log().all()
                .when()
                .get(baseURI + "/api/v1/user/checkAuth");
        System.out.println("Response body: " + response.getBody().asString());
        // System.out.println(authCookie);
        //Response response = restClient.getNoParams("/api/v1/user/checkAuth", authCookie);
        Allure.step("Verify status-code is 200.");
        AssertionClient.checkStatusCode(response, 200);
    }
}
