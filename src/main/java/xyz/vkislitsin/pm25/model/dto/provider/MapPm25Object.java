package xyz.vkislitsin.pm25.model.dto.provider;

import lombok.Data;

import java.time.ZonedDateTime;

@Data
public class MapPm25Object {
    private double latitude;
    private double longitude;
    private String title;
    private double pm25;
    private ZonedDateTime lastUpdate;
}
