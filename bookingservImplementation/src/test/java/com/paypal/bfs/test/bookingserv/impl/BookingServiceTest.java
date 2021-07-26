package com.paypal.bfs.test.bookingserv.impl;

import com.paypal.bfs.test.bookingserv.api.model.Address;
import com.paypal.bfs.test.bookingserv.api.model.Booking;
import com.paypal.bfs.test.bookingserv.error_handling.BookingException;
import com.paypal.bfs.test.bookingserv.storage.BookingDAO;
import com.paypal.bfs.test.bookingserv.storage.BookingRepository;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.text.SimpleDateFormat;
import java.util.Date;

@RunWith(MockitoJUnitRunner.class)
public class BookingServiceTest extends TestCase {

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Mock
    private BookingRepository bookingRepository;

    @InjectMocks
    private BookingService bookingService = new BookingService();

    @Test
    public void testCreateBooking() throws Exception {

        Booking bookingDTO = new Booking();

        bookingDTO.setFirstName("Ramesh");
        bookingDTO.setLastName("Kumar");

        bookingDTO.setDateOfBirth("1990-08-08");

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd KK:mm:ss");
        Date checkin = simpleDateFormat.parse("2021-08-08 13:49:51");
        Date checkout = simpleDateFormat.parse("2021-08-09 12:00:00");

        bookingDTO.setCheckIn(checkin);
        bookingDTO.setCheckOut(checkout);

        bookingDTO.setDeposit(1000);
        bookingDTO.setTotalPrice(5000);

        Address address = new Address();
        address.setLine1("radha nagar");
        address.setCity("Bangalore");
        bookingDTO.setAddress(address);

        BookingDAO bookingDAO = new BookingDAO();
        bookingDAO.setFirstName(bookingDTO.getFirstName());
        bookingDAO.setLastName(bookingDTO.getLastName());
        bookingDAO.setDateOfBirth(bookingDTO.getDateOfBirth());
        bookingDAO.setCheckIn(bookingDTO.getCheckIn());

        Mockito.when(bookingRepository.save(Mockito.any())).thenReturn(bookingDAO);

        bookingService.create(bookingDTO, "a1b1c1");

        Mockito.verify(bookingRepository).save(Mockito.any(BookingDAO.class));
    }

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Test
    public void testCheckInGreaterThanCheckout() throws Exception {

        Booking bookingDTO = new Booking();

        bookingDTO.setFirstName("Ramesh");
        bookingDTO.setLastName("Kumar");

        bookingDTO.setDateOfBirth("1990-08-08");

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd KK:mm:ss");
        Date checkin = simpleDateFormat.parse("2021-08-10 13:49:51");
        Date checkout = simpleDateFormat.parse("2021-08-09 12:00:00");

        bookingDTO.setCheckIn(checkin);
        bookingDTO.setCheckOut(checkout);

        exceptionRule.expect(BookingException.class);
        exceptionRule.expectMessage("Check in date occur before checkout date");

        bookingService.create(bookingDTO, "a1b1c2");
    }

    @Test
    public void testGetAllBookings() {
        bookingService.getAllBookings();
        Mockito.verify(bookingRepository).findAll();
    }
}