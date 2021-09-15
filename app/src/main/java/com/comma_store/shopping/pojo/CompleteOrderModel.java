package com.comma_store.shopping.pojo;

import java.io.Serializable;
import java.util.List;

public class CompleteOrderModel implements Serializable {
    private List<ItemModel>itemModels;
    private List<CartItem>cartItemList;

    public List<ItemModel> getItemModels() {
        return itemModels;
    }

    public void setItemModels(List<ItemModel> itemModels) {
        this.itemModels = itemModels;
    }

    public List<CartItem> getCartItemList() {
        return cartItemList;
    }

    public void setCartItemList(List<CartItem> cartItemList) {
        this.cartItemList = cartItemList;
    }
}
