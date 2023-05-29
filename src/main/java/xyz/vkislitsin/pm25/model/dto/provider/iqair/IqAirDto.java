package xyz.vkislitsin.pm25.model.dto.provider.iqair;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import xyz.vkislitsin.pm25.model.dto.provider.aqicn.AqicnData;

import java.util.ArrayList;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class IqAirDto {
    @JsonProperty("markers")
    private List<IqAirData> data = new ArrayList<>();
}
