package api.sync_controller;

import base.BaseTest;
import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertAll;

public class GetSyncTaskByIdTest extends BaseTest {

    private static final int syncTaskId = 1010;
    private static final int wrongSyncTaskId = 0;
    private static final String taskName = "FlowerGarden_Yulia";

    @Feature("Get sync task by id list")

    @Test
    @Tag("positive")
    @Order(1)
    @DisplayName("Get Sync Task by id successfully")
    @Description("Get synchronization task by id with status code 200")
    public void getSyncTaskByIdSuccessfully() {
        System.out.println("--- Start test: Get Sync Task by id successfully");
        Allure.step("Sending get sync task by id request.");
        Response response = given()
                .header("Content-Type", "application/json")
                .header("Cookie", "JSESSIONID=" + authCookie)
                .log().all()
                .when()
                .get(baseURI + "/api/v1/sync/" + syncTaskId);

        System.out.println("--- Response Body: " + response.getBody().prettyPrint());
        System.out.println("--- Checking response status code.");
        Allure.step("Verify status-code is 200.");
        response.then().statusCode(200);
        System.out.println("--- Response code: " + response.getStatusCode());
    }

    @Test
    @Disabled("Test disabled: Comes status code 200 instead of 500!")
    @Tag("negative")
    @Order(2)
    @DisplayName("Get 500 for Sync Task by id")
    @Description("Get synchronization task by wrong id with status code 500")
    public void verifyStatusCodeForWrongSyncTaskId() {
        System.out.println("--- Start test: Get 500 for Sync Task by id");
        Allure.step("Sending get sync task by wrong id request.");
        Response response = given()
                .header("Content-Type", "application/json")
                .header("Cookie", "JSESSIONID=" + authCookie)
                .log().all()
                .when()
                .get(baseURI + "/api/v1/sync/" + wrongSyncTaskId);

        System.out.println("--- Response Body: " + response.getBody().prettyPrint());
        System.out.println("--- Checking response status code.");
        Allure.step("Verify status-code is 500.");
        response.then().statusCode(500);
        System.out.println("--- Response code: " + response.getStatusCode());
    }

    @Test
    @Tag("positive")
    @Order(3)
    @DisplayName("Verify id and taskName for Sync Task")
    @Description("Verify id and taskName for synchronization task")
    public void verifyIdAndTaskNameForSyncTask() {
        System.out.println("--- Start test: Verify id and taskName for Sync Task");
        Allure.step("Sending get sync task by id request.");
        Response response = given()
                .header("Content-Type", "application/json")
                .header("Cookie", "JSESSIONID=" + authCookie)
                .log().all()
                .when()
                .get(baseURI + "/api/v1/sync/" + syncTaskId);

        System.out.println("--- Response Body: " + response.getBody().prettyPrint());
        System.out.println("--- Checking id " + syncTaskId + " and taskName " + taskName + ".");
        Allure.step("Verify id equals " + syncTaskId + " and taskName equals " + taskName);
        assertAll(
                () -> response.then().body("id", equalTo(syncTaskId)),
                () -> response.then().body("taskName", equalTo(taskName))
        );
    }
}
