package api.user_mapping_controller;

import base.BaseTest;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.restassured.RestAssured;
import org.junit.jupiter.api.*;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@Disabled
public class GetUserMappingListTests extends BaseTest {

    @Feature("Get user mapping")
    @Tag("positive")
    @Order(1)
    @DisplayName("Get user mapping")
    @Description("Test for retrieving user mapping")
    @Test
    public void getUserMapping() {
        given()
                .cookie("JSESSIONID", authCookie)
                .queryParam("page", 1)
                .queryParam("size", 100)
                .queryParam("sort", "[\"id\"]")
                .when()
                .get(RestAssured.baseURI + "/api/v1" + "/mapping/user")
                .then()
                .statusCode(200)
                .body("size()", equalTo(1));

        System.out.println("Successfully retrieved user mapping.");
    }
}
