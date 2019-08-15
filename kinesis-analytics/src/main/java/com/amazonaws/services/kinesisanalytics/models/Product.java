package com.amazonaws.services.kinesisanalytics.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Product {
    private int id;
    private String name, description;
    private float weight;
}
