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

public class UpdateUserMappingTest extends BaseTest {

    private final RestClient restClient = new RestClient(RestAssured.baseURI);
    private final TestDataBuilder testDataBuilder = new TestDataBuilder();

    @Feature("Update mapping user")
    @Tag("positive")
    @Order(1)
    @DisplayName("Update mapping user sourceUser and targetUser")
    @Description("Update existing mapping user sourceUser and targetUser")
    @Test
    public void updateMapping() throws IOException {
        String createdId = testDataBuilder.create("validMapping", authCookie);
        UserMappingDto existingData = testDataBuilder.getMappingFromApi(createdId, authCookie);
        Assertions.assertNotNull(existingData, "Created mapping should not be null!");

        UserMappingDto updateData = testDataBuilder.update("validMappingUpdate", createdId, authCookie);
        updateData.setId(createdId);
        updateData.setCreated(existingData.getCreated());
        updateData.setCreatedBy(existingData.getCreatedBy());

        Response updateResponse = restClient.put("/api/v1/mapping/user", updateData, authCookie);
        updateResponse.then()
                .log().all()
                .statusCode(200);

        UserMappingDto updatedData = testDataBuilder.getMappingFromApi(createdId, authCookie);
        Assertions.assertNotNull(updatedData, "Updated mapping should not be null!");

        Assertions.assertEquals("UpdatedSourceUser", updatedData.getSourceUser(), "SourceUser did not update correctly!");
        Assertions.assertEquals("UpdatedTargetUser", updatedData.getTargetUser(), "TargetUser did not update correctly!");
        Assertions.assertEquals("Updated comment", updatedData.getComment(), "Comment did not update correctly!");

        testDataBuilder.deleteMapping(createdId, authCookie);
    }
}
