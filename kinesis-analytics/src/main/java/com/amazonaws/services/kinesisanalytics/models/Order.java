package com.amazonaws.services.kinesisanalytics.models;

public class Order {
    private Integer order_number;
    private Integer purchaser;
    private Integer quantity;
    private Integer product_id;
    private String order_date;

    public Order() {
    }

    public Order(int order_number, int purchaser, int quantity, int product_id, String order_date) {
        this.order_number = order_number;
        this.purchaser = purchaser;
        this.quantity = quantity;
        this.product_id = product_id;
        this.order_date = order_date;
    }

    public Integer getOrder_number() {
        return order_number;
    }

    public void setOrder_number(Integer order_number) {
        this.order_number = order_number;
    }

    public Integer getPurchaser() {
        return purchaser;
    }

    public void setPurchaser(Integer purchaser) {
        this.purchaser = purchaser;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getProduct_id() {
        return product_id;
    }

    public void setProduct_id(Integer product_id) {
        this.product_id = product_id;
    }

    public String getOrder_date() {
        return order_date;
    }

    public void setOrder_date(String order_date) {
        this.order_date = order_date;
    }
}
