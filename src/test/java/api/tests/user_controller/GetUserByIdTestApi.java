package api.tests.user_controller;

import api.base.ApiBaseTest;
import api.data.TestDataGenerator;
import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import api.utils.AssertionClient;
import api.utils.DataBaseClient;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.emptyOrNullString;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Tag("api")
public class GetUserByIdTestApi extends ApiBaseTest {

    /**
     * Get test data: list of user ids
     */
    public static List<Integer> getUsersIds() {
        int size = 10; // User amount per page
        int page = 0;  // Page number

        System.out.println("-> Getting test parameters -> user ids with /api/v1/user.");
        Response response = given()
                .header("Content-Type", "application/json")
                .header("Cookie", "JSESSIONID=" + authCookie)
                .queryParam("size", size)
                .queryParam("page", page)
                .when()
                .get(baseURI + "/api/v1/user");

        JsonPath jsonPath = response.jsonPath();
        List<Integer> userIdsList = jsonPath.getList("id", Integer.class);
        System.out.println("--- Test parameters have gotten, user ids list: " + userIdsList + ".");
        return userIdsList;
    }

    /**
     * Test method
     *
     * @param id
     */
    @Feature("Get user by Id")

    @ParameterizedTest
    @MethodSource("getUsersIds")
    @Tag("positive")
    @Order(1)
    @DisplayName("Get users by Ids correctly.")
    @Description("Get user by Id parametrized test with existing Ids. User ids got from /api/v1/user.")
    public void getUserByIdWithExistingIds(int id) throws SQLException {
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("page", String.valueOf(0));
        queryParams.put("size", String.valueOf(10));
        String endpoint = "/api/v1/user/" + id;

        System.out.println("-> Start test: Sending GET-user-by-id request with id = " + id + ".");
        Allure.step("Sending GET-user-by-id request with id = " + id + ".");
        Response response = restClient.get(endpoint, queryParams, authCookie);
        Allure.step("Verify status-code is 200.");
        AssertionClient.checkStatusCode(response, 200);
        Allure.step("Verify that response body not null.");
        AssertionClient.checkResponseBodyIsNotNull(response);
        Allure.step("Verify username with Data base.");
        String expected = response.path("username");

        List<Map<String, Object>> users = DataBaseClient.executeDQLQuery("select username from t_user where id= " + id + ";");
        System.out.println("--- Verify username with Data Base");
        String actual = (String) users.get(0).get("username");
        assertEquals(expected, actual,
                String.format("Strings are not equals! Expected: '%s', Actual: '%s'", expected, actual));
    }

    /**
     * Test method
     */
    @Test
    @Tag("negative")
    @Order(2)
    @DisplayName("Get users by Ids with not existing Id.")
    @Description("Get user by Id test with not existing Id. User id got from generator.")
    public void getUserByIdWithNotExistingId() {
        Integer id = TestDataGenerator.generateId();
        String endpoint = "/api/v1/user/" + id;

        System.out.println("-> Start test: Sending GET-user-by-id request with not existing id = " + id + ".");
        Allure.step("Sending GET-user-by-id request with not existing id = " + id + ".");
        Response response = restClient.getNoParams(endpoint, authCookie);

        Allure.step("Verify status-code is 400.");
        AssertionClient.checkStatusCode(response, 400);
        Allure.step("Verify that response body not null.");
        AssertionClient.checkResponseBodyIsNotNull(response);
        Allure.step("Verify that response contain message.");
        String error = response.jsonPath().getString("error");
        assertThat("Message field is absent or empty.", error, not(emptyOrNullString()));
    }

    /**
     * Test method
     */
    @Test
    @Tag("negative")
    @Order(3)
    @DisplayName("Get users by Ids with not valid Id.")
    @Description("Get user by Id test with not existing Id. User id got from generator.")
    public void getUserByIdWithNotValidId() {
        Integer id = -1;
        String endpoint = "/api/v1/user/" + id;

        System.out.println("-> Start test: Sending GET-user-by-id request with not valid id = " + id + ".");
        Allure.step("Sending GET-user-by-id request with not valid id = " + id + ".");
        Response response = restClient.getNoParams(endpoint, authCookie);

        Allure.step("Verify status-code is 400.");
        AssertionClient.checkStatusCode(response, 400);
        Allure.step("Verify that response body not null.");
        AssertionClient.checkResponseBodyIsNotNull(response);
    }
}