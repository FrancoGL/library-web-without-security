package com.beth.libraryweb.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class MainController {

    @GetMapping
    public ModelAndView home() {
        return new ModelAndView("index");
    }
}
