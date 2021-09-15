package com.comma_store.shopping.ui.SubCategoryItems;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PageKeyedDataSource;
import androidx.paging.PagedList;


import com.comma_store.shopping.pojo.ItemModel;

import java.util.Locale;

public class SubCategoryItemsViewModel extends ViewModel {
     MutableLiveData<Boolean> isConnected=new MutableLiveData<>();
    boolean showSpinKit=true;
    ItemDataSourceFactorySubCategoryItems itemDataSourceFactorySubCategoryItems;

    public MutableLiveData<Boolean> getIsConnected() {
        return isConnected;
    }
    public void setConnected(boolean connected){
        if (connected){
            isConnected.postValue(true);
        }else if (!connected){
            isConnected.postValue(false);
        }
    }
    LiveData itemPagedList;
    LiveData<PageKeyedDataSource<Integer, ItemModel>> liveDataSource;

    public void getSubCategoryItems(SubCategoryItemsFragment subCategoryItemsFragment, int subCategoryId){
        if (itemDataSourceFactorySubCategoryItems ==null){
            itemDataSourceFactorySubCategoryItems = new ItemDataSourceFactorySubCategoryItems("idDesc",Locale.getDefault().getLanguage(),subCategoryId,subCategoryItemsFragment);
        }

        //getting the live data source from data source factory
        liveDataSource = itemDataSourceFactorySubCategoryItems.getItemLiveDataSource();

        //Getting PagedList config
        PagedList.Config pagedListConfig =
                (new PagedList.Config.Builder())
                        .setEnablePlaceholders(true)
                        .setPageSize(25)
                        .build();

        //Building the paged list
        itemPagedList=(new LivePagedListBuilder(itemDataSourceFactorySubCategoryItems,pagedListConfig)).build();
    }
    public void sort(String sortBy){
        itemDataSourceFactorySubCategoryItems.setOrderBy(sortBy);
        itemDataSourceFactorySubCategoryItems.itemDataSourceSubCategoryItems.invalidate();
    }
}