package com.mihaisavin.stox.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {

    @RequestMapping("/")
    String index() {
        return "index";
    }

    @RequestMapping("/app")
    String app() {
        return "app";
    }

}
