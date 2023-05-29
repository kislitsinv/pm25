package xyz.vkislitsin.pm25.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import xyz.vkislitsin.pm25.enums.AirDataProvider;

import java.time.ZonedDateTime;

@Entity
@Table(name = "air_quality_value")
@Getter
@Setter
public class AirQualityValue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "latitude", nullable = false)
    private Double latitude;

    @Column(name = "longitude", nullable = false)
    private Double longitude;

    @Column(name = "title")
    private String title;

    @Column(name = "pm_25_value", nullable = false)
    private Double pm25Value;

    @Column(name = "pm_10_value")
    private Double pm10Value;

    @Column(name = "provider_name", nullable = false)
    @Enumerated(EnumType.STRING)
    private AirDataProvider providerName;

    @Column(name = "active")
    private boolean active = true;

    @Column(name = "created_at")
    @CreationTimestamp
    private ZonedDateTime createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private ZonedDateTime updatedAt;
}
