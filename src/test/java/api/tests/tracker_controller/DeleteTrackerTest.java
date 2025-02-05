package api.tests.tracker_controller;

import api.base.ApiBaseTest;
import api.data.TestDataGenerator;
import api.data.dto.TrackerDto;
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
import api.utils.AssertionClient;
import api.utils.RestClient;

import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;

public class DeleteTrackerTest extends ApiBaseTest {

    private final RestClient restClient = new RestClient(RestAssured.baseURI);
    private final String path = "/api/v1/tracker/";

    /**
     * Get test data: get list of Automation tracker ids (created or updated automatically)
     */
    public static List<Integer> getAutomationTrackerIds() {
        int size = 100; // Tracker amount per page
        int page = 0;  // Page number

        System.out.println("-> Creating Automation tracker");
        TrackerDto trackerDto = new TrackerDto();
        trackerDto.setId(TestDataGenerator.generateId());
        trackerDto.setTrackerName("Automation");
        trackerDto.setType("YT");
        trackerDto.setTrackerUrl("http://sync-ui-dev.softwarecats.pro:8080/");
        trackerDto.setProjectId("Automation");
        trackerDto.setUsername("Automation");
        trackerDto.setPassword("Automation");

        TestDataGenerator.createTracker(trackerDto);

        System.out.println("-> Getting test parameters -> automatically created or updated " +
                "tracker ids list with /api/v1/tracker.");
        Response response = given()
                .header("Content-Type", "application/json")
                .header("Cookie", "JSESSIONID=" + authCookie)
                .queryParam("size", size)
                .queryParam("page", page)
                .log().all()
                .when()
                .get(baseURI + "/api/v1/tracker");

        List<TrackerDto> trackers = response.jsonPath().getList("", TrackerDto.class);
        List<TrackerDto> automationTrackers = new ArrayList<>();

        for (TrackerDto tracker : trackers) {
            if ("Automation".equals(tracker.getTrackerName())) {
                automationTrackers.add(tracker);
            }
        }

        List<Integer> ids = new ArrayList<>();
        for (TrackerDto tracker : automationTrackers) {
            ids.add(tracker.getId());
        }

        System.out.println("--- Test parameters have gotten, automatically created or updated " +
                "tracker ids list: " + ids + ".");
        return ids;
    }

    @Feature("Delete tracker")

    @ParameterizedTest
    @MethodSource("getAutomationTrackerIds")
    @Tag("negative")
    @Order(1)
    @DisplayName("Get 403 for delete tracker")
    @Description("Get 403 when deleting tracker with wrong cookie")
    public void deleteTrackerWithWrongCookie(int id) {
        System.out.println("--- Start test: Get 403 for delete tracker");

        Allure.step("Sending delete tracker request with wrong cookie.");
        Response response = restClient.delete(path + id,
                "8C37CE51E75FFEFA9A29B71BEBA34072");

        System.out.println("--- Response Body: " + response.getBody().prettyPrint());

        Allure.step("Verify status-code is 403.");
        AssertionClient.checkStatusCode(response, 403);
    }

    @ParameterizedTest
    @MethodSource("getAutomationTrackerIds")
    @Tag("positive")
    @Order(2)
    @DisplayName("Delete tracker successfully")
    @Description("Delete tracker with status code 200")
    public void deleteSyncTaskSuccessfully(int id) {
        System.out.println("--- Start test: Delete tracker successfully");

        Allure.step("Sending delete tracker request.");
        Response response = restClient.delete(path + id, authCookie);

        System.out.println("--- Response Body: " + response.getBody().prettyPrint());

        Allure.step("Verify status-code is 200.");
        AssertionClient.checkStatusCode(response, 200);

        Allure.step("Sending delete tracker request again to verify it was deleted.");
        response = restClient.delete(path + id, authCookie);

        Allure.step("Verify status-code is 400.");
        AssertionClient.checkStatusCode(response, 400);
    }
}
