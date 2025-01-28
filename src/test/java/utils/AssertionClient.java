package utils;

import io.restassured.response.Response;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertAll;

public class AssertionClient {

    /**
     * Checking response status code
     *
     * @param response
     * @param expectedStatusCode
     */
    public static void checkStatusCode(Response response, int expectedStatusCode) {
        System.out.println("--- Checking response status code.");
        int statusCode = response.getStatusCode();
        if (statusCode == expectedStatusCode) {
            // Выводим сообщение в консоль, если статус совпадает
            System.out.println("Success: Expected status code " + expectedStatusCode + " received.");
        } else {
            // Если статус не совпадает, выбрасываем исключение
            throw new AssertionError("Error: Expected status code: " + expectedStatusCode + ", but got: " + statusCode);
        }
    }

    /**
     * Check response body is not null
     *
     * @param response
     */
    public static void checkResponseBodyIsNotNull(Response response) {
        System.out.println("--- Checking response body.");
        String responseBody = response.getBody().asString();
        if (responseBody != null) {
            System.out.println("Success: Response body is not null.");
        } else {
            throw new AssertionError("Error: Response body is null.");
        }
    }
    /**
     *
     * @param list
     */
    public static void checkValuesListNotEmpty(List<?> list) {
        System.out.println("--- Checking value list is not empty.");
        if (list.size() > 0) {
            System.out.println("Success: Value list is not empty.");
        } else {
            throw new AssertionError("Error: Response body is null.");
        }
    }

    /**
     * Checking wrong path error
     *
     * @param response
     * @param wrongPath
     */
    public static void checkWrongPathError(Response response, String wrongPath) {
        System.out.println("--- Checking response body error for wrong path.");
        assertAll(
                () -> response.then().body("status", equalTo(500)),
                () -> response.then().body("error", equalTo("No static resource " + wrongPath + ".")),
                () -> response.then().body("path", equalTo("/" + wrongPath))
        );
    }

    /**
     * Checking wrong content type error
     *
     * @param response
     * @param contentType
     */
    public static void checkUnsupportedContentTypeError(Response response, String contentType, String path) {
        System.out.println("--- Checking response body error for unsupported content type.");
        assertAll(
                () -> response.then().body("status", equalTo(500)),
                () -> response.then().body("error", equalTo("Content-Type '" + contentType + ";charset=ISO-8859-1' is not supported")),
                () -> response.then().body("path", equalTo(path))
        );
    }

    /**
     * Checking response body message
     *
     * @param response
     * @param bodyMessage
     */
    public static void checkResponseBodyMessage(Response response, String bodyMessage) {
        System.out.println("--- Checking response body message.");
        assertAll(
                () -> response.then().body(not(empty())),
                () -> response.then().body(containsString(bodyMessage))
        );
    }
}

