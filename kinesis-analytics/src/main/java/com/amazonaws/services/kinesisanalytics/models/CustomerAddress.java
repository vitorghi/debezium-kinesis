package com.amazonaws.services.kinesisanalytics.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CustomerAddress {
    private Customer customer;
    private Address address;
}
