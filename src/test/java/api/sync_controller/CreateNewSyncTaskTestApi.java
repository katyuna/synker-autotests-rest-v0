package api.sync_controller;

import base.ApiBaseTest;
import data.TestDataGenerator;
import data.dto.SyncDto;
import data.dto.TrackerDto;
import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import utils.AssertionClient;
import utils.RestClient;

import java.util.List;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertAll;

public class CreateNewSyncTaskTestApi extends ApiBaseTest {

    private final String path = "/api/v1/sync";
    private final RestClient restClient = new RestClient(RestAssured.baseURI);

    /**
     * Get test data: get list of Trackers
     */
    public static List<TrackerDto> getTrackers() {
        int size = 2; // Tracker amount per page
        int page = 0;  // Page number

        System.out.println("-> Getting test parameters -> trackers with /api/v1/tracker.");
        Response response = given()
                .header("Content-Type", "application/json")
                .header("Cookie", "JSESSIONID=" + authCookie)
                .queryParam("size", size)
                .queryParam("page", page)
                .log().all()
                .when()
                .get(baseURI + "/api/v1/tracker");

        return response
                .jsonPath()
                .getList("", TrackerDto.class);
    }

    @Feature("Create new sync task")

    @Test
    @Tag("positive")
    @Order(1)
    @DisplayName("Create new sync task successfully")
    @Description("Create synchronization task with status code 200 and verify required fields")
    public void createSyncTask() {
        System.out.println("--- Start test: Create new sync task successfully");
        Allure.step("Sending post new sync task request.");

        SyncDto syncDto = new SyncDto();
        syncDto.setCreatedBy("Automation");
        syncDto.setId(TestDataGenerator.generateId());
        syncDto.setCreated(null);
        syncDto.setSource(getTrackers().get(0));
        syncDto.setDestination(getTrackers().get(1));
        syncDto.setFilter("Automation");
        syncDto.setComment("Automation");
        syncDto.setSyncTime(false);
        syncDto.setSyncComments(false);
        syncDto.setAllowSync(false);
        syncDto.setSyncPeriod("* 0 */3 * * *");
        syncDto.setTaskName("Automation");

        Response response = restClient.post(path, syncDto, authCookie);

        System.out.println("--- Response Body: " + response.getBody().prettyPrint());

        Allure.step("Verify status-code is 200.");
        AssertionClient.checkStatusCode(response, 200);

        Allure.step("Verify that response body not empty.");
        AssertionClient.checkResponseBodyIsNotNull(response);

        Allure.step("Getting created sync task by id JSON Object.");
        SyncDto syncDtoCreated = response.jsonPath().getObject("", SyncDto.class);
        int createdSyncTaskId = syncDtoCreated.getId();

        System.out.println("--- Sending get created sync task by id " + createdSyncTaskId + " request.");
        Allure.step("Sending get created sync task by id " + createdSyncTaskId + " request.");
        Response finalResponse = restClient.getNoParams(path + "/" + createdSyncTaskId, authCookie);

        Allure.step("Verify response body fields.");
        assertAll(
                () -> finalResponse.then().body("id", equalTo(syncDtoCreated.getId())),
                () -> finalResponse.then().body("taskName", equalTo(syncDtoCreated.getTaskName())),
                () -> finalResponse.then().body("createdBy", equalTo(syncDtoCreated.getCreatedBy())),
                () -> finalResponse.then().body("source", not(empty())),
                () -> finalResponse.then().body("destination", not(empty())),
                () -> finalResponse.then().body("filter", equalTo(syncDtoCreated.getFilter())),
                () -> finalResponse.then().body("comment", equalTo(syncDtoCreated.getComment())),
                () -> finalResponse.then().body("syncTime", equalTo(syncDtoCreated.getSyncTime())),
                () -> finalResponse.then().body("syncComments", equalTo(syncDtoCreated.getSyncComments())),
                () -> finalResponse.then().body("allowSync", equalTo(syncDtoCreated.getAllowSync())),
                () -> finalResponse.then().body("syncPeriod", equalTo(syncDtoCreated.getSyncPeriod()))
        );
    }

    @Test
    @Tag("negative")
    @Order(2)
    @DisplayName("Get 403 for new sync task")
    @Description("Get 403 when creating new sync task with wrong cookie")
    public void createSyncTaskWithWrongCookie() {
        System.out.println("--- Start test: Get 403 for new sync task");
        Allure.step("Sending post new sync task request with wrong cookie.");

        SyncDto syncDto = new SyncDto();
        syncDto.setCreatedBy("Automation");
        syncDto.setId(TestDataGenerator.generateId());
        syncDto.setCreated(null);
        syncDto.setSource(getTrackers().get(0));
        syncDto.setDestination(getTrackers().get(1));
        syncDto.setFilter("Automation");
        syncDto.setComment("Automation");
        syncDto.setSyncTime(false);
        syncDto.setSyncComments(false);
        syncDto.setAllowSync(false);
        syncDto.setSyncPeriod("* 0 */3 * * *");
        syncDto.setTaskName("Automation");

        Response response = restClient.post(path, syncDto, "8C37CE51E75FFEFA9A29B71BEBA34072");

        System.out.println("--- Response Body: " + response.getBody().prettyPrint());

        Allure.step("Verify status-code is 403.");
        AssertionClient.checkStatusCode(response, 403);
    }
}
