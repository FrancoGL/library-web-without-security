package com.beth.libraryweb.services;

import com.beth.libraryweb.entities.Book;
import com.beth.libraryweb.errors.ErrorService;
import com.beth.libraryweb.repositories.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private EditorialService editorialService;

    @Autowired
    private AuthorService authorService;

    @Transactional
    public void addBook(Long isbn, String title, Integer publicationYear, Integer copies,
                        String author, String editorial) throws ErrorService {

        validation(isbn,title,copies,publicationYear,author,editorial);

        Book entity = new Book();
        setAttributes(isbn,title,publicationYear,copies,author,editorial,entity);
        bookRepository.save(entity);
    }

    @Transactional
    public void updateBook(String id, long isbn ,String title, int publicationYear, int copies,
                           String author, String editorial ) throws ErrorService {

        Book book = bookRepository.findById(id).orElse(null);

        if(book == null) {
            throw new ErrorService("The book requested doesn't exist");
        }

        validation(isbn,title,publicationYear,copies,author,editorial);

        setAttributes(isbn,title,publicationYear,copies,author,editorial,book);
        bookRepository.save(book);
    }

    @Transactional(readOnly = true)
    public List<Book> getBooks() {
        return bookRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Book getBook(String id) {
        return bookRepository.getById(id);
    }

    @Transactional
    public void changeState(String id) {

        Optional<Book> state = bookRepository.findById(id);

        if(state.isPresent()) {
            Book book = state.get();
            book.setActive(!book.getActive());

            bookRepository.save(book);
        }
    }

    private void setAttributes(Long isbn ,String title, Integer publicationYear, Integer copies,
                               String author, String editorial, Book book) {

        book.setIsbn(isbn);
        book.setTitle(title);
        book.setPublicationYear(publicationYear);
        book.setCopies(copies);
        book.setLentCopies(0);
        book.setRemainingCopies(copies);
        book.setActive(true);
        book.setAuthor(authorService.getAuthor(author));
        book.setEditorial(editorialService.getEditorial(editorial));
    }

    private void validation(Long isbn, String title, Integer publicationYear,
                           Integer copies, String author, String editorial) throws ErrorService {

        if(isbn == null || isbn.toString().length() < 10) {
            throw new ErrorService("You have to provide a valid isbn");
        }
        if(title == null || title.length() < 5) {
            throw new ErrorService("You have to provide a title");
        }
        if(publicationYear.toString().length() < 4) {
            throw new ErrorService("You have to provide a valid year");
        }
        if(copies < 0) {
            throw new ErrorService("You have to provide a valid number");
        }
        if(author == null || author.length() < 5) {
            throw new ErrorService("You have to provide a valid author");
        }
        if(editorial == null || editorial.length() < 5) {
            throw new ErrorService("You have to provide a valid editorial");
        }
    }
}
