package com.amazonaws.services.kinesisanalytics.models;

import lombok.Data;

@Data
public class Address {
    private int id, customer_id, zip;
    private String street, city, state, type;
}
