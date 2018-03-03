package com.mihaisavin.stox.web;

import com.mihaisavin.stox.domain.Alarm;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

@Controller
public class IndexController {

    @RequestMapping("/")
    String index() {
        return "index";
    }

    @RequestMapping("/test")
    ModelAndView app() {

        ModelAndView modelAndView = new ModelAndView("app");

        List<Alarm> alarmsList = generateMockAlarms();

        modelAndView.addObject("alarmsList", alarmsList);

        return modelAndView;
    }

    private List<Alarm> generateMockAlarms() {
        List<Alarm> alarmsList = new ArrayList<>();

        alarmsList.add(new Alarm("MSFT", 95.565, 1));
        alarmsList.add(new Alarm("AAPL", 180.33, 2));
        alarmsList.add(new Alarm("FB", 181.99, 3));
        alarmsList.add(new Alarm("ORCL", 51.385, -10));

        return alarmsList;
    }

}
