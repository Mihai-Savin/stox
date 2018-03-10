package com.mihaisavin.stox.web;

import com.mihaisavin.stox.domain.User;
import com.mihaisavin.stox.domain.ValidationException;
import com.mihaisavin.stox.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class AppController {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    @Autowired
    UserService userService;

    @RequestMapping("/")
    String index() {
        return "index";
    }

    @RequestMapping("/login")
    String serveLoginPage() {
        return "login";
    }

    @RequestMapping("/register")
    String showRegistrationForm() {
        return "register";
    }

    @RequestMapping(value="/register", method = RequestMethod.POST)
    String register(@ModelAttribute User user) {
        LOGGER.info("Registering user " + user.getEmail());

        try {
            userService.save(user);
        } catch (ValidationException e) {
            e.printStackTrace();
            return "register";
        }

        return "login";
    }
}