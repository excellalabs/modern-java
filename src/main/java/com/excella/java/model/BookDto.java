package com.excella.java.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Value
@Builder
@JsonDeserialize(builder = BookDto.BookDtoBuilder.class)
public class BookDto {
    @NonNull
    private String title;

    @NonNull
    private String author;

    private int yearPublished;

    @JsonPOJOBuilder(withPrefix = "")
    public static class BookDtoBuilder {}
}
