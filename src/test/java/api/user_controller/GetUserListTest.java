package api.user_controller;

import base.BaseTest;
import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.emptyOrNullString;
import static org.hamcrest.Matchers.not;

public class GetUserListTest extends BaseTest {

    @ParameterizedTest
    @CsvSource({
            "0, 1",
            "0, 10",
            "0, 100",
            "1,10",
            "100, 10",
            "125, 3"
    })
    @Tag("positive")
    @Order(1)
    @DisplayName("Get users by Ids correctly.")
    @Description("Get user list with different size and page")
    public void getUserListWithDifferentPageNumbersAndUsersPerPage(int page, int size){
        System.out.println("-> Start test: Get user list with parameters: size = " + size +", page = " + page + ".");
        Allure.step("Get user list with page = " + page + " and size = " + size + ".");
        Allure.step("Sending get user list request.");
        Response response = given()
                .header("Content-Type", "application/json")
                .header("Cookie", "JSESSIONID=" + authCookie)
                .queryParam("size", size)
                .queryParam("page", page)
                .log().all()
                .when()
                .get(baseURI + "/api/v1/user");

        System.out.println("--- Response Body: " + response.getBody().asString());
        System.out.println("--- Checking response status code.");
        Allure.step("Verify status-code is 200.");
        response.then().statusCode(200);
        System.out.println("--- Response code: " + response.getStatusCode());
        System.out.println("--- Checking response body.");
        Allure.step("Verify that response body not empty.");
        response.then().assertThat().body(not(emptyOrNullString()));
    }

    @Test
    @Tag("positive")
    @Order(2)
    @DisplayName("Get user list without page and size parameters")
    @Description("Get user list when no page and size pregames")
    public void getUsersListWhenNoParametersInRequest(){

    }

    @ParameterizedTest
    @CsvSource({
            " , ",
            "-1, 1",
            "1, -1",
            "null, 1",
            "1, null"
    })
    @Tag("negative")
    @Order(3)
    @DisplayName("Get user list with empty page and size parameters")
    @Description("Get 500 when page and size parameters are empty, null or negative number")
    public void getErrorWhenEmptyParametersInRequest(int page, int size){


    }

    @Test
    @Tag("negative")
    @Order(4)
    @DisplayName("Get 500 with user list request")
    @Description("Get 500 with user list request when URL is wrong")
    public void getInternalServerErrorWithWrongUrl(){

    }


    @Test
    @Tag("negative")
    @Order(5)
    @DisplayName("Get 400 with wrong request")
    @Description("Get 400 when request body is wrong")
    public void getBadRequestErrorWithWrongRequestBody(){

    }


}
