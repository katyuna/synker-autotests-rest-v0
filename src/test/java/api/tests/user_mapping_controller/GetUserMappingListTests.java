package api.tests.user_mapping_controller;

import api.base.ApiBaseTest;
import api.data.dto.UserMappingDto;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;

import java.io.IOException;

@Disabled
public class GetUserMappingListTests extends ApiBaseTest {
    TestDataBuilder testDataBuilder = new TestDataBuilder();

    public void createMapping() throws IOException {
        UserMappingDto userMapping = testDataBuilder.createUserMapping("validMapping", authCookie);
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
    }
        @Feature("Get user mapping")
        @Tag("positive")
        @Order(1)
        @DisplayName("Get user mapping by ID")
        @Description("Test for retrieving user mapping by ID")
        @Test
        public void getUserMapping() throws IOException {

            String createdId = testDataBuilder.create("validMapping", authCookie);

            UserMappingDto retrievedData = testDataBuilder.getMappingFromApi(createdId, authCookie);

            Assertions.assertNotNull(retrievedData, "Retrieved mapping should not be null!");
            Assertions.assertEquals("sourceUser", retrievedData.getSourceUser(), "SourceUser does not match!");
            Assertions.assertEquals("targetUser", retrievedData.getTargetUser(), "TargetUser does not match!");
            Assertions.assertEquals("Test comment", retrievedData.getComment(), "Comment does not match!");
        testDataBuilder.deleteMapping(createdId, authCookie);
    }
}
