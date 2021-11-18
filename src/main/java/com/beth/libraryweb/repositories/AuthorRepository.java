package com.beth.libraryweb.repositories;

import com.beth.libraryweb.entities.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorRepository extends JpaRepository<Author, String> {

    @Query("SELECT a FROM Author a WHERE a.name LIKE :name")
    public Author findAuthorByName(@Param("name") String name);
}
