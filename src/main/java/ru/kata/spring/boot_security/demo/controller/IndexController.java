package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import ru.kata.spring.boot_security.demo.service.UserService;

import javax.security.auth.login.LoginException;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/")
public class IndexController {
    private UserService userService;

    @Autowired
    public IndexController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("")
    public String welcomePage(Model model, HttpSession session,
                              @SessionAttribute(required = false, name = "Authentication-Exception") LoginException authenticationException,
                              @SessionAttribute(required = false, name = "Authentication-Name") String authenticationName) {
        userService.tryIndex(model, session, authenticationException, authenticationName);

        return "index";
    }
}