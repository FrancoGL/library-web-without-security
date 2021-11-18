package com.beth.libraryweb.repositories;

import com.beth.libraryweb.entities.Editorial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EditorialRepository extends JpaRepository<Editorial, String> {

    @Query("SELECT e FROM Editorial e WHERE e.name LIKE :name")
    public Editorial findEditorialByName(@Param("name") String name);
}
