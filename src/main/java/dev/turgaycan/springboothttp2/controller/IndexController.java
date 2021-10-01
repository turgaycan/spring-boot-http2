package dev.turgaycan.springboothttp2.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

    @GetMapping({""})
    public String redirect() {
        return "forward:/";
    }

    @GetMapping({"/", "/index"})
    public String index(final Model indexModel) {
        indexModel.addAttribute("fullname", "Turgay Can");
        return "index";
    }
}
