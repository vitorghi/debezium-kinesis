package com.amazonaws.services.kinesisanalytics.models;

import lombok.Data;

@Data
public class Customer {
    private int id;
    private String first_name, last_name, email;
}
