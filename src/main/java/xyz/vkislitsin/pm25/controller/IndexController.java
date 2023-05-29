package xyz.vkislitsin.pm25.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;
import xyz.vkislitsin.pm25.enums.AirDataProvider;
import xyz.vkislitsin.pm25.model.dto.provider.MapPm25Object;
import xyz.vkislitsin.pm25.model.repository.AirQualityValueRepository;
import xyz.vkislitsin.pm25.service.AirDataProviderService;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Controller
@Getter
@Setter
public class IndexController {

    @Autowired
    @Qualifier("iqair")
    private AirDataProviderService iqairService;

    @Autowired
    @Qualifier("aqicn")
    private AirDataProviderService aqicnService;

    @Autowired
    private AirQualityValueRepository repository;

    @GetMapping("/")
    public ModelAndView index(ModelMap model) {

        return new ModelAndView("redirect:/aqicn", model);
    }

    @GetMapping("/aqicn")
    public String aqicn(Model model, HttpServletRequest request) {

        int todaysAvg = repository.getTodaysAverage(AirDataProvider.AQICN.toString()).intValue();

        List<MapPm25Object> aqicnList =  aqicnService.getActiveLocations();
        model.addAttribute("aqicnLocations", aqicnList);
        model.addAttribute("url", request.getRequestURI());
        model.addAttribute("lastUpdate", zdtToAlmatyTime(aqicnList.get(0).getLastUpdate()));
        model.addAttribute("todayAvgValue", todaysAvg);
        model.addAttribute("todayAvgBg", getBgFromPmValue(todaysAvg));

        return "index";
    }

    @GetMapping("/iqair")
    public String iqair(Model model, HttpServletRequest request) {

        int todaysAvg = repository.getTodaysAverage(AirDataProvider.IQAIR.toString()).intValue();

        List<MapPm25Object> iqAirList =  iqairService.getActiveLocations();
        model.addAttribute("iqAirLocations", iqAirList);
        model.addAttribute("url", request.getRequestURI());
        model.addAttribute("lastUpdate", zdtToAlmatyTime(iqAirList.get(0).getLastUpdate()));
        model.addAttribute("todayAvgValue", todaysAvg);
        model.addAttribute("todayAvgBg", getBgFromPmValue(todaysAvg));

        return "index";
    }

    private String zdtToAlmatyTime(ZonedDateTime zdt) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm");
        ZonedDateTime zdt2 = zdt.plus(6, ChronoUnit.HOURS);
        return zdt2.format(formatter);
    }

    private String getBgFromPmValue(int val) {
        if (val < 50) {
            return "bg-success";
        } else if (val < 100) {
            return "bg-warning";
        } else if (val < 130) {
            return "bg-danger";
        } else {
            return "bg-dark";
        }
    }
}
