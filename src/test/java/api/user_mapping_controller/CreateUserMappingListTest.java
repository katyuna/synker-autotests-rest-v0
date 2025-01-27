package api.user_mapping_controller;

import base.BaseTest;
import data.dto.UserMappingDto;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import utils.RestClient;

import java.io.IOException;

public class CreateUserMappingListTest extends BaseTest {

    private final RestClient restClient = new RestClient(RestAssured.baseURI);
    private final TestDataBuilder testDataBuilder = new TestDataBuilder();


    @Feature("Create mapping user")
    @Tag("positive")
    @Order(1)
    @DisplayName("Create mapping user sourceUser and targetUser")
    @Description("Create mapping user sourceUser and targetUser")
    @Test
    public void createMapping() throws IOException {
        UserMappingDto userMapping =  testDataBuilder.createUserMapping("validMapping", authCookie);
        Response response = restClient.post("/api/v1/mapping/user", userMapping, authCookie);

        response.then()
                .log().all()
                .statusCode(200);

        String createdId = response.jsonPath().getString("id");
        userMapping.setId(createdId);
        System.out.println("Created ID: " + userMapping.getId());
        UserMappingDto actualData = testDataBuilder.getMappingFromApi(createdId, authCookie);
        assert actualData != null : "Данные из API отсутствуют";
        assert userMapping.getSourceUser().equals(actualData.getSourceUser()) : "Поле sourceUser не совпадает";
        assert userMapping.getTargetUser().equals(actualData.getTargetUser()) : "Поле targetUser не совпадает";
        assert userMapping.getComment().equals(actualData.getComment()) : "Поле comment не совпадает";

        testDataBuilder.deleteMapping(createdId,authCookie);
    }

    @Feature("Create mapping user")
    @Tag("negative")
    @Order(2)
    @DisplayName("Negative: Missing required fields")
    @Test
    @Description("Attempt to create a user mapping without required fields")
    @Disabled
    public void testCreateMappingWithMissingSourceUserFields() throws IOException {
        String userMappingDto = testDataBuilder.loadTestData("missingSourceUserFieldsMapping");
        Response response = restClient.post("/api/v1/mapping/user/", userMappingDto, authCookie);
        response.then()
                .log().all()
                .statusCode(400);
    }

    @Feature("Create mapping user")
    @Tag("negative")
    @Order(3)
    @DisplayName("Negative: Missing required fields")
    @Test
    @Description("Attempt to create a user mapping without required fields")
    @Disabled
    public void testCreateMappingWithMissingTargetUserFields() throws IOException {
        String userMappingDto = testDataBuilder.loadTestData("missingTargetUserFieldsMapping");
        Response response = restClient.post("/api/v1/mapping/user/", userMappingDto, authCookie);
        response.then()
                .log().all()
                .statusCode(400);
    }

}
