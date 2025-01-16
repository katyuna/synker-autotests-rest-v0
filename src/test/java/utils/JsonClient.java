package utils;

import io.qameta.allure.internal.shadowed.jackson.core.JsonProcessingException;
import io.qameta.allure.internal.shadowed.jackson.databind.JsonNode;
import io.qameta.allure.internal.shadowed.jackson.databind.ObjectMapper;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class JsonClient {

    /*  private static ObjectMapper objectMapper = new ObjectMapper();

    public static JsonPath getJsonPath(Response response) {
        JsonPath jsonPath = response.jsonPath();
        return jsonPath;
    }

 public static JsonNode makeJsonNodeTreeFromResponse (Response response) throws JsonProcessingException {
     String jsonResponse = response.getBody().asString();
     JsonNode rootNode = objectMapper.readTree(jsonResponse);
     return rootNode;
 }

     */


}
