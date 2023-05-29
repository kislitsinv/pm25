package xyz.vkislitsin.pm25.job;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import xyz.vkislitsin.pm25.service.implementation.AqicnService;
import xyz.vkislitsin.pm25.service.implementation.IqAirService;

@Component
@RequiredArgsConstructor
@Getter
@Setter
public class UpdateProviderInstancesJob {
    private final AqicnService aqicnService;
    private final IqAirService iqAirService;

    @Scheduled(cron = "0 0 */2 * * ?") // every 2 hours
    @Transactional
    public void updateAqicnData() {
        aqicnService.updateLocalData(aqicnService.getRequestHeaders());
    }

    @Scheduled(cron = "0 0 */2 * * ?") // every 2 minutes (tests)
    @Transactional
    public void updateIqAirData() {
        iqAirService.updateLocalData(iqAirService.getRequestHeaders());
    }
}
