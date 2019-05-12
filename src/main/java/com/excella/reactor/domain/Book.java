package com.excella.reactor.domain;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import lombok.experimental.NonFinal;
import org.springframework.data.annotation.Id;

@Value
@Builder
public class Book {
    @NonFinal
    @Id
    private Long id;

    @NonNull
    private String title;

    @NonNull
    private String author;

    private int yearPublished;
}
