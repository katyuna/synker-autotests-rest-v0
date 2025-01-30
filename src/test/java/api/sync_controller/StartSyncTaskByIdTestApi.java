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
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import utils.AssertionClient;
import utils.RestClient;

import java.util.List;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class StartSyncTaskByIdTestApi extends ApiBaseTest {

    private final String path = "/api/v1/sync/";
    private final RestClient restClient = new RestClient(RestAssured.baseURI);

    /**
     * Get test data: get list of Sync task ids
     */
    public static List<Integer> getSyncTaskIds() {
        int size = 3; // Sync task amount per page
        int page = 1;  // Page number

        System.out.println("-> Getting test parameters -> sync task ids with /api/v1/sync.");
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

        System.out.println("--- Test parameters have gotten, sync task ids list: " + syncTaskIdsList + ".");
        return syncTaskIdsList;
    }

    @Feature("Start sync task by id")

    @ParameterizedTest
    @MethodSource("getSyncTaskIds")
    @Tag("positive")
    @Order(1)
    @DisplayName("Start Sync Task by id successfully")
    @Description("Start synchronization task by id with status code 200")
    public void startSyncTaskByIdSuccessfully(int syncTaskId) {
        System.out.println("--- Start test: Start Sync Task by id successfully");

        Allure.step("Sending post sync task by id request.");
        Response response = restClient.postNoBody(path + syncTaskId, authCookie);

        System.out.println("--- Response Body: " + response.getBody().prettyPrint());

        Allure.step("Verify status-code is 200.");
        AssertionClient.checkStatusCode(response, 200);

        System.out.println("--- Checking response body.");
        Allure.step("Verify that response body not empty and as \"Sync task has started\".");
        response.then().assertThat().body(not(emptyOrNullString()))
                .and().body(equalTo("Sync task has started"));
    }

    @Test
    @Tag("negative")
    @Order(2)
    @DisplayName("Sync task not found if start Sync Task by wrong id")
    @Description("Sync task not found if start synchronization task by wrong id")
    public void verifyStatusCodeForStartSyncTaskWithWrongId() {
        System.out.println("--- Start test: Sync task not found if start Sync Task by wrong id");

        int wrongSyncTaskId = TestDataGenerator.generateId();

        Allure.step("Sending post sync task with wrong id request.");
        Response response = restClient.postNoBody(path + wrongSyncTaskId, authCookie);

        System.out.println("--- Response Body: " + response.getBody().prettyPrint());

        Allure.step("Verify status-code is 200.");
        AssertionClient.checkStatusCode(response, 200);

        System.out.println("--- Checking response body.");
        Allure.step("Verify that response body not empty and as \"Sync task with id "
                + wrongSyncTaskId + " not found\".");
        response.then().assertThat().body(not(emptyOrNullString()))
                .and().body(equalTo("Sync task with id " + wrongSyncTaskId + " not found"));
    }

    @ParameterizedTest
    @MethodSource("getSyncTaskIds")
    @Tag("negative")
    @Order(3)
    @DisplayName("Get 403 for start sync task by id request")
    @Description("Get 403 for start sync task by id request when cookie is wrong")
    public void getAccessForbiddenWithWrongCookie(int syncTaskId) {
        System.out.println("--- Start test: Get 403 with start sync task by id request");

        Allure.step("Sending start sync task by id request with wrong cookie.");
        Response response = restClient.postNoBody(path + syncTaskId, "8C37CE51E75FFEFA9A29B71BEBA34072");

        System.out.println("--- Response Body: " + response.getBody().prettyPrint());

        Allure.step("Verify status-code is 403.");
        AssertionClient.checkStatusCode(response, 403);
    }

    @ParameterizedTest
    @CsvSource({
            "application/xml",
            "application/soap+xml",
            "application/javascript",
            "application/graphql",
            "application/pdf",
    })
    @Tag("negative")
    @Order(4)
    @DisplayName("Start sync task by id with unsupported content-type header")
    @Description("Get 500 for start sync task by id request when unsupported content-type header")
    public void getErrorWhenUnsupportedContentTypeInRequest(String contentType) {
        System.out.println("--- Start test: Start sync task by id with unsupported content-type header");

        int syncTaskId = getSyncTaskIds().get(0);

        Allure.step("Sending start sync task by id request with with unsupported content-type header.");
        Response response = restClient.postNoBody(path + syncTaskId, authCookie, contentType);

        System.out.println("--- Response Body: " + response.getBody().prettyPrint());

        Allure.step("Verify status-code is 500.");
        AssertionClient.checkStatusCode(response, 500);

        Allure.step("Verify response body error is \"Content-Type '" + contentType + ";charset=ISO-8859-1' is not supported");
        AssertionClient.checkUnsupportedContentTypeError(response, contentType, path + syncTaskId);
    }
}
