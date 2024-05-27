package br.com.alura.literalura.model;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "books")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String title;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
            name = "book_authors",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "author_id")
    )
    private List<Author> authors;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "book_languages", joinColumns = @JoinColumn(name = "book_id"))
    @Column(name = "language")
    private List<String> languages;

    @Column(name = "number_downloads")
    private Double numberDownloads;

    public Book() {}

    public Book(BookData bookData) {
        this.title = bookData.title();
        this.authors = bookData.authors().stream().map(da -> new Author(da.name(), da.birthYear(), da.deathYear())).toList();
        this.languages = bookData.languages();
        this.numberDownloads = bookData.numberDownloads();
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Author> getAuthors() {
        return authors;
    }

    public void setAuthors(List<Author> authors) {
        this.authors = authors;
    }

    public List<String> getLanguages() {
        return languages;
    }

    public void setLanguages(List<String> languages) {
        this.languages = languages;
    }

    public Double getNumberDownloads() {
        return numberDownloads;
    }

    public void setNumberDownloads(Double numberDownloads) {
        this.numberDownloads = numberDownloads;
    }
}
