package com.comma_store.shopping.ui.SubCategoryItems;

import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;
import androidx.paging.PageKeyedDataSource;

import com.comma_store.shopping.pojo.ItemModel;

public class ItemDataSourceFactorySubCategoryItems extends DataSource.Factory{
    private MutableLiveData<PageKeyedDataSource<Integer, ItemModel>> itemLiveDataSource = new MutableLiveData<>();
    private String orderBy;
    private String lang;
    private int SubCategory_id;
    SubCategoryItemsFragment SubCategoryItemsFragment;
    itemDataSourceSubCategoryItems itemDataSourceSubCategoryItems;

    public ItemDataSourceFactorySubCategoryItems(String orderBy, String lang, int subCategory_id, SubCategoryItemsFragment subCategoryItemsFragment) {
        this.orderBy = orderBy;
        this.lang = lang;
        SubCategory_id = subCategory_id;
        SubCategoryItemsFragment = subCategoryItemsFragment;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    @Override
    public DataSource<Integer, ItemModel> create() {
        itemDataSourceSubCategoryItems = new itemDataSourceSubCategoryItems(orderBy,lang,SubCategory_id,SubCategoryItemsFragment);

        //posting the datasource to get the values
        itemLiveDataSource.postValue(itemDataSourceSubCategoryItems);

        //returning the datasource
        return itemDataSourceSubCategoryItems;
    }
    public MutableLiveData<PageKeyedDataSource<Integer, ItemModel>> getItemLiveDataSource() {
        return itemLiveDataSource;
    }
}
