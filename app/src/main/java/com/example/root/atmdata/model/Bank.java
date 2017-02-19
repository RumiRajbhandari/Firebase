package com.example.root.atmdata.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by root on 1/23/17.
 */
public class Bank implements Serializable {

    public static final String EXTRA_KEY = "bank";

    private String name;
    private String address;
    private String email;
    private String headOffice;
    private String openingHours;
    private String phone;
    private String url;
    private List<Atm> atmList;

    public Bank() {
    }

    public Bank(String name, String address, String email, String headOffice, String openingHours,
                String phone, String url, List<Atm> atmList) {
        this.name = name;
        this.address = address;
        this.email = email;
        this.headOffice = headOffice;
        this.openingHours = openingHours;
        this.phone = phone;
        this.url = url;
        this.atmList = atmList;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setHeadOffice(String headOffice) {
        this.headOffice = headOffice;
    }

    public void setOpeningHours(String openingHours) {
        this.openingHours = openingHours;
    }

    public List<Atm> getAtmList() {
        return atmList;
    }

    public void setAtmList(List<Atm> atmList) {
        this.atmList = atmList;
    }

    public String getHeadOffice() {
        return headOffice;
    }

    public String getOpeningHours() {
        return openingHours;
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

    @Override
    public String toString() {
        return "Bank{" +
                "name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", email='" + email + '\'' +
                ", headOffice='" + headOffice + '\'' +
                ", openingHours='" + openingHours + '\'' +
                ", phone='" + phone + '\'' +
                ", url='" + url + '\'' +
                ", atmList=" + atmList +
                '}';
    }

}
