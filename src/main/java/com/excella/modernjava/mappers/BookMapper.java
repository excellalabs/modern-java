package com.excella.modernjava.mappers;


import com.excella.modernjava.domain.Book;
import com.excella.modernjava.model.BookDto;
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
