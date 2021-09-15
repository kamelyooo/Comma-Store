package com.comma_store.shopping.ui.Search;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;
import androidx.paging.PageKeyedDataSource;

import com.comma_store.shopping.pojo.ItemModel;

public class ItemDataSourceFactorySearch extends DataSource.Factory{
    private MutableLiveData<PageKeyedDataSource<Integer, ItemModel>> itemLiveDataSource = new MutableLiveData<>();
    private String orderBy;
    private String q;
    SearchFragment searchFragment;
    ItemDataSourceSearch itemDataSource;

    public ItemDataSourceFactorySearch(SearchFragment searchFragment) {
        this.searchFragment = searchFragment;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public void setQuery(String q) {
        this.q = q;
    }

    @Override
    public DataSource create() {

        itemDataSource = new ItemDataSourceSearch(orderBy,q,searchFragment);

        //posting the datasource to get the values
//        itemLiveDataSource.postValue(itemDataSource);

        //returning the datasource
        return itemDataSource;
    }
    public MutableLiveData<PageKeyedDataSource<Integer, ItemModel>> getItemLiveDataSource() {
        return itemLiveDataSource;
    }
}
