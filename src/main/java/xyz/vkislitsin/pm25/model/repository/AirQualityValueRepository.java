package xyz.vkislitsin.pm25.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import xyz.vkislitsin.pm25.enums.AirDataProvider;
import xyz.vkislitsin.pm25.model.entity.AirQualityValue;

import java.util.List;

public interface AirQualityValueRepository extends JpaRepository<AirQualityValue, Long> {
    @Modifying
    @Query(value = "UPDATE air_quality_value SET active = false WHERE provider_name = ?1", nativeQuery = true)
    void cleanProviderFromTable(String provider);

    List<AirQualityValue> findAllByActiveTrueAndProviderName(AirDataProvider provider);

    @Query(value = "SELECT AVG(pm_25_value) FROM air_quality_value WHERE provider_name = ?1 AND DATE(created_at) = DATE(now())", nativeQuery = true)
    Double getTodaysAverage(String provider);
}
