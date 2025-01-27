package api.user_mapping_controller;

import data.dto.UserMappingDto;
import io.qameta.allure.internal.shadowed.jackson.databind.JsonNode;
import io.qameta.allure.internal.shadowed.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import utils.RestClient;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class TestDataBuilder {
    private final RestClient restClient = new RestClient(RestAssured.baseURI);
    private final ObjectMapper objectMapper = new ObjectMapper();

    String loadTestData(String key) throws IOException {
        File file = new File("src/test/resources/UserMappingTestData.json");
        JsonNode rootNode = objectMapper.readTree(file);
        JsonNode testData = rootNode.get(key);
        if (testData == null) {
            throw new IllegalArgumentException("Test data with key '" + key + "' not found in UserMappingTestData.json");
        }
        return testData.toString();
    }

    protected String create(String key, String authCookie) throws IOException {
        String requestBody = loadTestData(key);
        Response createResponse = restClient.post("/api/v1/mapping/user", requestBody, authCookie);
        createResponse.then().statusCode(200);
        return createResponse.jsonPath().getString("id");
    }

    protected UserMappingDto createUserMapping(String key, String authCookie) throws IOException {
        String requestBody = loadTestData(key);
        Response createResponse = restClient.post("/api/v1/mapping/user", requestBody, authCookie);
        createResponse.then().statusCode(200);
        return objectMapper.readValue(createResponse.asString(), UserMappingDto.class);
    }

    UserMappingDto getMappingFromApi(String mappingId, String authCookie) {
        Response response = getResponse(mappingId, authCookie);
        response.then()
                .log().all()
                .statusCode(200);

        return response.as(UserMappingDto.class);
    }

    protected int getStatusCode(String mappingId, String authCookie) {
        return getResponse(mappingId, authCookie).getStatusCode();
    }

    protected Response getResponse(String mappingId, String authCookie) {
        return restClient.get("/api/v1/mapping/user/" + mappingId, Map.of(), authCookie);
    }

    public void deleteMapping(String mappingId, String authCookie) {
        Response response = restClient.delete("/api/v1/mapping/user/" + mappingId, authCookie);
        System.out.println("Response status: " + response.statusCode());
        System.out.println("Response body: " + response.body().asString());
    }
    public UserMappingDto update(String key, String mappingId, String authCookie) throws IOException {
        // Загрузить данные для обновления
        String requestBody = loadTestData(key);
        System.out.println("Loaded Test Data: " + requestBody);

        // Выполнить запрос PUT для обновления данных
        System.out.println("PUT URL: /api/v1/mapping/user/" + mappingId);
        System.out.println("PUT Request Body: " + requestBody);
        System.out.println("Auth Cookie: " + authCookie);
        UserMappingDto userMappingDto =  objectMapper.readValue(requestBody, UserMappingDto.class);
        userMappingDto.setId(mappingId);
        Response updateResponse = restClient.put("/api/v1/mapping/user", userMappingDto, authCookie);

        updateResponse.then().statusCode(200);

        return updateResponse.as(UserMappingDto.class);
    }

}
