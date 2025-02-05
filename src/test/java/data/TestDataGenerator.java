package data;

import com.github.javafaker.Faker;
import data.dto.SyncDto;
import data.dto.TrackerDto;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;

public class TestDataGenerator {

    private static Faker faker = new Faker();

    /**
     * Id generation method
     */
    public static Integer generateId() {
        return Math.abs((int) System.currentTimeMillis());
    }

    public static String generateUserName() {
        return faker.name().username();
    }

    /**
     * Auth method
     *
     * @param login
     * @param password
     * @return authCookie
     */
    public static String auth(String login, String password) {
        String requestBody = "{ \"username\": \"" + login + "\", \"password\": \"" + password + "\" }";
        Response response = given()
                .header("Content-Type", "application/json")
                .body(requestBody)
                .when()
                .post(baseURI + "/api/v1/auth/login");

        String authCookie = response.getCookie("JSESSIONID");

        if (response.statusCode() != 200) {
            throw new RuntimeException("Authorization failed with status code: " + response.statusCode());
        }

        if (authCookie == null) {
            throw new RuntimeException("No auth cookie found in the response!");
        }
        return authCookie;
    }

    /**
     * Create Sync task method
     *
     * @param syncDto
     */
    public static void createSyncTask(SyncDto syncDto) {
        given()
                .baseUri(baseURI)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .cookie("JSESSIONID", auth("test", "test"))
                .body(syncDto)
                .log().all()
                .when()
                .post("/api/v1/sync");
    }

    /**
     * Create Tracker method
     *
     * @param tracker
     */
    public static void createTracker(TrackerDto tracker) {
        given()
                .baseUri(baseURI)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .cookie("JSESSIONID", auth("test", "test"))
                .body(tracker)
                .log().all()
                .when()
                .post("/api/v1/tracker");
    }
}


