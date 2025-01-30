package api.sync_controller;

import base.ApiBaseTest;
import data.dto.SyncDto;
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

import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertAll;

public class UpdateSyncTaskTestApi extends ApiBaseTest {

    private final String path = "/api/v1/sync";
    private final RestClient restClient = new RestClient(RestAssured.baseURI);

    /**
     * Get test data: get list of Automation sync tasks
     */
    public static List<SyncDto> getAutomationSyncTasks() {
        int size = 100; // Sync task amount per page
        int page = 0;  // Page number

        System.out.println("-> Getting test parameters -> automatically created " +
                "sync task list with /api/v1/sync.");
        Response response = given()
                .header("Content-Type", "application/json")
                .header("Cookie", "JSESSIONID=" + authCookie)
                .queryParam("size", size)
                .queryParam("page", page)
                .log().all()
                .when()
                .get(baseURI + "/api/v1/sync");

        List<SyncDto> syncTasks = response.jsonPath().getList("", SyncDto.class);
        List<SyncDto> automationSyncTasks = new ArrayList<>();

        for (SyncDto syncTask : syncTasks) {
            if ("Automation".equals(syncTask.getTaskName())) {
                automationSyncTasks.add(syncTask);
            }
        }

        return automationSyncTasks;
    }

    @Feature("Update sync task")

    @Test
    @Tag("positive")
    @Order(1)
    @DisplayName("Update sync task successfully")
    @Description("Update synchronization task with status code 200")
    public void updateSyncTaskSuccessfully() {
        System.out.println("--- Start test: Update sync task successfully");
        String updated = "Automation updated";
        Boolean sync = true;

        // Update fields value
        SyncDto syncTask = getAutomationSyncTasks().get(0);
        syncTask.setTaskName(updated);
        syncTask.setFilter(updated);
        syncTask.setSyncComments(sync);

        Allure.step("Sending put sync task request.");
        Response response = restClient.put(path, syncTask, authCookie);

        System.out.println("--- Response Body: " + response.getBody().prettyPrint());

        Allure.step("Verify status-code is 200.");
        AssertionClient.checkStatusCode(response, 200);

        Allure.step("Verify that response body not empty.");
        AssertionClient.checkResponseBodyIsNotNull(response);

        Allure.step("Getting updated sync task by id JSON Object.");
        SyncDto syncDtoUpdated = response.jsonPath().getObject("", SyncDto.class);
        int updatedSyncTaskId = syncDtoUpdated.getId();

        System.out.println("--- Sending get updated sync task by id " + updatedSyncTaskId + " request.");
        Allure.step("Sending get updated sync task by id " + updatedSyncTaskId + " request.");
        Response finalResponse = restClient.getNoParams(path + "/" + updatedSyncTaskId, authCookie);

        System.out.println("--- Checking sync task by id " + updatedSyncTaskId + " updated successfully");
        Allure.step("Verify taskName equals " + updated + ". Other fields are the same.");
        assertAll(
                () -> finalResponse.then().body("id", equalTo(updatedSyncTaskId)),
                () -> finalResponse.then().body("taskName", equalTo(updated)),
                () -> finalResponse.then().body("createdBy", equalTo(syncDtoUpdated.getCreatedBy())),
                () -> finalResponse.then().body("source", not(empty())),
                () -> finalResponse.then().body("destination", not(empty())),
                () -> finalResponse.then().body("filter", equalTo(updated)),
                () -> finalResponse.then().body("comment", equalTo(syncDtoUpdated.getComment())),
                () -> finalResponse.then().body("syncTime", equalTo(syncDtoUpdated.getSyncTime())),
                () -> finalResponse.then().body("syncComments", equalTo(sync)),
                () -> finalResponse.then().body("allowSync", equalTo(syncDtoUpdated.getAllowSync())),
                () -> finalResponse.then().body("syncPeriod", equalTo(syncDtoUpdated.getSyncPeriod()))
        );
    }

    @Test
    @Tag("negative")
    @Order(2)
    @DisplayName("Get 403 for update sync task")
    @Description("Get 403 when updating sync task with wrong cookie")
    public void updateSyncTaskWithWrongCookie() {
        System.out.println("--- Start test: Get 403 for update sync task");

        SyncDto syncTask = getAutomationSyncTasks().get(0);

        Allure.step("Sending put sync task request with wrong cookie.");
        Response response = restClient.put(path, syncTask, "8C37CE51E75FFEFA9A29B71BEBA34072");

        System.out.println("--- Response Body: " + response.getBody().prettyPrint());

        Allure.step("Verify status-code is 403.");
        AssertionClient.checkStatusCode(response, 403);
    }
}
