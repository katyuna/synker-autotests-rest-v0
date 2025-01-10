package base;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;

public class BaseTest {

    @BeforeAll
    public static void setUp() {

        //Устанавливаем базовый URL
        RestAssured.baseURI = "http://sync-ui-dev.softwarecats.pro:8536/";
    }



}