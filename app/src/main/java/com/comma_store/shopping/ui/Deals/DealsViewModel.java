package com.comma_store.shopping.ui.Deals;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PageKeyedDataSource;
import androidx.paging.PagedList;
import androidx.paging.RxPagedListBuilder;

import com.comma_store.shopping.pojo.ItemModel;

import java.util.Locale;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

public class DealsViewModel extends ViewModel {

    //creating livedata for PagedList  and PagedKeyedDataSource
    LiveData itemPagedList;
    LiveData<PageKeyedDataSource<Integer, ItemModel>> liveDataSource;

    MutableLiveData<PagedList<ItemModel>> iitempagelist=new MutableLiveData<>();

    boolean showSpinKit=false;
    public MutableLiveData<Boolean>isConnected=new MutableLiveData<>();
    public MutableLiveData<Boolean> getIsConnected() {
        return isConnected;
    }
    public ItemDataSourceFactoryDeals itemDataSourceFactoryDeals;


    public void setShowSpinKit(boolean show){
        if (show)
            showSpinKit=true;
        else showSpinKit=false;
    }


    public void getItemsPagedDeals(DealsFragment dealsFragment){

        if(itemDataSourceFactoryDeals ==null){
            itemDataSourceFactoryDeals = new ItemDataSourceFactoryDeals("idDesc", Locale.getDefault().getLanguage(),dealsFragment);
            liveDataSource = itemDataSourceFactoryDeals.getItemLiveDataSource();
        }
        //ItemDataSourceFactory itemDataSourceFactory = new ItemDataSourceFactory("idDesc", Locale.getDefault().getLanguage(),dealsFragment);




        //Getting PagedList config
        if (itemPagedList==null) {
            PagedList.Config pagedListConfig =
                    (new PagedList.Config.Builder())
                            .setEnablePlaceholders(true)
                            .setPageSize(25)
                            .build();
            new RxPagedListBuilder<>(itemDataSourceFactoryDeals,pagedListConfig).
                    buildObservable().subscribeOn(Schedulers.io()).subscribe(s->iitempagelist.postValue((PagedList<ItemModel>) s));
            //Building the paged list
//            itemPagedList = (new LivePagedListBuilder(itemDataSourceFactoryDeals, pagedListConfig)).build();
        }
    }

    public void sort(String sortBy){
        itemDataSourceFactoryDeals.setOrderBy(sortBy);
        itemDataSourceFactoryDeals.itemDataSourceDeals.invalidate();
    }
}