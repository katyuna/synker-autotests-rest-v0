package api.user_controller;

import base.BaseTest;
import data.TestDataGenerator;
import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import utils.AssertionClient;
import utils.DataBaseClient;

import java.sql.SQLException;

public class DeleteUserTest extends BaseTest {

    /**
    Test Data
     */
    Integer userId = TestDataGenerator.generateId();
    String userName = TestDataGenerator.generateUserName();
    String password = "$2a$10$wN3B8UniS8y1.7hcy96Czu1bPoSK2FdFPGNaDR.RNAI9Y1Dt.HS72";
    Integer roleId = 1;
    String insertUserQuery = "INSERT INTO public.t_user \n" +
            "(id, \"password\", username) \n" +
            "VALUES(" + userId + ", '" + password + "', '" + userName + "'); ";
    String insertRoleQuery = "INSERT INTO public.t_user_t_role (t_user_id, roles_id) VALUES(" + userId + ", " + roleId + ");";

    /**
     *
     */
    @Test
    @Tag("positive")
    @DisplayName("Delete user with existing Id.")
    @Description("Delete user by Id test with existing Id. User id got manually.")
    public void deleteUserByIdWithExistingId() throws SQLException {
        DataBaseClient.establishConnection();
        DataBaseClient.executeQuery(insertUserQuery);
        DataBaseClient.executeQuery(insertRoleQuery);


        Integer id = 456;
        String endpoint = "/api/v1/user/admin/" + id;
        System.out.println(endpoint);
        System.out.println("-> Start test: Sending DELETE-user request with existing id = " + id + ".");
        Allure.step("Sending DELETE-user request with existing id =" + id + ".");
        Response response = restClient.delete(endpoint, authCookie);

        Allure.step("Verify status-code is 200.");
        AssertionClient.checkStatusCode(response, 200);
        Allure.step("Verify that response body not null.");
        AssertionClient.checkResponseBodyIsNotNull(response);
    }
}
