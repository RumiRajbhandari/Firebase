package com.example.root.atmdata.model;

/**
 * Created by root on 2/5/17.
 */

public class Atm {
    private Double lat;
    private Double lon;
    private Boolean status;
     public Atm(){

     }

    public Atm(Double lat, Double lon, Boolean status) {
        this.lat = lat;
        this.lon = lon;
        this.status = status;
    }

    public Double getLat() {
        return lat;
    }

    public Double getLon() {
        return lon;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}
