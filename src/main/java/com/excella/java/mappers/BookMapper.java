package com.excella.java.mappers;


import com.excella.java.domain.Book;
import com.excella.java.model.BookDto;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(staticName = "from")
public class BookMapper implements DomainMapper<Book> {
    private final BookDto bookDto;

    @Override
    public Book toDomain() {
        return Book.builder()
                .title(bookDto.getTitle())
                .author(bookDto.getAuthor())
                .yearPublished(bookDto.getYearPublished())
                .build();
    }
}
