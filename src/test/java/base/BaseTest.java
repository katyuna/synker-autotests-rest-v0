package base;

import data.TestDataGenerator;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;

public class BaseTest {
    protected static String authCookie;

    @BeforeAll
    public static void setUp() {

        /**
         * Set Base URL
         */
         RestAssured.baseURI = "http://sync-ui-dev.softwarecats.pro:8536";
        /**
         * Authorization
         */
        System.out.println("--- Starting authorization from BaseTest class.");
        authCookie = TestDataGenerator.auth("test", "test");
        System.out.println("--- Authorization completed. Cookie = " + authCookie);
    }
}