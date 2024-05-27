package br.com.alura.literalura.repository;

import br.com.alura.literalura.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {

    Optional<Book> findByTitleContainsIgnoreCase(String title);




    @Query("SELECT b FROM Book b JOIN b.languages l WHERE l = :language")
    List<Book> findBooksByLanguage(String language);

    @Query("SELECT b FROM Book b ORDER BY b.numberDownloads DESC")
    List<Book> findTop10ByOrderByNumberDownloadsDesc();

    @Query("SELECT l FROM Book l JOIN l.authors a WHERE LOWER(a.name) LIKE LOWER(CONCAT('%', :authorName, '%'))")
    List<Book> findBooksByAuthorName(String authorName);

}