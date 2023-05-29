package xyz.vkislitsin.pm25.job;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import xyz.vkislitsin.pm25.enums.AirDataProvider;
import xyz.vkislitsin.pm25.service.AirDataProviderService;
import xyz.vkislitsin.pm25.service.factory.ProviderInstance;

@Component
@RequiredArgsConstructor
@Getter
@Setter
public class UpdateProviderInstancesJob {

    private final ProviderInstance providerInstance;

    @Scheduled(cron = "0 0 */2 * * ?")
    @Transactional
    public void updateAqicnData() {
        AirDataProviderService service = providerInstance.getProvider(AirDataProvider.AQICN);
        service.updateLocalData(service.getRequestHeaders());
    }
}
