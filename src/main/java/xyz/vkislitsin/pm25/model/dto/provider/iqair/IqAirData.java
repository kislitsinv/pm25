package xyz.vkislitsin.pm25.model.dto.provider.iqair;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class IqAirData {
    @JsonProperty("coordinates")
    private IqAirCoordinates coordinates;
    @JsonProperty("geohash")
    private String title;
    @JsonProperty("aqi")
    private Double pm25;
}
