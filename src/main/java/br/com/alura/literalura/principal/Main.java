package br.com.alura.literalura.principal;
import br.com.alura.literalura.model.Author;
import br.com.alura.literalura.model.Data;
import br.com.alura.literalura.model.Book;
import br.com.alura.literalura.model.BookData;
import br.com.alura.literalura.repository.BookRepository;
import br.com.alura.literalura.repository.AuthorRepository;
import br.com.alura.literalura.service.APIConsumer;
import br.com.alura.literalura.service.DataConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class Main implements CommandLineRunner {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorRepository authorRepository;

    private Scanner scanner = new Scanner(System.in);
    private APIConsumer apiConsumer = new APIConsumer();
    private final String BASE_URL = "https://gutendex.com/books/";
    private DataConverter dataConverter = new DataConverter();

    @Override
    public void run(String... args) throws Exception {
        showMenu();
    }

    public void showMenu() {
        int option;
        boolean shouldExit = false;

        while (!shouldExit) {
            try {
                var menu = """
                    -----------------------------------
                    Escolha uma opção pelo seu número:
                    1- Buscar livro por título
                    2- Listar livros cadastrados
                    3- Listar autores cadastrados
                    4- Listar autores vivos em um ano específico
                    5- Listar livros por idioma
                    6- Listar os 10 livros mais baixados
                    7- Listar livros por Autor
                    0- Sair
                    -----------------------------------
                    """;

                System.out.println(menu);
                option = scanner.nextInt();
                scanner.nextLine();

                switch (option) {
                    case 1:
                        searchBookByTitle();
                        break;
                    case 2:
                        listRegisteredBooks();
                        break;
                    case 3:
                        listRegisteredAuthors();
                        break;
                    case 4:
                        listLivingAuthors();
                        break;
                    case 5:
                        listBooksByLanguage();
                        break;
                    case 6:
                        listTop10DownloadedBooks();
                        break;
                    case 7:
                        searchBooksByAuthor();
                        break;
                    case 0:
                        System.out.println("Saindo da aplicação...");
                        shouldExit = true;
                        break;
                    default:
                        System.out.println("Opção Inválida");
                }
            } catch (InputMismatchException e) {
                System.out.println("Entrada inválida. Por favor, insira um número.");
                scanner.nextLine();
            }
        }
    }


    private void searchBookByTitle() {
        boolean exit = false;
        while (!exit) {
            System.out.println("Digite o nome do livro que você deseja procurar (0 para voltar ao menu):");
            var bookTitle = scanner.nextLine();

            if ("0".equals(bookTitle)) {
                break;
            }

            Optional<Book> existingBooks = bookRepository.findByTitleContainsIgnoreCase(bookTitle);
            if (!existingBooks.isEmpty()) {
                System.out.println("O livro já está cadastrado no banco de dados.");
                continue;
            }

            var json = apiConsumer.getData(BASE_URL + "?search=" + bookTitle.replace(" ", "+"));
            Data searchData = dataConverter.getData(json, Data.class);
            if (searchData.results().isEmpty()) {
                System.out.println("Livro não encontrado.");
                continue;
            }

            System.out.println("-----------------------------------");
            System.out.println("Livros encontrados:");
            printSearchResults(searchData);

            int selection = getBookSelection(searchData);
            if (selection == 0) {
                continue;
            }

            saveSelectedBook(searchData, selection);
        }
    }

    private void printSearchResults(Data searchData) {
        for (int i = 0; i < Math.min(10, searchData.results().size()); i++) {
            System.out.println((i + 1) + ". " + searchData.results().get(i).title());
        }
    }

    private int getBookSelection(Data searchData) {
        System.out.println("Digite a opção do livro que você deseja salvar (0 para cancelar):");
        return scanner.nextInt();
    }

    private void saveSelectedBook(Data searchData, int selection) {
        if (selection > 0 && selection <= searchData.results().size()) {
            BookData selectedBook = searchData.results().get(selection - 1);
            Book book = new Book(selectedBook);

            if (book.getLanguages() == null || book.getLanguages().isEmpty()) {
                System.out.println("Nenhum idioma encontrado para o livro.");
            } else {
                printBookDetails(book);
                bookRepository.save(book);
                System.out.println("Livro salvo no banco de dados");
            }
        } else {
            System.out.println("Seleção inválida");
        }
    }

    private void printBookDetails(Book book) {
        System.out.println("------------LIVRO-----------------");
        System.out.println("Titulo: " + book.getTitle());
        book.getAuthors().forEach(author -> System.out.println("Autor: " + author.getName()));
        System.out.println("Idioma: " + String.join(", ", book.getLanguages()));
        System.out.println("Downloads: " + book.getNumberDownloads().intValue());
        System.out.println("----------------------------------");
    }

    private void listRegisteredBooks() {
        System.out.println("Livros Registrados:");
        bookRepository.findAll().forEach(book -> printBookDetails(book));
    }

    private void listRegisteredAuthors() {
        System.out.println("Autores registrados:");
        Set<String> uniqueAuthors = new HashSet<>();

        List<Author> authors = authorRepository.findAll();
        for (Author author : authors) {
            uniqueAuthors.add(author.getName());
        }

        for (String authorName : uniqueAuthors) {
            System.out.println("Autor: " + authorName);
        }
    }


    private void listBooksByLanguage() {
        System.out.println("Digite o idioma para listar os livros (ES, EN, FR, PT):");
        String language = scanner.nextLine().toLowerCase();

        var booksByLanguage = bookRepository.findBooksByLanguage(language);
        if (booksByLanguage.isEmpty()) {
            System.out.println("Nenhum livro encontrado no idioma: " + language);
        } else {
            booksByLanguage.forEach(book -> printBookDetails(book));
        }
    }

    private void listLivingAuthors() {
        System.out.println("Digite o ano para listar os autores vivos:");
        try {
            int year = scanner.nextInt();
            scanner.nextLine();

            var livingAuthors = authorRepository.findLivingAuthorsInYear(year);
            if (livingAuthors.isEmpty()) {
                System.out.println("Nenhum autor vivo encontrado no ano " + year);
            } else {
                livingAuthors.forEach(author -> System.out.println("Autor: " + author));
            }
        } catch (InputMismatchException e) {
            System.out.println("Entrada inválida. Por favor, insira um ano válido.");
            scanner.nextLine();
        }
    }

    private void listTop10DownloadedBooks() {
        List<Book> top10Books = bookRepository.findTop10ByOrderByNumberDownloadsDesc();
        if (top10Books.isEmpty()) {
            System.out.println("Nenhum livro encontrado.");
        } else {
            System.out.println("Os 10 livros mais baixados:");
            for (int i = 0; i < Math.min(10, top10Books.size()); i++) {
                Book book = top10Books.get(i);
                System.out.println((i + 1) + ". " + book.getTitle() + " - Downloads: " + book.getNumberDownloads().intValue());
            }
        }
    }

    private void searchBooksByAuthor() {
        System.out.println("Digite parte do nome do autor para buscar os livros (0 para voltar ao menu):");
        String authorName = scanner.nextLine().toLowerCase(); // Convertendo para minúsculas

        if ("0".equals(authorName)) {
            return;
        }

        List<Book> foundBooks = bookRepository.findBooksByAuthorName(authorName);
        if (foundBooks.isEmpty()) {
            System.out.println("Nenhum livro encontrado para o autor especificado.");
        } else {
            System.out.println("Livros encontrados para o autor '" + authorName + "':");
            for (Book book : foundBooks) {
                System.out.println("Livro: " + book.getTitle());
            }
        }
    }

}

