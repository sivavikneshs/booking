package com.paypal.bfs.test.bookingserv.impl;

import com.paypal.bfs.test.bookingserv.api.model.Address;
import com.paypal.bfs.test.bookingserv.api.model.Booking;
import com.paypal.bfs.test.bookingserv.error_handling.BookingException;
import com.paypal.bfs.test.bookingserv.storage.AddressDAO;
import com.paypal.bfs.test.bookingserv.storage.BookingDAO;
import com.paypal.bfs.test.bookingserv.storage.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    public Booking create(Booking booking, String idempotencyKey) throws BookingException {

        BookingDAO bookingDAO = checkForIdempotency(idempotencyKey);
        if (bookingDAO != null)
            return buildBookingDTO(bookingDAO);

        if (booking.getCheckIn().after(booking.getCheckOut())) {
            throw new BookingException("Check in date occur before checkout date", HttpStatus.NOT_ACCEPTABLE);
        }

        BookingDAO newBookingDAO = buildBookingDAO(booking, idempotencyKey);
        try {
            BookingDAO saved = bookingRepository.save(newBookingDAO);
            return buildBookingDTO(saved);
        } catch (Exception e) {
            throw new BookingException("Unable to save booking object", HttpStatus.INTERNAL_SERVER_ERROR, e);
        }
    }


    /* One way is to pass an unique idempotency_key in header for each request and check if such key exists in db
     * Have implemented this technique. Drawback is user should be aware of passing an unique idempotency-key everytime
     * he makes a request.
     *
     * Other way is to use equals method and check equality of all fields. In this approach if there are huge number of
     * fields then lot of comparisons and request processing becomes slower
     *
     */
    private BookingDAO checkForIdempotency(String idempotencyKey) {
        Iterable<BookingDAO> bookingDAOS = bookingRepository.findAll();

        for (BookingDAO bookingDAO : bookingDAOS) {
            if (bookingDAO.getIdempotencyKey().equals(idempotencyKey)) {
                return bookingDAO;
            }
        }
        return null;
    }

    public Iterable<Booking> getAllBookings() {
        Iterable<BookingDAO> bookingDAOS = bookingRepository.findAll();
        List<Booking> resultDTO = new ArrayList<>();

        for (BookingDAO newBookingDAO : bookingDAOS) {
            Booking bookingDTO = buildBookingDTO(newBookingDAO);

            AddressDAO addressDAO = newBookingDAO.getAddress();
            Address addressDTO = new Address();
            addressDTO.setCity(addressDAO.getCity());
            addressDTO.setLine1(addressDAO.getLine1());
            bookingDTO.setAddress(addressDTO);

            resultDTO.add(bookingDTO);
        }
        return resultDTO;
    }


    private Booking buildBookingDTO(BookingDAO saved) {
        Booking bookingDTO = new Booking();
        bookingDTO.setFirstName(saved.getFirstName());
        bookingDTO.setLastName(saved.getLastName());
        bookingDTO.setDateOfBirth(saved.getDateOfBirth());
        bookingDTO.setCheckIn(saved.getCheckIn());
        return bookingDTO;
    }

    private BookingDAO buildBookingDAO(Booking booking, String idempotencyKey) {
        BookingDAO newBookingDAO = new BookingDAO();

        newBookingDAO.setFirstName(booking.getFirstName());
        newBookingDAO.setLastName(booking.getLastName());

        newBookingDAO.setDateOfBirth(booking.getDateOfBirth());

        newBookingDAO.setCheckIn(booking.getCheckIn());
        newBookingDAO.setCheckOut(booking.getCheckOut());

        newBookingDAO.setDeposit(booking.getDeposit());
        newBookingDAO.setTotalPrice(booking.getTotalPrice());

        newBookingDAO.setIdempotencyKey(idempotencyKey);

        AddressDAO addressDAO = buildAddressDAO(booking);
        newBookingDAO.setAddress(addressDAO);
        return newBookingDAO;
    }

    private AddressDAO buildAddressDAO(Booking booking) {
        Address address = booking.getAddress();
        AddressDAO addressDAO = new AddressDAO();
        addressDAO.setCity(address.getCity());
        addressDAO.setLine1(address.getLine1());
        return addressDAO;
    }
}
