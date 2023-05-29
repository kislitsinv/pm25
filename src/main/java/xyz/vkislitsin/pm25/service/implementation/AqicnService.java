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
import xyz.vkislitsin.pm25.model.dto.provider.aqicn.AqicnData;
import xyz.vkislitsin.pm25.model.dto.provider.aqicn.AqicnDto;
import xyz.vkislitsin.pm25.model.entity.AirQualityValue;
import xyz.vkislitsin.pm25.model.repository.AirQualityValueRepository;
import xyz.vkislitsin.pm25.service.AirDataProviderService;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@Qualifier("aqicn")
@Getter
@Setter
@RequiredArgsConstructor
@Log4j2
public class AqicnService implements AirDataProviderService {

    @Value("${service.aqicn.uri}")
    private String requestUri;
    private final RestTemplate restTemplate;
    private final AirQualityValueRepository repository;

    @Override
    public HttpHeaders getRequestHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Accept", "*/*");
        headers.add("User-Agent",
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/113.0");
        headers.add("Sec-Fetch-Dest", "empty");
        headers.add("Sec-Fetch-Mode", "cors");
        headers.add("Sec-Fetch-Site", "cross-site");
        headers.setOrigin("https://aqicn.org");
        headers.add("Content-Type",
                "multipart/form-data; boundary=---------------------------232954295820643828664025889788");
        headers.add("Referer", "https://aqicn.org/");
        headers.add("Accept-Language","ru-RU,ru;q=0.8,en-US;q=0.5,en;q=0.3");
        headers.add("Host", "airnet.waqi.info");

        return headers;
    }

    @Override
    @Transactional
    public void updateLocalData(HttpHeaders headers) {
        HttpEntity<String> httpEntity = new HttpEntity<>(getRequestBody(), headers);
        ResponseEntity<AqicnDto> resp = restTemplate.exchange(requestUri, HttpMethod.POST, httpEntity,
                AqicnDto.class);

        if (resp.getStatusCode().is2xxSuccessful()) {
            repository.cleanProviderFromTable(AirDataProvider.AQICN.toString());

            List<AqicnData> data = Objects.requireNonNull(resp.getBody()).getData();

            log.info("Got {} records from AQICN, marking old records as incative...", data.size());

            List<AirQualityValue> entityList = new ArrayList<>();

            for (AqicnData record : data) {
                AirQualityValue entity = new AirQualityValue();
                entity.setLatitude(record.getCoordinates().get(0));
                entity.setLongitude(record.getCoordinates().get(1));
                entity.setPm10Value(record.getPm10());
                entity.setPm25Value(record.getPm25());
                entity.setTitle(record.getTitle());
                entity.setProviderName(AirDataProvider.AQICN);
                entityList.add(entity);
            }
            repository.saveAll(entityList);
            log.info("Successfully saved {} new AQICN records to DB", data.size());
        }
    }

    @Override
    public List<MapPm25Object> getActiveLocations() {
        List<AirQualityValue> entityList = repository.findAllByActiveTrueAndProviderName(AirDataProvider.AQICN);
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

    private String getRequestBody() {
        return "-----------------------------232954295820643828664025889788\n" +
                "Content-Disposition: form-data; name=\"bounds\"\n" +
                "\n" +
                "75.87173886562088,43.05323028392481,78.04887139374681,43.45247861226447\n" +
                "-----------------------------232954295820643828664025889788\n" +
                "Content-Disposition: form-data; name=\"zoom\"\n" +
                "\n" +
                "12\n" +
                "-----------------------------232954295820643828664025889788\n" +
                "Content-Disposition: form-data; name=\"xscale\"\n" +
                "\n" +
                "1532.3825981654\n" +
                "-----------------------------232954295820643828664025889788\n" +
                "Content-Disposition: form-data; name=\"width\"\n" +
                "\n" +
                "2383\n" +
                "-----------------------------232954295820643828664025889788\n" +
                "Content-Disposition: form-data; name=\"time\"\n" +
                "\n" +
                Instant.now().toString() +
                "\n" +
                "-----------------------------232954295820643828664025889788--";
    }
}
