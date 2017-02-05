package com.example.root.atmdata;

import java.util.ArrayList;

/**
 * Created by root on 1/23/17.
 */
public class Bank {
    private String name;
    private String address;
    private String email;
    private ArrayList<Atm> atmlist;


    public Bank(){

    }

    public Bank(String name, String address, String email, ArrayList<Atm> atms) {
        this.name = name;
        this.address = address;
        this.email = email;
        this.atmlist = atms;
    }

    public ArrayList<Atm> getAtmlist() {
        return atmlist;
    }

    public void setAtmlist(ArrayList<Atm> atmlist) {
        this.atmlist = atmlist;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


}
