package com.beth.libraryweb.services;

import com.beth.libraryweb.entities.Author;
import com.beth.libraryweb.errors.ErrorService;
import com.beth.libraryweb.repositories.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class AuthorService {

    @Autowired
    private AuthorRepository authorRepository;

    @Transactional
    public void addAuthor(String name) throws ErrorService {

        validation(name);

        Optional<Author> state = Optional.ofNullable(authorRepository.findAuthorByName(name));

        if(state.isPresent()) {
            throw new ErrorService("Author already exist.");
        } else {
            Author author = new Author();
            author.setName(name);
            author.setActive(true);
            authorRepository.save(author);
        }
    }

    @Transactional
    public void updateAuthor(String id, String name) throws ErrorService {

        Author author = authorRepository.findById(id).orElse(null);

        if(author == null) {
            throw new ErrorService("The author requested doesn't exist.");
        }

        validation(name);

        author.setName(name);
        authorRepository.save(author);

    }

    @Transactional(readOnly = true)
    public Author getAuthor(String id) {

        return authorRepository.getById(id);
    }

    @Transactional(readOnly = true)
    public List<Author> getAuthors() {
        return authorRepository.findAll();
    }

    @Transactional
    public void changeState(String id) {

        Optional<Author> state = authorRepository.findById(id);

        if(state.isPresent()) {
            Author author = state.get();
            author.setActive(!author.getActive());
            authorRepository.save(author);
        }
    }

    private void validation(String name) throws ErrorService {
        if(name == null || name.length() < 3) {
            throw new ErrorService("You have to provide a name");
        }
    }
}
