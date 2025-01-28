package api.sync_controller;

import base.BaseTest;
import data.dto.SyncDto;
import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import utils.AssertionClient;
import utils.RestClient;

import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;

public class DeleteSyncTaskTest extends BaseTest {

    private final RestClient restClient = new RestClient(RestAssured.baseURI);

    /**
     * Get test data: get list of Automation sync task ids (created or updated automatically)
     */
    public static List<Integer> getAutomationSyncTaskIds() {
        int size = 100; // Sync task amount per page
        int page = 0;  // Page number

        System.out.println("-> Getting test parameters -> automatically created or updated " +
                "sync task ids list with /api/v1/sync.");
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
            if ("Automation".equals(syncTask.getComment())) {
                automationSyncTasks.add(syncTask);
            }
        }

        List<Integer> ids = new ArrayList<>();
        for (SyncDto syncTask : automationSyncTasks) {
            ids.add(syncTask.getId());
        }

        System.out.println("--- Test parameters have gotten, automatically created or updated " +
                "sync task ids list: " + ids + ".");
        return ids;
    }

    @Feature("Delete sync task")

    @ParameterizedTest
    @MethodSource("getAutomationSyncTaskIds")
    @Tag("negative")
    @Order(1)
    @DisplayName("Get 403 for delete sync task")
    @Description("Get 403 when deleting sync task with wrong cookie")
    public void deleteSyncTaskWithWrongCookie(int id) {
        System.out.println("--- Start test: Get 403 for delete sync task");

        Allure.step("Sending delete sync task request with wrong cookie.");
        Response response = restClient.delete("/api/v1/sync/" + id,
                "8C37CE51E75FFEFA9A29B71BEBA34072");

        System.out.println("--- Response Body: " + response.getBody().prettyPrint());

        Allure.step("Verify status-code is 403.");
        AssertionClient.checkStatusCode(response, 403);
    }

    @ParameterizedTest
    @MethodSource("getAutomationSyncTaskIds")
    @Tag("positive")
    @Order(2)
    @DisplayName("Delete sync task successfully")
    @Description("Delete synchronization task with status code 200")
    public void deleteSyncTaskSuccessfully(int id) {
        System.out.println("--- Start test: Delete sync task successfully");

        Allure.step("Sending delete sync task request.");
        Response response = restClient.delete("/api/v1/sync/" + id, authCookie);

        System.out.println("--- Response Body: " + response.getBody().prettyPrint());

        Allure.step("Verify status-code is 200.");
        AssertionClient.checkStatusCode(response, 200);

        Allure.step("Sending delete sync task request again to verify it was deleted.");
        response = restClient.delete("/api/v1/sync/" + id, authCookie);

        Allure.step("Verify status-code is 400.");
        AssertionClient.checkStatusCode(response, 400);
    }
}
