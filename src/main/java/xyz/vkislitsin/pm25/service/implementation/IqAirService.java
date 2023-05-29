package xyz.vkislitsin.pm25.service.implementation;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import xyz.vkislitsin.pm25.enums.AirDataProvider;
import xyz.vkislitsin.pm25.model.dto.provider.MapPm25Object;
import xyz.vkislitsin.pm25.model.dto.provider.iqair.IqAirData;
import xyz.vkislitsin.pm25.model.dto.provider.iqair.IqAirDto;
import xyz.vkislitsin.pm25.model.entity.AirQualityValue;
import xyz.vkislitsin.pm25.model.repository.AirQualityValueRepository;
import xyz.vkislitsin.pm25.service.AirDataProviderService;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@Qualifier("iqair")
@Getter
@Setter
@RequiredArgsConstructor
@Log4j2
public class IqAirService implements AirDataProviderService {

    @Value("${service.iqair.uri}")
    private String requestUri;
    private final RestTemplate restTemplate;
    private final AirQualityValueRepository repository;

    @Override
    public HttpHeaders getRequestHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", "application/json, text/plain, */*");
        headers.add("x-real-ip", "172.31.39.11");
        headers.add("user-location",
                "https://www.iqair.com/ru/air-quality-map/kazakhstan/almaty-qalasy/almaty");
        headers.add("User-Agent",
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/113.0");
        headers.add("Sec-Fetch-Dest", "empty");
        headers.add("Sec-Fetch-Mode", "cors");
        headers.add("Sec-Fetch-Site", "cross-site");
        headers.setOrigin("https://www.iqair.com");
        headers.add("Referer", "https://www.iqair.com/");
        headers.add("Accept-Language","ru-RU,ru;q=0.8,en-US;q=0.5,en;q=0.3");
        headers.add("Host", "website-api.airvisual.com");

        return headers;
    }

    @Override
    @Transactional
    public void updateLocalData(HttpHeaders headers) {
        HttpEntity<String> httpEntity = new HttpEntity<>(null, headers);
        ResponseEntity<IqAirDto> resp = restTemplate.exchange(requestUri, HttpMethod.GET, httpEntity,
                IqAirDto.class);

        if (resp.getStatusCode().is2xxSuccessful()) {
            repository.cleanProviderFromTable(AirDataProvider.IQAIR.toString());

            List<IqAirData> data = Objects.requireNonNull(resp.getBody()).getData();

            log.info("Got {} records from IQAIR, marking old records as incative...", data.size());

            List<AirQualityValue> entityList = new ArrayList<>();

            for (IqAirData record : data) {
                AirQualityValue entity = new AirQualityValue();
                entity.setLatitude(record.getCoordinates().getLatitude());
                entity.setLongitude(record.getCoordinates().getLongitude());
                entity.setPm10Value(null); //iqair hasn't got such ones
                entity.setPm25Value(record.getPm25());
                entity.setTitle(record.getTitle());
                entity.setProviderName(AirDataProvider.IQAIR);
                entityList.add(entity);
            }
            repository.saveAll(entityList);
            log.info("Successfully saved {} new IQAIR records to DB", data.size());
        }
    }

    @Override
    public List<MapPm25Object> getActiveLocations() {
        List<AirQualityValue> entityList = repository.findAllByActiveTrueAndProviderName(AirDataProvider.IQAIR);
        List<MapPm25Object> dtoList = new ArrayList<>();

        if (entityList.size() > 0) {
            for (AirQualityValue entity : entityList) {
                MapPm25Object object = new MapPm25Object();
                object.setPm25(entity.getPm25Value());
                object.setTitle(entity.getTitle());
                object.setLatitude(entity.getLatitude());
                object.setLongitude(entity.getLongitude());
                object.setLastUpdate(entity.getUpdatedAt());
                dtoList.add(object);
            }
        }

        return dtoList;
    }
}
