package api.tracker_controller;

import base.BaseTest;
import data.TestDataGenerator;
import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import utils.AssertionClient;
import utils.RestClient;

import java.util.List;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;

public class GetTrackerByIdTest extends BaseTest {

    private final String path = "/api/v1/tracker/";
    private final RestClient restClient = new RestClient(RestAssured.baseURI);

    /**
     * Get test data: get list of tracker ids
     */
    public static List<Integer> getTrackerIds() {
        int size = 3; // Tracker amount per page
        int page = 0;  // Page number

        Response response = given()
                .header("Content-Type", "application/json")
                .header("Cookie", "JSESSIONID=" + authCookie)
                .queryParam("size", size)
                .queryParam("page", page)
                .log().all()
                .when()
                .get(baseURI + "/api/v1/tracker");

        JsonPath jsonPath = response.jsonPath();
        List<Integer> trackerIdsList = jsonPath.getList("id", Integer.class);

        return trackerIdsList;
    }

    @Feature("Get tracker by id")

    @ParameterizedTest
    @MethodSource("getTrackerIds")
    @Tag("positive")
    @Order(1)
    @DisplayName("Get tracker by id successfully")
    @Description("Get tracker by id with status code 200")
    public void getTrackerByIdSuccessfully(int trackerId) {
        System.out.println("--- Start parametrized test: Get tracker by id with id = " + trackerId);

        Allure.step("Sending get sync task by id request.");
        Response response = restClient.getNoParams(path + trackerId, authCookie);

        System.out.println("--- Response Body: " + response.getBody().prettyPrint());

        Allure.step("Verify status-code is 200.");
        AssertionClient.checkStatusCode(response, 200);

        Allure.step("Verify that response body not empty.");
        AssertionClient.checkResponseBodyIsNotNull(response);
    }

    @Test
    @Tag("negative")
    @Order(2)
    @DisplayName("Get 400 for tracker by id")
    @Description("Get tracker by wrong id with status code 400")
    public void verifyStatusCodeForWrongTrackerId() {
        System.out.println("--- Start test: Get 400 for tracker by id");

        Allure.step("Sending get tracker by wrong id request.");
        Response response = restClient.getNoParams(path + TestDataGenerator.generateId(), authCookie);

        System.out.println("--- Response Body: " + response.getBody().prettyPrint());

        Allure.step("Verify status-code is 400.");
        AssertionClient.checkStatusCode(response, 400);
    }

    @ParameterizedTest
    @MethodSource("getTrackerIds")
    @Tag("negative")
    @Order(3)
    @DisplayName("Get 403 with tracker by id request")
    @Description("Get 403 with tracker by id request when cookie is wrong")
    public void getAccessForbiddenWithWrongCookie(int trackerId) {
        System.out.println("--- Start test: Get 403 with tracker by id request");

        Allure.step("Sending get tracker by id request with wrong cookie.");
        Response response = restClient.getNoParams(path + trackerId, "8C37CE51E75FFEFA9A29B71BEBA34072");

        System.out.println("--- Response Body: " + response.getBody().prettyPrint());

        Allure.step("Verify status-code is 403.");
        AssertionClient.checkStatusCode(response, 403);
    }

    @ParameterizedTest
    @CsvSource({
            "application/xml",
            "application/soap+xml",
            "application/javascript",
            "application/graphql",
            "application/pdf",
    })
    @Tag("negative")
    @Order(4)
    @DisplayName("Get tracker by id with unsupported content-type header")
    @Description("Get 500 with tracker by id request when unsupported content-type header")
    public void getErrorWhenUnsupportedContentTypeInRequest(String contentType) {
        System.out.println("--- Start test: Get tracker by id with unsupported content-type header");

        int trackerId = getTrackerIds().get(0);
        Allure.step("Sending get tracker by id request with unsupported content-type header.");
        Response response = restClient.get(path + trackerId, authCookie, contentType);

        System.out.println("--- Response Body: " + response.getBody().prettyPrint());

        Allure.step("Verify status-code is 500.");
        AssertionClient.checkStatusCode(response, 500);

        Allure.step("Verify response body error is \"Content-Type '" + contentType + ";charset=ISO-8859-1' is not supported");
        AssertionClient.checkUnsupportedContentTypeError(response, contentType, path + trackerId);
    }
}
