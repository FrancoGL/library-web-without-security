package com.beth.libraryweb.controllers;

import com.beth.libraryweb.entities.Editorial;
import com.beth.libraryweb.services.EditorialService;;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
@RequestMapping("/editorial")
public class EditorialController {

    @Autowired
    private EditorialService editorialService;

    @GetMapping("/editorial-table")
    public ModelAndView editorials(HttpServletRequest request) {
        ModelAndView mav = new ModelAndView("editorial");
        Map<String, ?> map = RequestContextUtils.getInputFlashMap(request);

        if(map != null) {
            mav.addObject("success",map.get("success-name"));
            mav.addObject("error",map.get("error-name"));
        }

        mav.addObject("editorials",editorialService.getEditorials());
        return mav;
    }

    @GetMapping("/editorial-form")
    public ModelAndView addEditorial() {
        ModelAndView mav = new ModelAndView("editorial-form");
        mav.addObject("editorial", new Editorial());
        mav.addObject("title","Add Editorial");
        mav.addObject("action","save-editorial");
        return mav;
    }

    @PostMapping("/save-editorial")
    public RedirectView saveEditorial(@RequestParam String name, RedirectAttributes attributes) {

        try {
            editorialService.addEditorial(name);
            attributes.addFlashAttribute("success-name","Editorial added correctly");
        } catch (Exception e) {
            attributes.addFlashAttribute("error-name",e.getMessage());
        }
        return new RedirectView("/editorial/editorial-table");
    }

    @GetMapping("/edit-editorial/{id}")
    public ModelAndView editEditorial(@PathVariable String id) {
        ModelAndView mav = new ModelAndView("editorial-form");
        mav.addObject("editorial",editorialService.getEditorial(id));
        mav.addObject("title","Update Editorial");
        mav.addObject("action","update-editorial");
        return mav;
    }

    @PostMapping("/update-editorial")
    public RedirectView updateEditorial(@RequestParam String id, @RequestParam String name, RedirectAttributes attributes) {

        try {
            editorialService.updateEditorial(id,name);
            attributes.addFlashAttribute("success-name","Editorial updated correctly");
        } catch (Exception e) {
            attributes.addFlashAttribute("error-name",e.getMessage());
        }
        return new RedirectView("/editorial/editorial-table");
    }

    @PostMapping("/toggle-state/{id}")
    public RedirectView toggleState(@PathVariable String id) {
        editorialService.changeState(id);
        return new RedirectView("/editorial/editorial-table");
    }
}
