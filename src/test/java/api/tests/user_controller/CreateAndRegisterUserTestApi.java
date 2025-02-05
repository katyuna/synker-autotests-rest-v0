package api.tests.user_controller;

import api.base.ApiBaseTest;
import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import api.utils.AssertionClient;

import static io.restassured.RestAssured.given;

@Tag("api")
public class CreateAndRegisterUserTestApi extends ApiBaseTest {

    String requestBodyCreateUser = "";

    String requestBodyRegisterUser = "";

    @Feature("Create new User")

    @Test
    @Disabled
    @Tag("positive")
    @DisplayName("Create user with role ROLE_ADMIN")
    @Description("Create user with role ROLE_ADMIN")

    public void createUserWithRoleAdmin() {
        System.out.println("-> Start test: Sending POST-create-user request.");
        Allure.step("Send POST-create-user request.");
        Response response = restClient.post("/api/v1/user/admin", requestBodyCreateUser, authCookie);
        Allure.step("Verify status-code is 200.");
        AssertionClient.checkStatusCode(response, 200);
        Allure.step("Verify that response body not null.");
        AssertionClient.checkResponseBodyIsNotNull(response);
    }

    @Test
    @Disabled
    @Tag("positive")
    @DisplayName("Register user with role ROLE_ADMIN")
    @Description("Register user with role ROLE_ADMIN")

    public void registerUserWithRoleAdmin() {
        System.out.println("-> Start test: Sending POST-register-user request.");
        Allure.step("Send POST-register-user request");
        Response response = restClient.post("/api/v1/user/registration", requestBodyRegisterUser, authCookie);
        Allure.step("Verify status-code is 200.");
        AssertionClient.checkStatusCode(response, 200);
        Allure.step("Verify that response body not null.");
        AssertionClient.checkResponseBodyIsNotNull(response);
    }



}
