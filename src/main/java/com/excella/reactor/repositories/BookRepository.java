package com.excella.reactor.repositories;

import com.excella.reactor.domain.Book;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends CrudRepository<Book, Long> {
    @Query("SELECT book.id FROM book WHERE UPPER(book.title) LIKE CONCAT('%',:search,'%')")
    List<Long> searchForIdsByTitle(@Param("search") String search);

    @Query("SELECT book.id FROM book WHERE UPPER(book.author) LIKE CONCAT('%',:search,'%')")
    List<Long> searchForIdsByAuthor(@Param("search") String search);
}
