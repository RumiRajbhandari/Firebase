package com.example.root.atmdata.model;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

/**
 * Created by root on 3/7/17.
 */

public class MyItem implements ClusterItem {
    private final LatLng position;
    private String mTitle;
    private String mSnippet;

    public MyItem(double lat, double lon){
        this.position=new LatLng(lat,lon);
    }

    public MyItem(double lat, double lon, String mTitle, String mSnippet) {
        this.position = new LatLng(lat,lon);
        this.mTitle = mTitle;
        this.mSnippet = mSnippet;
    }

    public String getmTitle() {
        return mTitle;
    }

    public String getmSnippet() {
        return mSnippet;
    }

    @Override
    public LatLng getPosition() {
        return null;
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
