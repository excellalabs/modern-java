package com.excella.domain;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;
import lombok.experimental.NonFinal;

@Value
@Builder
public class Book {
    @NonFinal
    private Long id;

    @NonNull
    private String title;

    @NonNull
    private String author;

    private int yearPublished;
}
