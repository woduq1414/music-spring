package dimigo.test.MusicSpring.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    //인덱스 페이지
    @GetMapping("/")
    public String home() {
        return "home";
    }
}
