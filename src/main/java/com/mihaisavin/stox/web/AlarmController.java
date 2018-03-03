package com.mihaisavin.stox.web;

import com.mihaisavin.stox.domain.Alarm;
import com.mihaisavin.stox.service.AlarmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.Collection;

@Controller
@RequestMapping("/app")
public class AlarmController {

    @Autowired
    private AlarmService alarmService;

    @RequestMapping("")
    public ModelAndView index() {
        Collection<Alarm> alarms = alarmService.getAllAlarms();

        ModelAndView modelAndView = new ModelAndView("alarms");
        modelAndView.addObject("alarms", alarms);

        return modelAndView;
    }

    @RequestMapping(method = RequestMethod.GET, params = "action=add")
    public String addAlarm() {
        return "alarm_edit";
    }

    @RequestMapping(method = RequestMethod.GET, params = "action=edit")
    public String editAlarm(@RequestParam("id") Long id) {
        return "alarm_edit";
    }
}