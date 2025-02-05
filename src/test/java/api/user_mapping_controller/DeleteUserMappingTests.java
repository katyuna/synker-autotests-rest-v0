package api.user_mapping_controller;

import base.BaseTest;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import utils.RestClient;

import java.io.IOException;

public class DeleteUserMappingTests extends BaseTest {
    private final RestClient restClient = new RestClient(RestAssured.baseURI);
    private final TestDataBuilder testDataBuilder = new TestDataBuilder();

    @Feature("Delete mapping user")
    @Tag("positive")
    @Order(1)
    @DisplayName("Delete mapping user by ID")
    @Description("delete mapping user by ID, and confirm deletion")
    @Test
    public void delete() throws IOException {
        String mappingId = testDataBuilder.create("validMappingDelete", authCookie);
        int getCreate = testDataBuilder.getStatusCode(mappingId, authCookie);
        if (getCreate != 200) {
            throw new IllegalArgumentException("Create method error");
        }
        Response confirmResponse = restClient.delete("/api/v1/mapping/user/" + mappingId, authCookie);

        System.out.println("Confirm delete status: " + confirmResponse.statusCode());
        System.out.println("Confirm delete body: " + confirmResponse.body().asString());

        Assertions.assertEquals(400, testDataBuilder.getStatusCode(mappingId, authCookie), "Expected status code 400 after deletion!");
    }
}
