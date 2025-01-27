package base;

import data.TestDataGenerator;
import io.restassured.RestAssured;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;
import utils.DataBaseClient;
import utils.RestClient;

import java.sql.SQLException;

@Suite
@SelectPackages("api")
public class BaseTest {
    protected static String authCookie;
    public final RestClient restClient = new RestClient(RestAssured.baseURI);

    @BeforeAll
    public static void setUp() throws SQLException {
        if (authCookie == null) {
            RestAssured.baseURI = "http://sync-ui-dev.softwarecats.pro:8536";

            System.out.println("-> Starting authorization from BaseTest class.");
            authCookie = TestDataGenerator.auth("test", "test");
            System.out.println("--- Authorization completed. Cookie = " + authCookie);
        } else {
            System.out.println("--- Authorization already performed. Cookie = " + authCookie);
        }

        System.out.println("-> Establish DataBase connection.");
        DataBaseClient.establishConnection();
    }

    @AfterAll
    public static void disconnectFromDataBase() {
        DataBaseClient.closeConnection();
    }

}
