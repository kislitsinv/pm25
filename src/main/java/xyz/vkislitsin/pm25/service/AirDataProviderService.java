package xyz.vkislitsin.pm25.service;

import org.springframework.http.HttpHeaders;
import xyz.vkislitsin.pm25.model.dto.provider.MapPm25Object;

import java.util.List;

public interface AirDataProviderService {
    HttpHeaders getRequestHeaders();
    void updateLocalData(HttpHeaders headers);
    List<MapPm25Object> getActiveLocations();
}
