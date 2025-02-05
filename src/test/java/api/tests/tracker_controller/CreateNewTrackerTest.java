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
import org.junit.jupiter.api.Test;
import api.utils.AssertionClient;
import api.utils.RestClient;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertAll;

public class CreateNewTrackerTest extends ApiBaseTest {

    private final String path = "/api/v1/tracker";
    private final RestClient restClient = new RestClient(RestAssured.baseURI);

    @Feature("Create new tracker")

    @Test
    @Tag("positive")
    @Order(1)
    @DisplayName("Create new tracker successfully")
    @Description("Create tracker with status code 200 and verify required fields")
    public void createTracker() {
        System.out.println("--- Start test: Create new tracker successfully");
        Allure.step("Sending post new tracker request.");

        TrackerDto trackerDto = new TrackerDto();
        trackerDto.setCreatedBy("Automation");
        trackerDto.setId(TestDataGenerator.generateId());
        trackerDto.setTrackerName("Automation");
        trackerDto.setCreated(null);
        trackerDto.setType("YT");
        trackerDto.setTrackerUrl("http://sync-ui-dev.softwarecats.pro:8080/");
        trackerDto.setProjectId("Automation");
        trackerDto.setUsername("Automation");
        trackerDto.setPassword("Automation");

        Response response = restClient.post(path, trackerDto, authCookie);

        System.out.println("--- Response Body: " + response.getBody().prettyPrint());

        Allure.step("Verify status-code is 200.");
        AssertionClient.checkStatusCode(response, 200);

        Allure.step("Verify that response body not empty.");
        AssertionClient.checkResponseBodyIsNotNull(response);

        Allure.step("Getting created tracker by id JSON Object.");
        TrackerDto trackerDtoCreated = response.jsonPath().getObject("", TrackerDto.class);
        int createdTrackerId = trackerDtoCreated.getId();

        System.out.println("--- Sending get created tracker by id " + createdTrackerId + " request.");
        Allure.step("Sending get created tracker by id " + createdTrackerId + " request.");
        Response finalResponse = restClient.getNoParams(path + "/" + createdTrackerId, authCookie);

        Allure.step("Verify response body fields.");
        assertAll(
                () -> finalResponse.then().body("id", equalTo(trackerDtoCreated.getId())),
                () -> finalResponse.then().body("createdBy", equalTo(trackerDtoCreated.getCreatedBy())),
                () -> finalResponse.then().body("trackerName", equalTo(trackerDtoCreated.getTrackerName())),
                () -> finalResponse.then().body("created", not(empty())),
                () -> finalResponse.then().body("type", equalTo(trackerDtoCreated.getType())),
                () -> finalResponse.then().body("trackerUrl", equalTo(trackerDtoCreated.getTrackerUrl())),
                () -> finalResponse.then().body("projectId", equalTo(trackerDtoCreated.getProjectId())),
                () -> finalResponse.then().body("username", equalTo(trackerDtoCreated.getUsername())),
                () -> finalResponse.then().body("password", equalTo(trackerDtoCreated.getPassword()))
        );
    }

    @Test
    @Tag("negative")
    @Order(2)
    @DisplayName("Get 403 for new tracker")
    @Description("Get 403 when creating new tracker with wrong cookie")
    public void createTrackerWithWrongCookie() {
        System.out.println("--- Start test: Get 403 for new tracker");
        Allure.step("Sending post new tracker request with wrong cookie.");

        TrackerDto trackerDto = new TrackerDto();
        trackerDto.setCreatedBy("Automation");
        trackerDto.setId(TestDataGenerator.generateId());
        trackerDto.setTrackerName("Automation");
        trackerDto.setCreated(null);
        trackerDto.setType("YT");
        trackerDto.setTrackerUrl("http://sync-ui-dev.softwarecats.pro:8080/");
        trackerDto.setProjectId("Automation");
        trackerDto.setUsername("Automation");
        trackerDto.setPassword("Automation");

        Response response = restClient.post(path, trackerDto, "8C37CE51E75FFEFA9A29B71BEBA34072");

        System.out.println("--- Response Body: " + response.getBody().prettyPrint());

        Allure.step("Verify status-code is 403.");
        AssertionClient.checkStatusCode(response, 403);
    }
}