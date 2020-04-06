package com.excella.reactor.controllers;

import com.excella.reactor.common.exceptions.BookNotFoundException;
import com.excella.reactor.common.reactor.MonoUtils;
import com.excella.reactor.domain.Book;
import com.excella.reactor.mappers.BookMapper;
import com.excella.reactor.model.BookDto;
import com.excella.reactor.repositories.BookRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@RestController
@RequestMapping("/book")
@Slf4j
public class BookController {
    private final BookRepository bookRepository;

    @Autowired BookController(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @GetMapping(value = "/", name = "Get all books", produces = "application/json")
    Flux<Book> getAllBooks() {
        return MonoUtils.retrieveAsList(bookRepository::findAll)
                .doOnSubscribe(result -> log.info("Getting all books"));
    }

    @GetMapping(value = "/{bookId}", name = "Get book by id", produces = "application/json")
    Mono<Book> getBookById(@PathVariable Long bookId) {
        return MonoUtils.fromCallableOpt(() -> bookRepository.findById(bookId))
                .doOnSubscribe(result -> log.info("Getting book id {}", bookId))
                .switchIfEmpty(
                        Mono.error(BookNotFoundException.of("No book was found"))
                );
    }

    @PostMapping(value = "/", name = "Add a new book", produces = "application/json")
    Mono<Book> addNewBook(@RequestBody @Validated BookDto bookDto) {
        return Mono.just(bookDto).map(dto -> BookMapper.from(dto).toDomain())
                .flatMap(this::saveBook)
                .doOnSubscribe(result -> log.info("Adding new book {}", bookDto.getTitle()));
    }

    @DeleteMapping(value = "/{bookId}", name = "Get book by id", produces = "application/json")
    Mono<Book> removeBookById(@PathVariable Long bookId) {
        return getBookById(bookId)
                .flatMap(this::deleteBook)
                .doOnSubscribe(result -> log.info("Deleting book id {}", bookId));
    }

    @GetMapping(value = "/search", name = "Search for books", produces = "application/json")
    Flux<Book> searchForBooks(@RequestParam String q) {
        return searchByTitle(q).mergeWith(searchByAuthor(q))
                .doOnSubscribe(result -> log.info("Searching for books using query: {}", q));
    }

    private Mono<Book> saveBook(Book b) {
        return MonoUtils.fromCallable(() -> bookRepository.save(b));
    }

    private Mono<Book> deleteBook(Book b) {
        return MonoUtils.fromCallable(() -> {
            bookRepository.deleteById(b.getId());
            return b;
        });
    }

    private Flux<Book> searchByTitle(String title) {
        var bookIds = bookRepository.searchForIdsByTitle(title.toUpperCase());
        if (bookIds.isEmpty()) return Flux.empty();
        return MonoUtils.retrieveAsList(() -> bookRepository.findAllById(bookIds));
    }

    private Flux<Book> searchByAuthor(String author) {
        var bookIds = bookRepository.searchForIdsByAuthor(author.toUpperCase());
        if (bookIds.isEmpty()) return Flux.empty();
        return MonoUtils.retrieveAsList(() -> bookRepository.findAllById(bookIds));
    }

    // @GetMapping(value = "/search", name = "For testing", produces = "application/json")
    // Mono<Boolean> testEndpoint(@RequestParam String q) {
    //     var booltest = boolTest().subscribe().equals(true);
    //     Mono.jus
        
    //     return boolTest
    //             .doOnSubscribe(result -> log.info("Searching for books using query: {}", q));
    // }

    private Mono<Boolean> boolTest() {
        return Mono.just(false);
    }

}
