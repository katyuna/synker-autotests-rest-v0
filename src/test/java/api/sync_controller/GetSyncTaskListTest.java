package api.sync_controller;

import base.BaseTest;
import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;

public class GetSyncTaskListTest extends BaseTest {

    @Feature("Get sync task list")

    @Test
    @Tag("positive")
    @Order(1)
    @DisplayName("Get Sync Task List successfully")
    @Description("Get synchronization task list with status code 200")
    public void verifyStatusCodeForGetSyncTaskList() {
        System.out.println("--- Start test: Get Sync Task List successfully");
        Allure.step("Sending get sync task list request.");
        Response response = given()
                .header("Content-Type", "application/json")
                .header("Cookie", "JSESSIONID=" + authCookie)
                .log().all()
                .when()
                .get(baseURI + "/api/v1/sync");

        System.out.println("--- Response Body: " + response.getBody().prettyPrint());
        System.out.println("--- Checking response status code.");
        Allure.step("Verify status-code is 200.");
        response.then().statusCode(200);
        System.out.println("--- Response code: " + response.getStatusCode());
    }

    @Test
    @Tag("negative")
    @Order(2)
    @DisplayName("Get 500 for Sync Task List")
    @Description("Get status code 500 for Sync Task List with wrong path")
    public void verifyStatusCodeForGetSyncTaskListWithWrongPath() {
        System.out.println("--- Start test: Get 500 for Sync Task List");
        Allure.step("Sending get sync task list request with wrong path.");
        Response response = given()
                .header("Content-Type", "application/json")
                .header("Cookie", "JSESSIONID=" + authCookie)
                .log().all()
                .when()
                .get(baseURI + "/api/v1/sinc"); // Wrong path

        System.out.println("--- Response Body: " + response.getBody().prettyPrint());
        System.out.println("--- Checking response status code.");
        Allure.step("Verify status-code is 500.");
        response.then().statusCode(500);
        System.out.println("--- Response code: " + response.getStatusCode());
    }
}
