package com.beth.libraryweb.controllers;

import com.beth.libraryweb.entities.Author;
import com.beth.libraryweb.entities.Book;
import com.beth.libraryweb.services.AuthorService;
import com.beth.libraryweb.services.BookService;
import com.beth.libraryweb.services.EditorialService;
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
@RequestMapping("/book")
public class BookController {

    @Autowired
    private BookService bookService;

    @Autowired
    private AuthorService authorService;

    @Autowired
    private EditorialService editorialService;

    @GetMapping("/book-table")
    public ModelAndView books(HttpServletRequest request) {
        ModelAndView mav = new ModelAndView("book");
        Map<String, ?> map = RequestContextUtils.getInputFlashMap(request);

        if(map != null) {
            mav.addObject("success",map.get("success-action"));
            mav.addObject("error",map.get("error-action"));
        }

        mav.addObject("books",bookService.getBooks());
        return mav;
    }

    @GetMapping("/book-form")
    public ModelAndView addBook() {
        ModelAndView mav = new ModelAndView("book-form");
        mav.addObject("book",new Book());
        mav.addObject("authors",authorService.getAuthors());
        mav.addObject("editorials",editorialService.getEditorials());
        mav.addObject("title","Add Book");
        mav.addObject("action","save-book");
        return mav;
    }

    @PostMapping("/save-book")
    public RedirectView saveBook(@RequestParam long isbn, @RequestParam String title,
                                 @RequestParam int publicationYear, @RequestParam int copies,
                                 @RequestParam String author_id, @RequestParam String editorial_id,
                                 RedirectAttributes attributes) {

        try {
            bookService.addBook(isbn,title,publicationYear,copies,author_id,editorial_id);
            attributes.addFlashAttribute("success-action","Book added correctly");
        } catch (Exception e) {
            attributes.addFlashAttribute("error-action",e.getMessage());
        }
        return new RedirectView("/book/book-table");
    }

    @GetMapping("/edit-book/{id}")
    public ModelAndView editBook(@PathVariable String id) {
        ModelAndView mav = new ModelAndView("book-form");
        mav.addObject("book",bookService.getBook(id));
        mav.addObject("authors",authorService.getAuthors());
        mav.addObject("editorials",editorialService.getEditorials());
        mav.addObject("title","Update Book");
        mav.addObject("action","update-book");
        return mav;
    }

    @PostMapping("/update-book")
    public RedirectView updateBook(@RequestParam String id,@RequestParam long isbn,@RequestParam String title,
                                   @RequestParam int publicationYear, @RequestParam int copies,
                                   @RequestParam String author_id, @RequestParam String editorial_id,
                                   RedirectAttributes attributes) {
        try {
            bookService.updateBook(id,isbn,title,publicationYear,copies,author_id,editorial_id);
            attributes.addFlashAttribute("success-action","Book updated correctly");
        } catch (Exception e) {
            attributes.addFlashAttribute("error-action",e.getMessage());
        }
        return new RedirectView("/book/book-table");
    }

    @PostMapping("toggle-state/{id}")
    public RedirectView toggleState(@PathVariable String id) {
        bookService.changeState(id);
        return new RedirectView("/book/book-table");
    }
}
