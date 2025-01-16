package utils;

import io.restassured.response.Response;

import static org.hamcrest.Matchers.not;

public class AssertionClient {

    /**
     * Checking response status code
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

    //


}


