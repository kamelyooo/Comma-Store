package com.comma_store.shopping.ui.SubCategoryItems;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PageKeyedDataSource;
import androidx.paging.PagedList;


import com.comma_store.shopping.data.CartDataBase;
import com.comma_store.shopping.pojo.FavoriteItem;
import com.comma_store.shopping.pojo.ItemModel;

import java.util.Locale;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class SubCategoryItemsViewModel extends AndroidViewModel {
     MutableLiveData<Boolean> isConnected=new MutableLiveData<>();
    boolean showSpinKit=true;
    ItemDataSourceFactorySubCategoryItems itemDataSourceFactorySubCategoryItems;
    CompositeDisposable disposable=new CompositeDisposable();
    private Application context;

    public SubCategoryItemsViewModel(@NonNull Application application) {
        super(application);
        context = application;
    }

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
    public void insertFavoriteItem(int id){
        disposable.add(CartDataBase.getInstance(context).favoriteItemsDAO().insetFavoriteItem(new FavoriteItem(id))
                .subscribeOn(Schedulers.io()).subscribe());
    }
    public void deleteFavoriteItem(int id){
        disposable.add( CartDataBase.getInstance(context).favoriteItemsDAO().DeleteFavoriteItem(new FavoriteItem(id))
                .subscribeOn(Schedulers.io()).subscribe());
    }
    public void sort(String sortBy){
        itemDataSourceFactorySubCategoryItems.setOrderBy(sortBy);
        itemDataSourceFactorySubCategoryItems.itemDataSourceSubCategoryItems.invalidate();
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.dispose();
        disposable.clear();
    }
}