package xyz.vkislitsin.pm25.service.factory;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import xyz.vkislitsin.pm25.enums.AirDataProvider;
import xyz.vkislitsin.pm25.model.repository.AirQualityValueRepository;
import xyz.vkislitsin.pm25.service.AirDataProviderService;
import xyz.vkislitsin.pm25.service.implementation.AqicnService;

@Component
@RequiredArgsConstructor
public class ProviderInstance {

    @Value("${service.aqicn.uri}")
    private String aqicnRequestUri;

    private final AirQualityValueRepository airQualityValueRepository;
    private final RestTemplate restTemplate;

    public AirDataProviderService getProvider(AirDataProvider value) {
        return switch (value) {
            case AQICN -> new AqicnService(restTemplate, aqicnRequestUri, airQualityValueRepository);
            default -> new AqicnService(restTemplate, aqicnRequestUri, airQualityValueRepository);
        };
    }
}
