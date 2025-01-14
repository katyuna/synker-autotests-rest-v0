package base;

import data.TestDataGenerator;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectPackages("api")
public class BaseTest {
    protected static String authCookie;

    @BeforeAll
    public static void setUp() {
        if (authCookie == null) {
            RestAssured.baseURI = "http://sync-ui-dev.softwarecats.pro:8536";

            System.out.println("-> Starting authorization from BaseTest class.");
            authCookie = TestDataGenerator.auth("test", "test");
            System.out.println("-> Authorization completed. Cookie = " + authCookie);
        } else {
            System.out.println("-> Authorization already performed. Cookie = " + authCookie);
        }
    }
}
