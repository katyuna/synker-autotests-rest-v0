package utils;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import java.util.Map;

public class RestClient {

    private final String baseUri;

    public RestClient(String baseUri) {
        this.baseUri = baseUri;
    }

    public Response get(String endpoint, Map<String, Object> queryParams, Map<String, String> headers, String cookie) {
        return RestAssured.given()
                .baseUri(baseUri)
                .headers(headers)
                .queryParams(queryParams)
                .cookie("JSESSIONID", cookie)
                .when()
                .get(endpoint);
    }

    public Response post(String endpoint, Object body, String cookie) {
        return RestAssured.given()
                .baseUri(baseUri)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .cookie("JSESSIONID", cookie)
                .body(body)
                .when()
                .post(endpoint);
    }

    public Response put(String endpoint, Object body, Map<String, String> headers, String cookie) {
        return RestAssured.given()
                .baseUri(baseUri)
                .headers(headers)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .cookie("JSESSIONID", cookie)
                .body(body)
                .when()
                .put(endpoint);
    }

    public Response delete(String endpoint, Map<String, String> headers, String cookie) {
        return RestAssured.given()
                .baseUri(baseUri)
                .headers(headers)
                .cookie("JSESSIONID", cookie)
                .when()
                .delete(endpoint);
    }
}

