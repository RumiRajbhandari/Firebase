package com.wlit.fellowship.atmnepal.model;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

import java.io.Serializable;

/**
 * Created by root on 2/5/17.
 */

public class Atm implements Serializable, ClusterItem {

    // assign default values to latitude, longitude
    private Double latitude = Double.MIN_NORMAL;
    private Double longitude = Double.MIN_NORMAL;
    private String status;
    private String reference;
    private String updated;

    public Atm() {
    }

    public Atm(Double latitude, Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Atm(Double latitude, Double longitude, String status,String updated) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.status = status;
        this.updated=updated;
    }


    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public String getStatus() {
        return status;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUpdated() {
        return updated;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }

    @Override
    public String toString() {
        return "Atm{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                ", status=" + status +
                ", updated=" + updated +
                ", reference='" + reference + '\'' +
                '}';
    }

    @Override
    public LatLng getPosition() {
        return new LatLng(latitude, longitude);
    }

    @Override
    public String getTitle() {
        return null;
    }

    @Override
    public String getSnippet() {
        return null;
    }
}
