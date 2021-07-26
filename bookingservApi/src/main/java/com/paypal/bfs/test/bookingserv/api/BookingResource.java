package com.paypal.bfs.test.bookingserv.api;

import com.paypal.bfs.test.bookingserv.api.model.Booking;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/v1/bfs/booking")
public interface BookingResource {
    /**
     * Create {@link Booking} resource
     *
     * @param booking the booking object
     * @return the created booking
     */
    @PostMapping
    ResponseEntity<Booking> create(@RequestBody Booking booking, @RequestHeader("idempotency-key") String idempotencyKey)
            throws Exception;

    @GetMapping
    @ResponseBody
    ResponseEntity<Iterable<Booking>> getAllBookings();
}
