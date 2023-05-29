package xyz.vkislitsin.pm25.service;

import org.springframework.http.HttpHeaders;

public interface AirDataProviderService {
    HttpHeaders getRequestHeaders();
    void updateLocalData(HttpHeaders headers);
}
