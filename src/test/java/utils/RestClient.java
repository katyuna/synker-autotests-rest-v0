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

    /**
     *
     * @param endpoint
     * @param queryParams
     * @param cookie
     * @return
     */
    public Response get(String endpoint, Map<String, String> queryParams, String cookie) {

        return RestAssured.given()
                .baseUri(baseUri)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .queryParams(queryParams)
                .cookie("JSESSIONID", cookie)
                .when()
                .get(endpoint);
    }

    /**
     *
     * @param endpoint
     * @param cookie
     * @return
     */
    public Response getNoParams(String endpoint, String cookie) {

        return RestAssured.given()
                .baseUri(baseUri)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .cookie("JSESSIONID", cookie)
                .when()
                .get(endpoint);
    }

    public Response getNoCookie(String endpoint) {

        return RestAssured.given()
                .baseUri(baseUri)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
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

    public Response put(String endpoint, Object body, String cookie) {
        return RestAssured.given()
                .baseUri(baseUri)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .cookie("JSESSIONID", cookie)
                .body(body)
                .when()
                .put(endpoint);
    }

    public Response delete(String endpoint, String cookie) {
        return RestAssured.given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .baseUri(baseUri)
                .cookie("JSESSIONID", cookie)
                .log().all()
                .when()
                .delete(endpoint);
    }
}

