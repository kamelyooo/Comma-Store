package com.comma_store.shopping.pojo;

import java.util.List;

public class GetOrderModelResponse {
    int id;
    String code;
    String user_id;
    String status;
    String created_at;
    String updated_at;
    String notes;
    String discount;
    String promocode_id;
    String total;
    String cost;
    String driver_cost;
    String duration;
    String longitude;
    String latitude;
    String address;
    String notes_user;
    String status_name;
    List<OrderDetailsModel> orderDetails;
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getPromocode_id() {
        return promocode_id;
    }

    public void setPromocode_id(String promocode_id) {
        this.promocode_id = promocode_id;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getDriver_cost() {
        return driver_cost;
    }

    public void setDriver_cost(String driver_cost) {
        this.driver_cost = driver_cost;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
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

    public String getStatus_name() {
        return status_name;
    }

    public void setStatus_name(String status_name) {
        this.status_name = status_name;
    }

    public List<OrderDetailsModel> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(List<OrderDetailsModel> orderDetails) {
        this.orderDetails = orderDetails;
    }



}
