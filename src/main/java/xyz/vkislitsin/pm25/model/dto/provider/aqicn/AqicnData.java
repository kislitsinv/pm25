package xyz.vkislitsin.pm25.model.dto.provider.aqicn;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class AqicnData {
    @JsonProperty("g")
    private List<Double> coordinates;
    @JsonProperty("n")
    private String title;
    @JsonProperty("c")
    private Double pm10;
    @JsonProperty("a")
    private Double pm25;
}
