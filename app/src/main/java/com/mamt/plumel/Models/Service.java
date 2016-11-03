package com.mamt.plumel.Models;

import java.util.Date;

/**
 * Created by marcosmt on 02/11/2016.
 */

public class Service {
    private String picture;
    private String username;
    private String address;
    private String date;


    public Service(String picture, String username, String address, String date) {
        this.picture = picture;
        this.username = username;
        this.address = address;
        this.date = date;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
