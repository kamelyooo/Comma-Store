package com.comma_store.shopping.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.Keep;

import java.io.Serializable;
import java.util.List;


public class CompleteOrderModel implements Parcelable {
    private List<ItemModel>itemModels;
    private List<CartItem>cartItemList;


    public CompleteOrderModel(List<ItemModel> itemModels, List<CartItem> cartItemList) {
        this.itemModels = itemModels;
        this.cartItemList = cartItemList;
    }

    protected CompleteOrderModel(Parcel in) {
        
    }

    public static final Creator<CompleteOrderModel> CREATOR = new Creator<CompleteOrderModel>() {
        @Override
        public CompleteOrderModel createFromParcel(Parcel in) {
            return new CompleteOrderModel(in);
        }

        @Override
        public CompleteOrderModel[] newArray(int size) {
            return new CompleteOrderModel[size];
        }
    };

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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }
}
