package tw.liangze.backend.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {

    @GetMapping(value = {"/", "/{path:[^\\.]*}"})
    public String forward() {
        return "forward:/index.html";
    }

}
