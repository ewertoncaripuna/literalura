package br.com.alura.literalura.repository;


import br.com.alura.literalura.model.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AuthorRepository extends JpaRepository<Author, Long> {
    @Query("SELECT a FROM Author a WHERE a.name LIKE %:name%")
    List<Author> findByNameContainingIgnoreCase(String name);

    @Query("SELECT a.name FROM Author a WHERE a.birthYear <= :year AND (a.deathYear IS NULL OR a.deathYear >= :year)")
    List<String> findLivingAuthorsInYear(int year);
}