package com.excella.common.exceptions;

public class BookNotFoundException extends RuntimeException {
    public BookNotFoundException(String message) {
        super(message);
    }
    public static BookNotFoundException of(String message) {
        return new BookNotFoundException(message);
    }
}
