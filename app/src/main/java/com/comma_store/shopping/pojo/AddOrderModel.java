package com.comma_store.shopping.pojo;

import java.util.List;

public class AddOrderModel {
    String api_key;
    String lang;
    int deliveryCost;
    String promocode;
    double longitude;
    double latitude;
    String address;
    String notes_user;
    List<CartItem> order_details;

    public AddOrderModel(String api_key, String lang, int deliveryCost, String promocode, double longitude, double latitude, String address, String notes_user, List<CartItem> order_details) {
        this.api_key = api_key;
        this.lang = lang;
        this.deliveryCost = deliveryCost;
        this.promocode = promocode;
        this.longitude = longitude;
        this.latitude = latitude;
        this.address = address;
        this.notes_user = notes_user;
        this.order_details = order_details;
    }

    public String getApi_key() {
        return api_key;
    }

    public void setApi_key(String api_key) {
        this.api_key = api_key;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public int getDeliveryCost() {
        return deliveryCost;
    }

    public void setDeliveryCost(int deliveryCost) {
        this.deliveryCost = deliveryCost;
    }

    public String getPromocode() {
        return promocode;
    }

    public void setPromocode(String promocode) {
        this.promocode = promocode;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNotes_user() {
        return notes_user;
    }

    public void setNotes_user(String notes_user) {
        this.notes_user = notes_user;
    }

    public List<CartItem> getOrder_details() {
        return order_details;
    }

    public void setOrder_details(List<CartItem> order_details) {
        this.order_details = order_details;
    }

}