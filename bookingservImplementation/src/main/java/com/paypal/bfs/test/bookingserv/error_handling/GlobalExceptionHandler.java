package com.paypal.bfs.test.bookingserv.error_handling;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(BookingException.class)
    protected ResponseEntity<Object> handleBookingException(BookingException bookingException) {
        return new ResponseEntity<>(bookingException.getMessage(), bookingException.getHttpStatus());
    }
}
