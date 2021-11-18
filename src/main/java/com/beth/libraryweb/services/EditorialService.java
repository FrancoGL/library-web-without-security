package com.beth.libraryweb.services;

import com.beth.libraryweb.entities.Book;
import com.beth.libraryweb.entities.Editorial;
import com.beth.libraryweb.errors.ErrorService;
import com.beth.libraryweb.repositories.EditorialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class EditorialService {

    @Autowired
    private EditorialRepository editorialRepository;


    @Transactional
    public void addEditorial(String name) throws ErrorService {

        validation(name);

        Optional<Editorial> state = Optional.ofNullable(editorialRepository.findEditorialByName(name));

        if(state.isPresent()) {
            throw new ErrorService("Editorial already exist.");
        } else {
            Editorial editorial = new Editorial();
            editorial.setName(name);
            editorial.setActive(true);
            editorialRepository.save(editorial);
        }
    }

    @Transactional
    public void updateEditorial(String id, String name) throws ErrorService {

        Editorial editorial = editorialRepository.findById(id).orElse(null);

        if(editorial == null) {
            throw new ErrorService("The editorial requested doesn't exist.");
        }

        validation(name);

        editorial.setName(name);
        editorialRepository.save(editorial);

    }

    @Transactional
    public void changeState(String id) {

        Optional<Editorial> state = editorialRepository.findById(id);

        if(state.isPresent()) {
            Editorial editorial = state.get();
            editorial.setActive(!editorial.getActive());
            editorialRepository.save(editorial);
        }
    }

    @Transactional(readOnly = true)
    public Editorial getEditorial(String id) {
        return editorialRepository.getById(id);
    }

    @Transactional(readOnly = true)
    public List<Editorial> getEditorials() {
        return editorialRepository.findAll();
    }

    private void validation(String name) throws ErrorService {
        if(name == null || name.isEmpty()) {
            throw new ErrorService("You have to provide a name");
        }
    }
}
