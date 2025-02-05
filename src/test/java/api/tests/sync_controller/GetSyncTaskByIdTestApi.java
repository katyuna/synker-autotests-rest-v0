package api.tests.sync_controller;

import api.base.ApiBaseTest;
import api.data.TestDataGenerator;
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
import api.utils.AssertionClient;
import api.utils.RestClient;

import java.util.List;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertAll;

public class GetSyncTaskByIdTestApi extends ApiBaseTest {

    private static final int syncTaskId = getSyncTaskIds().get(0);
    private static final String taskName = getSyncTaskNames().get(0);
    private final String path = "/api/v1/sync/";

    private final RestClient restClient = new RestClient(RestAssured.baseURI);

    /**
     * Get test data: get list of Sync task ids
     */
    public static List<Integer> getSyncTaskIds() {
        int size = 3; // Sync task amount per page
        int page = 1;  // Page number

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

    /**
     * Get test data: get list of Sync task names
     */
    public static List<String> getSyncTaskNames() {
        int size = 3; // Sync task amount per page
        int page = 1;  // Page number

        Response response = given()
                .header("Content-Type", "application/json")
                .header("Cookie", "JSESSIONID=" + authCookie)
                .queryParam("size", size)
                .queryParam("page", page)
                .log().all()
                .when()
                .get(baseURI + "/api/v1/sync");

        JsonPath jsonPath = response.jsonPath();
        List<String> syncTaskNamesList = jsonPath.getList("taskName", String.class);

        return syncTaskNamesList;
    }

    @Feature("Get sync task by id")

    @ParameterizedTest
    @MethodSource("getSyncTaskIds")
    @Tag("positive")
    @Order(1)
    @DisplayName("Get Sync Task by id successfully")
    @Description("Get synchronization task by id with status code 200")
    public void getSyncTaskByIdSuccessfully(int syncTaskId) {
        System.out.println("--- Start parametrized test: Get Sync Task by id with id = " + syncTaskId);

        Allure.step("Sending get sync task by id request.");
        Response response = restClient.getNoParams(path + syncTaskId, authCookie);

        System.out.println("--- Response Body: " + response.getBody().prettyPrint());

        Allure.step("Verify status-code is 200.");
        AssertionClient.checkStatusCode(response, 200);

        Allure.step("Verify that response body not empty.");
        AssertionClient.checkResponseBodyIsNotNull(response);
    }

    @Test
    @Tag("negative")
    @Order(2)
    @DisplayName("Get 400 for Sync Task by id")
    @Description("Get synchronization task by wrong id with status code 400")
    public void verifyStatusCodeForWrongSyncTaskId() {
        System.out.println("--- Start test: Get 400 for Sync Task by id");

        Allure.step("Sending get sync task by wrong id request.");
        Response response = restClient.getNoParams(path + TestDataGenerator.generateId(), authCookie);

        System.out.println("--- Response Body: " + response.getBody().prettyPrint());

        Allure.step("Verify status-code is 400.");
        AssertionClient.checkStatusCode(response, 400);
    }

    @Test
    @Tag("positive")
    @Order(3)
    @DisplayName("Verify id and taskName for Sync Task")
    @Description("Verify id and taskName for synchronization task")
    public void verifyIdAndTaskNameForSyncTask() {
        System.out.println("--- Start test: Verify id and taskName for Sync Task");

        Allure.step("Sending get sync task by id request.");
        Response response = restClient.getNoParams(path + syncTaskId, authCookie);

        System.out.println("--- Response Body: " + response.getBody().prettyPrint());

        System.out.println("--- Checking id " + syncTaskId + " and taskName " + taskName + ".");
        Allure.step("Verify id equals " + syncTaskId + " and taskName equals " + taskName);
        assertAll(
                () -> response.then().body("id", equalTo(syncTaskId)),
                () -> response.then().body("taskName", equalTo(taskName))
        );
    }

    @Test
    @Tag("negative")
    @Order(4)
    @DisplayName("Get 403 with sync task by id request")
    @Description("Get 403 with sync task by id request when cookie is wrong")
    public void getAccessForbiddenWithWrongCookie() {
        System.out.println("--- Start test: Get 403 with sync task by id request");

        Allure.step("Sending get sync task by id request with wrong cookie.");
        Response response = restClient.getNoParams(path + syncTaskId, "8C37CE51E75FFEFA9A29B71BEBA34072");

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
    @Order(5)
    @DisplayName("Get sync task by id with unsupported content-type header")
    @Description("Get 500 with sync task by id request when unsupported content-type header")
    public void getErrorWhenUnsupportedContentTypeInRequest(String contentType) {
        System.out.println("--- Start test: Get sync task by id with unsupported content-type header");

        Allure.step("Sending get sync task by id request with unsupported content-type header.");
        Response response = restClient.get(path + syncTaskId, authCookie, contentType);

        System.out.println("--- Response Body: " + response.getBody().prettyPrint());

        Allure.step("Verify status-code is 500.");
        AssertionClient.checkStatusCode(response, 500);

        Allure.step("Verify response body error is \"Content-Type '" + contentType + ";charset=ISO-8859-1' is not supported");
        AssertionClient.checkUnsupportedContentTypeError(response, contentType, path + syncTaskId);
    }
}
