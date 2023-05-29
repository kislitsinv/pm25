package xyz.vkislitsin.pm25.controller;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
@Getter
@Setter
public class IndexController {

    @GetMapping("/")
    public String index() {
        return "index";
    }
}
