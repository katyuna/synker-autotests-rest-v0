package api.utils;

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
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .cookie("JSESSIONID", cookie)
                .log().all()
                .when()
                .get(endpoint);
    }

    /**
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
                .log().all()
                .when()
                .get(endpoint);
    }

    /**
     * @param endpoint
     * @param cookie
     * @param contentType
     * @return
     */
    public Response get(String endpoint, String cookie, String contentType) {

        return RestAssured.given()
                .baseUri(baseUri)
                .header("Content-Type", contentType)
                .cookie("JSESSIONID", cookie)
                .log().all()
                .when()
                .get(endpoint);
    }

    /**
     * @param endpoint
     * @param body
     * @param cookie
     * @return
     */
    public Response post(String endpoint, Object body, String cookie) {
        return RestAssured.given()
                .baseUri(baseUri)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .cookie("JSESSIONID", cookie)
                .body(body)
                .log().all()
                .when()
                .post(endpoint);
    }

    /**
     * @param endpoint
     * @param body
     * @param cookie
     * @param contentType
     * @return
     */
    public Response post(String endpoint, Object body, String cookie, String contentType) {
        return RestAssured.given()
                .baseUri(baseUri)
                .header("Content-Type", contentType)
                .accept(ContentType.JSON)
                .cookie("JSESSIONID", cookie)
                .body(body)
                .log().all()
                .when()
                .post(endpoint);
    }

    /**
     * @param endpoint
     * @param cookie
     * @return
     */
    public Response postNoBody(String endpoint, String cookie) {
        return RestAssured.given()
                .baseUri(baseUri)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .cookie("JSESSIONID", cookie)
                .log().all()
                .when()
                .post(endpoint);
    }

    /**
     * @param endpoint
     * @param cookie
     * @param contentType
     * @return
     */
    public Response postNoBody(String endpoint, String cookie, String contentType) {
        return RestAssured.given()
                .baseUri(baseUri)
                .header("Content-Type", contentType)
                .accept(ContentType.JSON)
                .cookie("JSESSIONID", cookie)
                .log().all()
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
                .log().all()
                .when()
                .put(endpoint);
    }

    public Response delete(String endpoint, String cookie) {
        return RestAssured.given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .baseUri(baseUri)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .cookie("JSESSIONID", cookie)
                .log().all()
                .when()
                .delete(endpoint);
    }
}

