package com.paypal.bfs.test.bookingserv.storage;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "bookings")
@Getter
@Setter
public class BookingDAO {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String firstName;

    private String lastName;

    private String dateOfBirth;

    @Temporal(TemporalType.DATE)
    private Date checkIn;

    @Temporal(TemporalType.DATE)
    private Date checkOut;

    private int deposit;

    private int totalPrice;

    @OneToOne(cascade= CascadeType.PERSIST)
    private AddressDAO address;

    private String idempotencyKey;

}
