package com.comma_store.shopping.pojo;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName ="cart")
public class CartItem {
    @PrimaryKey
    int id;
    int quantity;
    String color;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
