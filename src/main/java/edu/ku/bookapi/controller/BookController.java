package edu.ku.bookapi.controller;

import edu.ku.bookapi.model.Books;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/books")
public class BookController {

        private final List<Books> books = new ArrayList<>(List.of(
                        new Books(1L, "Clean Code", "Robert C. Martin",
                                        "9780132350884", 2008, "Software Engineering"),

                        new Books(2L, "Effective Java", "Joshua Bloch",
                                        "9780134685991", 2018, "Java"),

                        new Books(3L, "Designing Data-Intensive Applications",
                                        "Martin Kleppmann", "9781449373320",
                                        2017, "Distributed Systems"),

                        new Books(4L, "Spring in Action", "Craig Walls",
                                        "9781617297571", 2022, "Spring"),

                        new Books(5L, "Computer Networks", "Andrew S. Tanenbaum",
                                        "9780132126953", 2010, "Networking")));

        @GetMapping
        public List<Books> getAllBooks() {
                return books;
        }

        @GetMapping("/{id}")
        public Books getBookById(@PathVariable Long id) {
                return books.stream()
                                .filter(book -> book.getId().equals(id))
                                .findFirst()
                                .orElse(null);
        }

        @PostMapping
        public Books createBook(@RequestBody Books book) {
                book.setId((long) (books.size() + 1));
                books.add(book);
                return book;
        }

        @DeleteMapping("/{id}")
        public String deleteBook(@PathVariable Long id) {
                boolean removed = books.removeIf(book -> book.getId().equals(id));

                if (removed) {
                        return "Book deleted successfully!";
                }

                return "Book not found!";
        }
}