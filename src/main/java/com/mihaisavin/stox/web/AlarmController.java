package com.mihaisavin.stox.web;

import com.mihaisavin.stox.domain.Alarm;
import com.mihaisavin.stox.domain.User;
import com.mihaisavin.stox.domain.ValidationException;
import com.mihaisavin.stox.service.AlarmService;
import com.mihaisavin.stox.service.EmailService;
import com.mihaisavin.stox.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserService userService;

    @RequestMapping("")
    public ModelAndView index(@AuthenticationPrincipal User user) {

        long userId = user.getId();

        Collection<Alarm> alarms = alarmService.getAllAlarms(userId);
        Alarm newAlarm = new Alarm();

        ModelAndView modelAndView = new ModelAndView("app");
        modelAndView.addObject("alarmsList", alarms);
        modelAndView.addObject("alarm", newAlarm);

        return modelAndView;
    }

    @RequestMapping(value = "/alarm", method = RequestMethod.GET, params = "action=add")
    public ModelAndView addAlarm() {
        Alarm alarm = new Alarm();
        return new ModelAndView("alarm").addObject(alarm);
    }

    @RequestMapping(value = "/alarm", method = RequestMethod.GET, params = "action=edit")
    public ModelAndView editAlarm(@RequestParam("id") Long id) {
        Alarm alarm = alarmService.get(id);
        return new ModelAndView("alarm").addObject(alarm);
    }

    @RequestMapping(value = "/alarm", method = RequestMethod.GET, params = "action=delete")
    public ModelAndView deleteAlarm(@AuthenticationPrincipal User user, @RequestParam("id") Long id) {
        alarmService.delete(id);
        return index(user);
    }

    @RequestMapping(value = "/alarm", method = RequestMethod.POST)
    public ModelAndView saveAlarm(@AuthenticationPrincipal User user, @ModelAttribute Alarm alarm) {
        alarm.setOwnerId(user.getId());

        try {
            alarmService.save(alarm);
        } catch (ValidationException e) {
            e.printStackTrace();
            return editAlarm(alarm.getId());
        }

        emailService.sendEmail(userService.getEmailByUserId(user.getId()),
                "Alarm for " + alarm.getSymbol() + " has been set at " +
                        alarm.getOriginalValue() * (100 + alarm.getVariance()) / 100);

        return index(user);
    }
}