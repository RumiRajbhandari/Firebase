package com.example.root.atmdata.model;

import java.io.Serializable;

/**
 * Created by root on 2/5/17.
 */

public class Atm implements Serializable{
    private Double lat = Double.MIN_NORMAL;
    private Double lon = Double.MIN_NORMAL;
    private String status;
    private String reference;
     public Atm(){

     }

    public Atm(Double lat, Double lon, String status) {
        this.lat = lat;
        this.lon = lon;
        this.status = status;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public Double getLat() {
        return lat;
    }

    public Double getLon() {
        return lon;
    }

    public String getStatus() {
        return status;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Atm{" +
                "lat=" + lat +
                ", lon=" + lon +
                ", status=" + status +
                ", reference='" + reference + '\'' +
                '}';
    }
}
