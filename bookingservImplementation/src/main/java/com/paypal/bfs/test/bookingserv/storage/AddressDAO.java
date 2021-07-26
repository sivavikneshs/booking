package com.paypal.bfs.test.bookingserv.storage;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Getter
@Setter
@Entity
public class AddressDAO {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private String line1;
    private String city;
}
