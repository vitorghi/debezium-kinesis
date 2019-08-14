package com.amazonaws.services.kinesisanalytics.models;

public class CustomerAddress {
    private Customer customer;
    private Address address;

    public CustomerAddress(Customer customer, Address address) {
        this.customer = customer;
        this.address = address;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Address getAddress() {
        return address;
    }

    @Override
    public String toString() {
        return "CustomerAddress{" +
                "customer=" + customer +
                ", address=" + address +
                '}';
    }
}
