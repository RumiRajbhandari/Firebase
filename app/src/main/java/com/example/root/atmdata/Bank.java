package com.example.root.atmdata;

/**
 * Created by root on 1/23/17.
 */
public class Bank {
    private String name;
    private String address;
    private String email;
    public Bank(){

    }

    public Bank(String name, String address, String email) {
        this.name = name;
        this.address = address;
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
