package com.comma_store.shopping.ui.Deals;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;
import androidx.paging.PageKeyedDataSource;

import com.comma_store.shopping.pojo.ItemModel;

public class ItemDataSourceFactoryDeals extends DataSource.Factory{
     MutableLiveData<PageKeyedDataSource<Integer, ItemModel>> itemLiveDataSource = new MutableLiveData<>();
    private String orderBy;
    private String lang;
    DealsFragment dealsFragment;
    ItemDataSourceDeals itemDataSourceDeals;

    public ItemDataSourceFactoryDeals(String orderBy, String lang, DealsFragment dealsFragment) {
        this.orderBy = orderBy;
        this.lang = lang;
        this.dealsFragment = dealsFragment;
    }

    public void setOrderBy(String orderBy){
        this.orderBy = orderBy;
    }

    @Override
    public DataSource<Integer, ItemModel> create() {

        itemDataSourceDeals = new ItemDataSourceDeals(orderBy,lang,dealsFragment);
        //posting the datasource to get the values

        itemLiveDataSource.postValue(itemDataSourceDeals);
        //returning the datasource
        return itemDataSourceDeals;
    }
    public MutableLiveData<PageKeyedDataSource<Integer, ItemModel>> getItemLiveDataSource() {
        return itemLiveDataSource;
    }
}
