package com.paypal.bfs.test.bookingserv.error_handling;

import org.springframework.http.HttpStatus;

public class BookingException extends Exception {

    private final HttpStatus httpStatus;

    public BookingException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public BookingException(String message, HttpStatus httpStatus, Exception e) {
        super(message, e);
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
