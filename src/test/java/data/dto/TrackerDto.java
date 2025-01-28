package data.dto;

import io.qameta.allure.internal.shadowed.jackson.annotation.JsonIgnoreProperties;
import io.qameta.allure.internal.shadowed.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class TrackerDto {

    @JsonProperty(value = "createdBy")
    private String createdBy;

    @JsonProperty(value = "id")
    private Integer id;

    @JsonProperty(value = "trackerName")
    private String trackerName;

    @JsonProperty(value = "created")
    private String created;

    @JsonProperty(value = "type")
    private String type;

    @JsonProperty(value = "trackerUrl")
    private String trackerUrl;

    @JsonProperty(value = "projectId")
    private String projectId;

    @JsonProperty(value = "username")
    private String username;

    @JsonProperty(value = "password")
    private String password;

    @JsonProperty(value = "everHourKey")
    private String everHourKey;

    @JsonProperty(value = "properties")
    private Map<String, String> properties;
}
