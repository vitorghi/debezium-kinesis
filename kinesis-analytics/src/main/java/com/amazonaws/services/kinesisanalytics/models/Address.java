package com.amazonaws.services.kinesisanalytics.models;

public class Address {
    private int id, customer_id, zip;
    private String street, city, state, type;

    Address() {}

    public Address(int id, int customer_id, int zip, String street, String city, String state, String type) {
        this.id = id;
        this.customer_id = customer_id;
        this.zip = zip;
        this.street = street;
        this.city = city;
        this.state = state;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(int customer_id) {
        this.customer_id = customer_id;
    }

    public int getZip() {
        return zip;
    }

    public void setZip(int zip) {
        this.zip = zip;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Address{" +
                "id=" + id +
                ", customer_id=" + customer_id +
                ", zip=" + zip +
                ", street='" + street + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
