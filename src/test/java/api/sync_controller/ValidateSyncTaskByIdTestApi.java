package api.sync_controller;

import base.ApiBaseTest;
import data.TestDataGenerator;
import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import utils.AssertionClient;
import utils.RestClient;

import java.util.List;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;

public class ValidateSyncTaskByIdTestApi extends ApiBaseTest {

    private final String path = "/api/v1/sync/validate/";

    private final RestClient restClient = new RestClient(RestAssured.baseURI);

    /**
     * Get test data: get list of Sync task ids
     */
    public static List<Integer> getSyncTaskIds() {
        int size = 3; // Sync task amount per page
        int page = 0;  // Page number

        Response response = given()
                .header("Content-Type", "application/json")
                .header("Cookie", "JSESSIONID=" + authCookie)
                .queryParam("size", size)
                .queryParam("page", page)
                .log().all()
                .when()
                .get(baseURI + "/api/v1/sync");

        JsonPath jsonPath = response.jsonPath();
        List<Integer> syncTaskIdsList = jsonPath.getList("id", Integer.class);

        return syncTaskIdsList;
    }

    @Feature("Validate sync task by id")

    @ParameterizedTest
    @MethodSource("getSyncTaskIds")
    @Tag("positive")
    @Order(1)
    @DisplayName("Validate Sync Task by id successfully")
    @Description("Validate massage body for synchronization task by id with status code 200")
    public void validateSyncTaskByIdSuccessfully(int syncTaskId) {
        System.out.println("--- Start parametrized test: Validate Sync Task by id with id = " + syncTaskId);

        String bodyMessage = "Found tasks by request";

        Allure.step("Sending validate sync task by id request.");
        Response response = restClient.getNoParams(path + syncTaskId, authCookie);

        System.out.println("--- Response Body: " + response.getBody().prettyPrint());

        Allure.step("Verify status-code is 200.");
        AssertionClient.checkStatusCode(response, 200);

        Allure.step("Verify that response body not empty and is: " + bodyMessage);
        AssertionClient.checkResponseBodyMessage(response, bodyMessage);
    }

    @Test
    @Tag("negative")
    @Order(2)
    @DisplayName("Validate massage body for sync task by wrong id")
    @Description("Validate massage body invalid for synchronization task by wrong id")
    public void verifyStatusCodeForWrongSyncTaskId() {
        System.out.println("--- Start test: Validate massage body for sync task by wrong id");

        String bodyMessageInvalid = "Invalid request filter";

        Allure.step("Sending validate sync task by wrong id request.");
        Response response = restClient.getNoParams(path + TestDataGenerator.generateId(), authCookie);

        System.out.println("--- Response Body: " + response.getBody().prettyPrint());

        Allure.step("Verify status-code is 200.");
        AssertionClient.checkStatusCode(response, 200);

        Allure.step("Verify that response body not empty and is: " + bodyMessageInvalid);
        AssertionClient.checkResponseBodyMessage(response, bodyMessageInvalid);
    }
}
