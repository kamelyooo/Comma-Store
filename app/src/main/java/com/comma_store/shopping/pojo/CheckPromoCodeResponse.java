package com.comma_store.shopping.pojo;

public class CheckPromoCodeResponse {

    int id;
    String code;
    String expiry_date;
    int min_cost;
    String created_at;
    String updated_at;
    int user_id;
    int count_used;
    int cost;
    int send_notif;

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

    public String getExpiry_date() {
        return expiry_date;
    }

    public void setExpiry_date(String expiry_date) {
        this.expiry_date = expiry_date;
    }

    public int getMin_cost() {
        return min_cost;
    }

    public void setMin_cost(int min_cost) {
        this.min_cost = min_cost;
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

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getCount_used() {
        return count_used;
    }

    public void setCount_used(int count_used) {
        this.count_used = count_used;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public int getSend_notif() {
        return send_notif;
    }

    public void setSend_notif(int send_notif) {
        this.send_notif = send_notif;
    }
}
