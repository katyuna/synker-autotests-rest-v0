package api.user_mapping_controller;

import base.BaseTest;
import data.dto.UserMappingDto;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import utils.RestClient;

import static io.restassured.RestAssured.given;

public class CreateUserMappingListTest extends BaseTest {
    private final RestClient restClient = new RestClient(RestAssured.baseURI);
    private static String sessionCookie;

    @Feature("Create mapping user")
    @Tag("positive")
    @Order(1)
    @DisplayName("Create mapping user sourceUser and targetUser")
    @Description("Create mapping user sourceUser and targetUser")
    @Test
    public void createMapping() {
        UserMappingDto userMappingDto = new UserMappingDto();
        userMappingDto.setId("0");
        userMappingDto.setSourceUser("sourceUser");
        userMappingDto.setTargetUser("targetUser");
        userMappingDto.setComment("Test comment");
        userMappingDto.setCreated("2024-12-18T08:20:51.797Z");
        userMappingDto.setCreatedBy("tester");

        Response response = restClient.post("/api/v1/mapping/user", userMappingDto, null, authCookie);

        response.then()
                .statusCode(200)
                .extract()
                .response();
    }
}
