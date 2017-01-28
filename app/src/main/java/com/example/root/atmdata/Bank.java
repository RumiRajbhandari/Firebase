package com.example.root.atmdata;

import android.databinding.BaseObservable;

/**
 * Created by root on 1/23/17.
 */
public class Bank {

    public static final String FIREBASE_KEY = "bank";

    private String name;
    private String address;
    private String email;
    private int image;

    public Bank() {

    }

    public Bank(String name, String address, String email, int image) {
        this.name = name;
        this.address = address;
        this.email = email;
        this.image = image;
    }

    public int getImage() {
        return image;
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

    @Override
    public String toString() {
        return "Bank{" +
                "name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
