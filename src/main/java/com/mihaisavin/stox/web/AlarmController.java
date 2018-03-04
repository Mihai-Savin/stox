package com.mihaisavin.stox.web;

import com.mihaisavin.stox.domain.Alarm;
import com.mihaisavin.stox.domain.ValidationException;
import com.mihaisavin.stox.service.AlarmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
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

        ModelAndView modelAndView = new ModelAndView("app");
        modelAndView.addObject("alarmsList", alarms);

        return modelAndView;
    }

    @RequestMapping(value="/alarm", method = RequestMethod.GET, params = "action=add")
    public ModelAndView addAlarm() {
        return new ModelAndView("alarm").addObject( new Alarm());
    }

    @RequestMapping(method = RequestMethod.GET, params = "action=edit")
    public String editAlarm(@RequestParam("id") Long id) {
        return "alarm";
    }

    @RequestMapping(value="/alarm", method = RequestMethod.POST)
    public ModelAndView saveAlarm(@ModelAttribute Alarm alarm){
        try {
            alarmService.save(alarm);
        } catch (ValidationException e) {
            e.printStackTrace();
            return addAlarm();
        }

        return index();
    }
}