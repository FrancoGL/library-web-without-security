package com.beth.libraryweb.controllers;

import com.beth.libraryweb.entities.Author;
import com.beth.libraryweb.services.AuthorService;
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
@RequestMapping("/author")
public class AuthorController {

    @Autowired
    private AuthorService authorService;

    @GetMapping("/author-table")
    public ModelAndView authors(HttpServletRequest request) {
        ModelAndView mav = new ModelAndView("author");
        Map<String, ?> map = RequestContextUtils.getInputFlashMap(request);

        if(map != null) {
            mav.addObject("success",map.get("success-name"));
            mav.addObject("error",map.get("error-name"));
        }

        mav.addObject("authors",authorService.getAuthors());
        return mav;
    }

    @GetMapping("/author-form")
    public ModelAndView addAuthor() {
        ModelAndView mav = new ModelAndView("author-form");
        mav.addObject("author",new Author());
        mav.addObject("title","Add Author");
        mav.addObject("action","save-author");
        return mav;
    }

    @PostMapping("/save-author")
    public RedirectView saveAuthor(@RequestParam String name, RedirectAttributes attributes) {

        try {
            authorService.addAuthor(name);
            attributes.addFlashAttribute("success-name","Author added correctly");
            return new RedirectView("/author/author-table");
        } catch (Exception e) {
            attributes.addFlashAttribute("error-name",e.getMessage());
            return new RedirectView("/author/author-table");
        }
    }

    @GetMapping("/edit-author/{id}")
    public ModelAndView editAuthor(@PathVariable String id) {
        ModelAndView mav = new ModelAndView("author-form");
        mav.addObject("author",authorService.getAuthor(id));
        mav.addObject("title","Edit Author");
        mav.addObject("action","update-author");
        return mav;
    }

    @PostMapping("/update-author")
    public RedirectView updateAuthor(@RequestParam String id, @RequestParam String name, RedirectAttributes attributes) {
        try {
            authorService.updateAuthor(id,name);
            attributes.addFlashAttribute("success-name","Author updated correctly");
        } catch (Exception e) {
            attributes.addFlashAttribute("error-name",e.getMessage());
        }
            return new RedirectView("/author/author-table");
    }

    @PostMapping("/toggle-state/{id}")
    public RedirectView toggleState(@PathVariable String id) {
        authorService.changeState(id);
        return new RedirectView("/author/author-table");
    }
}
