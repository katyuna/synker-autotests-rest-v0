package api.user_mapping_controller;

import base.BaseTest;
import data.dto.UserMappingDto;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

public class GetUserMappingListTest extends BaseTest {

    private static String sessionCookie;

    @BeforeAll
    public static void authenticate() {
        sessionCookie = given()
                .contentType(ContentType.JSON)
                .body("{ \"username\": \"test\", \"password\": \"test\" }")
                .post(RestAssured.baseURI + "api/v1" + "/auth/login")
                .then()
                .statusCode(200)
                .extract()
                .cookie("JSESSIONID");
        System.out.println("Authenticated. Session Cookie: " + sessionCookie);
    }

    @Test
    public void createMapping() {
        UserMappingDto userMappingDto = new UserMappingDto();
        userMappingDto.setId("0");
        userMappingDto.setSourceUser("sourceUser");
        userMappingDto.setTargetUser("targetUser");
        userMappingDto.setComment("Test comment");
        userMappingDto.setCreated("2024-12-18T08:20:51.797Z");
        userMappingDto.setCreatedBy("tester");

        Response addMappingResponse = given()
                .cookie("JSESSIONID", sessionCookie)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(userMappingDto)
                .when()
                .post(RestAssured.baseURI + "api/v1" + "/mapping/user")
                .then()
                .statusCode(200)
                .extract()
                .response();

        int mappingId = addMappingResponse.jsonPath().getInt("id");
        System.out.println("Created Mapping ID: " + mappingId);
    }
}
