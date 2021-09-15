package com.comma_store.shopping.pojo;

import java.util.List;

public class GetItemsCartRespons {
    List<ItemModel> data;
    public List<ItemModel> getData() {
        return data;
    }

    public void setData(List<ItemModel> data) {
        this.data = data;
    }
}
