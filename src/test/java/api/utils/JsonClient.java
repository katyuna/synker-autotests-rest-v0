package api.utils;

import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

import java.util.List;

public class JsonClient {
    public static List<?> getValuesListFromResponseByJsonPath(Response response, String path){
        JsonPath jsonPath = response.jsonPath();
        List<String> valuesList = jsonPath.getList(path);
        System.out.println("List of values" + valuesList);
        return valuesList;
    }



}
