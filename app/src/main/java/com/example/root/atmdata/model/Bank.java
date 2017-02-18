package com.example.root.atmdata.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 1/23/17.
 */
public class Bank implements Serializable{
    private String name;
    private String address;
    private String email;
    private String head_office;
    private String opeing_hours;
    private String phone;
    private String url;

    public Bank(String name, String address, String email, String head_office, String opeing_hours, String phone, String url, List<Atm> atmlist) {
        this.name = name;
        this.address = address;
        this.email = email;
        this.head_office = head_office;
        this.opeing_hours = opeing_hours;
        this.phone = phone;
        this.url = url;
        this.atmlist = atmlist;
    }

    @Override
    public String toString() {
        return "Bank{" +
                "name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", email='" + email + '\'' +
                ", head_office='" + head_office + '\'' +
                ", opeing_hours='" + opeing_hours + '\'' +
                ", phone='" + phone + '\'' +
                ", url='" + url + '\'' +
                ", atmlist=" + atmlist +
                '}';
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setHead_office(String head_office) {
        this.head_office = head_office;
    }

    public void setOpeing_hours(String opeing_hours) {
        this.opeing_hours = opeing_hours;
    }

    public List<Atm> getAtmlist() {
        return atmlist;
    }

    public void setAtmlist(List<Atm> atmlist) {
        this.atmlist = atmlist;
    }

    private List<Atm> atmlist;
//

    public Bank(){

    }

    public String getHead_office() {
        return head_office;
    }

    public void setHeadOffice(String head_office) {
        this.head_office = head_office;
    }

    public String getOpeing_hours() {
        return opeing_hours;
    }

    public void setOpeningHours(String opeing_hours) {
        this.opeing_hours = opeing_hours;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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
