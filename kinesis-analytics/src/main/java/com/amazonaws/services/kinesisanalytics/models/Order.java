package com.amazonaws.services.kinesisanalytics.models;

import lombok.Data;

@Data
public class Order {
    private Integer order_number;
    private Integer purchaser;
    private Integer quantity;
    private Integer product_id;
    private String order_date;
}
