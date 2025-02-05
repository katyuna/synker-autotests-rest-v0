package api.data.dto;

import io.qameta.allure.internal.shadowed.jackson.annotation.JsonFormat;
import io.qameta.allure.internal.shadowed.jackson.annotation.JsonIgnoreProperties;
import io.qameta.allure.internal.shadowed.jackson.annotation.JsonProperty;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class SyncDto {

    @JsonProperty(value = "createdBy")
    private String createdBy;

    @JsonProperty(value = "id")
    private Integer id;

    @JsonProperty(value = "created")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS")
    private String created;

    @JsonProperty(value = "taskName")
    private String taskName;

    @JsonProperty(value = "source")
    private TrackerDto source;

    @JsonProperty(value = "destination")
    private TrackerDto destination;

    @JsonProperty(value = "filter")
    private String filter;

    @JsonProperty(value = "comment")
    private String comment;

    @JsonProperty(value = "syncTime")
    private Boolean syncTime;

    @JsonProperty(value = "syncComments")
    private Boolean syncComments;

    @JsonProperty(value = "allowSync")
    private Boolean allowSync;

    @JsonProperty(value = "syncPeriod")
    private String syncPeriod;
}
