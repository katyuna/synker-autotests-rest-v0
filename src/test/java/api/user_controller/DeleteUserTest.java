package api.user_controller;

import base.BaseTest;
import data.TestDataGenerator;
import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import utils.AssertionClient;
import utils.DataBaseClient;

import java.sql.SQLException;

public class DeleteUserTest extends BaseTest {

    /**
     * Test Data
     */
    Integer userId = TestDataGenerator.generateId();
    String userName = TestDataGenerator.generateUserName();
    String password = "$2a$10$wN3B8UniS8y1.7hcy96Czu1bPoSK2FdFPGNaDR.RNAI9Y1Dt.HS72";
    Integer roleId = 1;
    String insertUserQuery = "INSERT INTO public.t_user \n" +
            "(id, \"password\", username) \n" +
            "VALUES(" + userId + ", '" + password + "', '" + userName + "'); ";
    String insertRoleQuery = "INSERT INTO public.t_user_t_role (t_user_id, roles_id) VALUES(" + userId + ", " + roleId + ");";

    @BeforeEach
    public void dataBaseConnection() throws SQLException {
        System.out.println("-> Establish DataBase connection.");
        DataBaseClient.establishConnection();
    }

    @AfterEach
    public void disconnectFromDataBase() {
        DataBaseClient.closeConnection();
    }

    /**
     *
     */
    @Test
    @Tag("positive")
    @DisplayName("Delete user with existing Id.")
    @Description("Delete user by Id test with existing Id. User id got manually.")
    public void deleteUserByIdWithExistingId() throws SQLException {
        System.out.println("-> Add User in to Data Base with id = " + userId + ".");
        DataBaseClient.executeQuery(insertUserQuery);
        System.out.println("-> Add UserRoleId = " + roleId + " in to Data Base.");
        DataBaseClient.executeQuery(insertRoleQuery);

        String endpoint = "/api/v1/user/admin/" + userId;
        System.out.println(endpoint);
        System.out.println("-> Start test: Sending DELETE-user request with existing id = " + userId + ".");
        Allure.step("Sending DELETE-user request with existing id =" + userId + ".");
        Response response = restClient.delete(endpoint, authCookie);

        Allure.step("Verify status-code is 200.");
        AssertionClient.checkStatusCode(response, 200);
        Allure.step("Verify that response body not null.");
        AssertionClient.checkResponseBodyIsNotNull(response);
    }

}
