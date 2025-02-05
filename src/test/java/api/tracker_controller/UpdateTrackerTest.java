package api.tracker_controller;

import base.BaseTest;
import data.TestDataGenerator;
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

import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertAll;

public class UpdateTrackerTest extends BaseTest {

    private final RestClient restClient = new RestClient(RestAssured.baseURI);
    private final String path = "/api/v1/tracker";

    /**
     * Get test data: get list of Automation trackers
     */
    public static List<TrackerDto> getAutomationTrackers() {
        int size = 100; // Trackers amount per page
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

        return automationTrackers;
    }

    @Feature("Update tracker")

    @Test
    @Tag("positive")
    @Order(1)
    @DisplayName("Update tracker successfully")
    @Description("Update tracker with status code 200")
    public void updateTrackerSuccessfully() {
        System.out.println("--- Start test: Update tracker successfully");
        String updated = "Automation updated";

        // Update fields value
        TrackerDto tracker = getAutomationTrackers().get(0);
        tracker.setProjectId(updated);
        tracker.setUsername(updated);
        tracker.setPassword(updated);

        Allure.step("Sending put tracker request.");
        Response response = restClient.put(path, tracker, authCookie);

        System.out.println("--- Response Body: " + response.getBody().prettyPrint());

        Allure.step("Verify status-code is 200.");
        AssertionClient.checkStatusCode(response, 200);

        Allure.step("Verify that response body not empty.");
        AssertionClient.checkResponseBodyIsNotNull(response);

        Allure.step("Getting updated tracker by id JSON Object.");
        TrackerDto trackerDtoUpdated = response.jsonPath().getObject("", TrackerDto.class);
        int updatedTrackerId = trackerDtoUpdated.getId();

        System.out.println("--- Sending get updated tracker by id " + updatedTrackerId + " request.");
        Allure.step("Sending get updated tracker by id " + updatedTrackerId + " request.");
        Response finalResponse = restClient.getNoParams(path + "/" + updatedTrackerId, authCookie);

        System.out.println("--- Checking tracker by id " + updatedTrackerId + " updated successfully");
        Allure.step("Verify projectId, username, password fields equal " + updated +
                ". Other fields are the same.");
        assertAll(
                () -> finalResponse.then().body("id", equalTo(updatedTrackerId)),
                () -> finalResponse.then().body("trackerName", equalTo(trackerDtoUpdated.getTrackerName())),
                () -> finalResponse.then().body("createdBy", equalTo(trackerDtoUpdated.getCreatedBy())),
                () -> finalResponse.then().body("type", equalTo(trackerDtoUpdated.getType())),
                () -> finalResponse.then().body("trackerUrl", equalTo(trackerDtoUpdated.getTrackerUrl())),
                () -> finalResponse.then().body("projectId", equalTo(updated)),
                () -> finalResponse.then().body("username", equalTo(updated)),
                () -> finalResponse.then().body("password", equalTo(updated))
        );
    }

    @Test
    @Tag("negative")
    @Order(2)
    @DisplayName("Get 403 for update tracker")
    @Description("Get 403 when updating tracker with wrong cookie")
    public void updateTrackerWithWrongCookie() {
        System.out.println("--- Start test: Get 403 for update tracker");

        TrackerDto tracker = getAutomationTrackers().get(0);

        Allure.step("Sending put tracker request with wrong cookie.");
        Response response = restClient.put(path, tracker, "8C37CE51E75FFEFA9A29B71BEBA34072");

        System.out.println("--- Response Body: " + response.getBody().prettyPrint());

        Allure.step("Verify status-code is 403.");
        AssertionClient.checkStatusCode(response, 403);
    }
}
