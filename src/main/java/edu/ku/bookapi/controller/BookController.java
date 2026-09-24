package edu.ku.bookapi.controller;

import edu.ku.bookapi.model.Books;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/books")
public class BookController {

    // Mutable list (ArrayList) so PUT and DELETE can modify it
    private final List<Books> books = new ArrayList<>(List.of(
            new Books(1L, "Java Programming", "John Smith", 5),
            new Books(2L, "Web Development", "Sara Ahmad", 3),
            new Books(3L, "Database Systems", "Emily Clark", 4)
    ));

    // ---------- GET: all books ----------
    @GetMapping
    public List<Books> getAllBooks() {
        return books;
    }

    // ---------- GET: single book by id ----------
    @GetMapping("/{id}")
    public ResponseEntity<Books> getBookById(@PathVariable Long id) {
        return books.stream()
                .filter(book -> book.getId().equals(id))
                .findFirst()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

  // ---------- POST: create a new book ----------
@PostMapping
public ResponseEntity<Books> createBook(@RequestBody Books book) {
    // Compute the next ID safely
    long newId = 1L;
    for (Books b : books) {
        if (b.getId() != null && b.getId() >= newId) {
            newId = b.getId() + 1;
        }
    }
    book.setId(newId);
    books.add(book);
    return ResponseEntity.status(HttpStatus.CREATED).body(book);
}

    // ---------- PUT: update an existing book ----------
    @PutMapping("/{bookId}")
    public ResponseEntity<Books> updateBook(
            @PathVariable Long bookId,
            @RequestBody Books input
    ) {
        for (Books book : books) {
            if (book.getId().equals(bookId)) {
                // Keep original ID; update the rest
                book.setTitle(input.getTitle());
                book.setAuthor(input.getAuthor());
                book.setAvailableCopies(input.getAvailableCopies());
                return ResponseEntity.ok(book); // 200 OK
            }
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // 404 Not Found
    }

    // ---------- DELETE: remove a book ----------
    @DeleteMapping("/{bookId}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long bookId) {
        boolean removed = books.removeIf(book -> book.getId().equals(bookId));

        if (removed) {
            return ResponseEntity.noContent().build(); // 204 No Content
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // 404 Not Found
    }
}