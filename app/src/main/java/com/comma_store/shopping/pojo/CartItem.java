package com.comma_store.shopping.pojo;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName ="cart")
public class CartItem {
    @PrimaryKey
    int item_id;
    int quantity;
    String color;

    public CartItem() {
    }

    public CartItem(int item_id, int quantity, String color) {
        this.item_id = item_id;
        this.quantity = quantity;
        this.color = color;
    }

    public int getItem_id() {
        return item_id;
    }

    public void setItem_id(int item_id) {
        this.item_id = item_id;
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
