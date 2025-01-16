package api.user_controller;

import base.BaseTest;
import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.internal.shadowed.jackson.core.JsonProcessingException;
import io.qameta.allure.internal.shadowed.jackson.databind.JsonNode;
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
import utils.JsonClient;
import utils.RestClient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.emptyOrNullString;
import static org.hamcrest.Matchers.not;

public class GetUserListTest extends BaseTest {

    @Feature("Get user list")

    @ParameterizedTest
    @CsvSource({
            "0, 1",
            "01, 01",
            "0, 10",
            "0, 100",
     })
    @Tag("positive")
    @Order(1)
    @DisplayName("Get user list with valid page and size parameters.")
    @Description("Send GET request to fetch a user list with valid page and size parameters, and verify that the response is correct.")
    public void getUserListWithDifferentPageNumbersAndUsersPerPage(int page, int size){
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("page", String.valueOf(page));
        queryParams.put("size", String.valueOf(size));

        System.out.println("-> Start test: Sending GET-user-list request with page = " + page + " and size = " + size + ".");
        Response response = restClient.get("/api/v1/user", queryParams, authCookie);
        Allure.step("Verify status-code is 200.");
        AssertionClient.checkStatusCode(response, 200);
        Allure.step("Verify that response body not null.");
        AssertionClient.checkResponseBodyIsNotNull(response);
        Allure.step("Verify that response body contains users.");
        List<?> users = JsonClient.getValuesListFromResponseByJsonPath(response,"username");
        AssertionClient.checkValuesListNotEmpty(users);
        List<?> passwords = JsonClient.getValuesListFromResponseByJsonPath(response,"password");
        AssertionClient.checkValuesListNotEmpty(passwords);
        List<?> rolesId = JsonClient.getValuesListFromResponseByJsonPath(response,"roles.id");
        AssertionClient.checkValuesListNotEmpty(rolesId);
        List<?> rolesNames = JsonClient.getValuesListFromResponseByJsonPath(response,"roles.name");
        AssertionClient.checkValuesListNotEmpty(rolesNames);
    }

    @ParameterizedTest
    @CsvSource({
            "1,10",
            "100, 10",
            "125, 3"
    })
    @Tag("positive")
    @Order(2)
    @DisplayName("Pagination test.")
    @Description("Send GET request to fetch a user list pagination, and verify that the response not empty.")
    public void getUsersListWithPagination(int page, int size){
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("page", String.valueOf(page));
        queryParams.put("size", String.valueOf(size));

        System.out.println("-> Start test: Sending GET-user-list request with page = " + page + " and size = " + size + ".");
        Response response = restClient.get("/api/v1/user", queryParams, authCookie);
        Allure.step("Verify status-code is 200.");
        AssertionClient.checkStatusCode(response, 200);
        Allure.step("Verify that response body not null.");
        AssertionClient.checkResponseBodyIsNotNull(response);
    }

    @Test
    @Tag("positive")
    @Order(3)
    @DisplayName("Get user list without page and size parameters.")
    @Description("Send GET request to fetch the user list without page and size parameters, and verify that the response is correct.")
    public void getUsersListWhenNoParametersInRequest(){
        System.out.println("-> Start test: Sending GET-user-list request with no parameters.");
        Response response = restClient.getNoParams("/api/v1/user", authCookie);
        Allure.step("Verify status-code is 200.");
        AssertionClient.checkStatusCode(response, 200);
        Allure.step("Verify that response body not null.");
        AssertionClient.checkResponseBodyIsNotNull(response);
    }

    @ParameterizedTest
    @CsvSource({
            "-1, 1",
            "1, -1"
    })
    @Tag("negative")
    @Order(4)
    @DisplayName("Get user list with not valid page and size parameters.")
    @Description("Send GET request to fetch an error with code 500 when page and size parameters have negative values.")
    public void getErrorWhenEmptyParametersInRequest(int page, int size){
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("page", String.valueOf(page));
        queryParams.put("size", String.valueOf(size));

        System.out.println("-> Start test: Sending GET-user-list request with page = " + page + " and size = " + size + ".");
        Response response = restClient.get("/api/v1/user", queryParams, authCookie);
        Allure.step("Verify status-code is 500.");
        AssertionClient.checkStatusCode(response, 500);
        Allure.step("Verify that response body not null.");
        AssertionClient.checkResponseBodyIsNotNull(response);
    }

    @Test
    @Tag("negative")
    @Order(5)
    @DisplayName("Get user list with not valid URL")
    @Description("Send GET request to fetch an error with code 500 when URL is incorrect.")
    public void getInternalServerErrorWithWrongUrl(){
        System.out.println("-> Start test: Sending GET-user-list request with wrong URL.");
        Response response = restClient.getNoParams("/api/v1/user123", authCookie);
        Allure.step("Verify status-code is 500.");
        AssertionClient.checkStatusCode(response, 500);
        Allure.step("Verify that response body not null.");
        AssertionClient.checkResponseBodyIsNotNull(response);
    }
}
