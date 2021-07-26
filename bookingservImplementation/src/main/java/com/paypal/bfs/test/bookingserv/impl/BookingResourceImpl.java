package com.paypal.bfs.test.bookingserv.impl;

import com.paypal.bfs.test.bookingserv.api.BookingResource;
import com.paypal.bfs.test.bookingserv.api.model.Booking;
import com.paypal.bfs.test.bookingserv.error_handling.BookingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BookingResourceImpl implements BookingResource {

    @Autowired
    private BookingService bookingService;

    @Override
    public ResponseEntity<Booking> create(Booking booking, String idempotencyKey)
            throws BookingException {
        Booking bookingDTO = bookingService.create(booking, idempotencyKey);
        return new ResponseEntity<>(bookingDTO, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Iterable<Booking>> getAllBookings() {
        Iterable<Booking> resultDTO = bookingService.getAllBookings();
        return new ResponseEntity<>(resultDTO, HttpStatus.OK);
    }
}
