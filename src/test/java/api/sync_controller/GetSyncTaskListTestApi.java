package api.sync_controller;

import base.ApiBaseTest;
import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import utils.AssertionClient;
import utils.RestClient;

import java.util.HashMap;
import java.util.Map;

public class GetSyncTaskListTestApi extends ApiBaseTest {

    private final String path = "/api/v1/sync";
    private final RestClient restClient = new RestClient(RestAssured.baseURI);

    @Feature("Get sync task list")

    @Test
    @Tag("positive")
    @Order(1)
    @DisplayName("Get sync task list successfully")
    @Description("Get sync task list with status code 200")
    public void getSyncTaskListWhenNoParametersInRequest() {
        System.out.println("--- Start test: Get sync task list successfully");

        Allure.step("Sending get sync task list request.");
        Response response = restClient.getNoParams(path, authCookie);

        System.out.println("--- Response Body: " + response.getBody().prettyPrint());

        Allure.step("Verify status-code is 200.");
        AssertionClient.checkStatusCode(response, 200);

        Allure.step("Verify that response body not empty.");
        AssertionClient.checkResponseBodyIsNotNull(response);
    }

    @ParameterizedTest
    @CsvSource({
            "1, 10",
            "100, 10",
            "125, 3"
    })
    @Tag("positive")
    @Order(2)
    @DisplayName("Get sync task list with parameters")
    @Description("Get sync task list with different size and page")
    public void getSyncTaskListWithDifferentPageNumbersAndSyncTasksPerPage(int page, int size) {
        System.out.println("--- Start test: Get sync task list with parameters: " +
                "size = \" + size +\", page = \" + page + \".\"");

        Allure.step("Sending get sync task list request with page = " + page + " and size = " + size + ".");

        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("page", String.valueOf(page));
        queryParams.put("size", String.valueOf(size));

        Response response = restClient.get(path, queryParams, authCookie);

        System.out.println("--- Response Body: " + response.getBody().prettyPrint());

        Allure.step("Verify status-code is 200.");
        AssertionClient.checkStatusCode(response, 200);

        Allure.step("Verify that response body not empty.");
        AssertionClient.checkResponseBodyIsNotNull(response);
    }

    @ParameterizedTest
    @CsvSource({
            "-1, 1",
            "1, -1"
    })
    @Tag("negative")
    @Order(3)
    @DisplayName("Get sync task list with negative page and size parameters")
    @Description("Get 500 when page and size parameters are negative number")
    public void getErrorWhenEmptyParametersInRequest(int page, int size) {
        System.out.println("--- Start test: Get sync task list with empty page and size parameters: " +
                "size = \" + size +\", page = \" + page + \".\"");
        Allure.step("Sending get sync task list request with page = " + page + " and size = " + size + ".");

        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("page", String.valueOf(page));
        queryParams.put("size", String.valueOf(size));

        Response response = restClient.get(path, queryParams, authCookie);

        System.out.println("--- Response Body: " + response.getBody().prettyPrint());

        Allure.step("Verify status-code is 500.");
        AssertionClient.checkStatusCode(response, 500);
    }

    @Test
    @Tag("negative")
    @Order(4)
    @DisplayName("Get 403 with sync task list request")
    @Description("Get 403 with sync task list request when cookie is wrong")
    public void getAccessForbiddenWithWrongCookie() {
        System.out.println("--- Start test: Get 403 with sync task list request");
        Allure.step("Sending get sync task list request with wrong cookie.");

        Response response = restClient.getNoParams(path, "8C37CE51E75FFEFA9A29B71BEBA34072");

        System.out.println("--- Response Body: " + response.getBody().prettyPrint());

        Allure.step("Verify status-code is 403.");
        AssertionClient.checkStatusCode(response, 403);
    }

    @Test
    @Tag("negative")
    @Order(5)
    @DisplayName("Get 500 with sync task list request")
    @Description("Get 500 with sync task list request when URL is wrong")
    public void getInternalServerErrorWithWrongUrl() {
        System.out.println("--- Start test: Get 500 with sync task list request");

        Allure.step("Sending get sync task list request with wrong path.");
        String wrongPath = "api/v1/sinc";

        Response response = restClient.getNoParams("/" + wrongPath, authCookie);

        System.out.println("--- Response Body: " + response.getBody().prettyPrint());

        Allure.step("Verify status-code is 500.");
        AssertionClient.checkStatusCode(response, 500);

        Allure.step("Verify response body error is \"No static resource /" + wrongPath + ".\".");
        AssertionClient.checkWrongPathError(response, wrongPath);
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
    @Order(6)
    @DisplayName("Get sync task list with unsupported content-type header")
    @Description("Get 500 with sync task list request when unsupported content-type header")
    public void getErrorWhenUnsupportedContentTypeInRequest(String contentType) {
        System.out.println("--- Start test: Get sync task list with unsupported content-type header");
        Allure.step("Sending get sync task list request with with unsupported content-type header.");

        Response response = restClient.get(path, authCookie, contentType);

        System.out.println("--- Response Body: " + response.getBody().prettyPrint());

        Allure.step("Verify status-code is 500.");
        AssertionClient.checkStatusCode(response, 500);

        Allure.step("Verify response body error is \"Content-Type '" + contentType + ";charset=ISO-8859-1' is not supported");
        AssertionClient.checkUnsupportedContentTypeError(response, contentType, path);
    }
}
