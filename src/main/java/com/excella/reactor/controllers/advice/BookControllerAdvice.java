package com.excella.reactor.controllers.advice;


import com.excella.reactor.common.exceptions.BookNotFoundException;
import com.excella.reactor.common.exceptions.GenericError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;

@ControllerAdvice
@Slf4j
@RequestMapping
public class BookControllerAdvice {

    @ExceptionHandler(BookNotFoundException.class)
    ResponseEntity<GenericError> handleBookNotFoundException(final BookNotFoundException e) {
        log.warn(e.getMessage(), e.getCause());
        return buildGenericErrorResponseEntity(
                "Book not found",
                HttpStatus.NOT_FOUND);
    }

    private static ResponseEntity<GenericError> buildGenericErrorResponseEntity(
            String message, HttpStatus httpStatus) {
        return new ResponseEntity<GenericError>(GenericError.of(httpStatus.value(), message), httpStatus);
    }
}
