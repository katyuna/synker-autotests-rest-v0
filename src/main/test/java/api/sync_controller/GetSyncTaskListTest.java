package api.sync_controller;

import base.BaseTest;
import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.apache.http.HttpHeaders.CONTENT_TYPE;

public class GetSyncTaskListTest extends BaseTest {

    private static String sessionCookie;
    private static final int syncTaskId = 1010;
    private static final int wrongSyncTaskId = 0;

    @BeforeAll
    public static void authenticate() {
        sessionCookie = given()
                .contentType(ContentType.JSON)
                .body("{ \"username\": \"test\", \"password\": \"test\" }")
                .post(RestAssured.baseURI + "api/v1" + "/auth/login")
                .then()
                .statusCode(200)
                .extract()
                .cookie("JSESSIONID");
        System.out.println("Authenticated. Session Cookie: " + sessionCookie);
    }

    @Test
    @Tag("positive")
    @Order(1)
    @DisplayName("Get Sync Task List successfully")
    @Description("Get synchronization task list with status code 200")
    public void verifyStatusCodeForGetSyncTaskList() {
        Response response = given()
                .header(CONTENT_TYPE, JSON)
                .header("Cookie", "JSESSIONID=" + sessionCookie)
                .log().all()
                .when()
                .get(RestAssured.baseURI + "api/v1/sync");
        Allure.step("Verify status code is 200");
        response.then()
                .log().all()
                .statusCode(200);
    }

    @Test
    @Tag("positive")
    @Order(2)
    @DisplayName("Get Sync Task by id successfully")
    @Description("Get synchronization task by id with status code 200")
    public void getSyncTaskByIdSuccessfully() {
        Response response = given()
                .header(CONTENT_TYPE, JSON)
                .header("Cookie", "JSESSIONID=" + sessionCookie)
                .log().all()
                .when()
                .get(RestAssured.baseURI + "api/v1/sync/" + syncTaskId);
        Allure.step("Verify status code is 200");
        response.then()
                .log().all()
                .statusCode(200);
    }

    @Test
    @Tag("negative")
    @Order(3)
    @DisplayName("Get 500 for Sync Task List")
    @Description("Get status code 500 for Sync Task List with wrong path")
    public void verifyStatusCodeForGetSyncTaskListWithWrongPath() {
        Response response = given()
                .header(CONTENT_TYPE, JSON)
                .header("Cookie", "JSESSIONID=" + sessionCookie)
                .log().all()
                .when()
                .get(RestAssured.baseURI + "api/v1/sinc");
        Allure.step("Verify status code is 500");
        response.then()
                .log().all()
                .statusCode(500);
    }

    @Test
    @Disabled("Comes status code 200 instead of 500!")
    @Tag("negative")
    @Order(4)
    @DisplayName("Get 500 for Sync Task by id")
    @Description("Get synchronization task by wrong id with status code 500")
    public void verifyStatusCodeForWrongSyncTaskId() {
        Response response = given()
                .header(CONTENT_TYPE, JSON)
                .header("Cookie", "JSESSIONID=" + sessionCookie)
                .log().all()
                .when()
                .get(RestAssured.baseURI + "api/v1/sync/" + wrongSyncTaskId);
        Allure.step("Verify status code is 500");
        response.then()
                .log().all()
                .statusCode(500);
    }

}
