package com.comma_store.shopping.ui.FavoriteItemsScreen;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.comma_store.shopping.data.CartDataBase;
import com.comma_store.shopping.data.ItemClient;
import com.comma_store.shopping.pojo.CartItem;
import com.comma_store.shopping.pojo.FavoriteItem;
import com.comma_store.shopping.pojo.ItemModel;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;
import java.util.stream.Collectors;

import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class FavoriteItemsViewModel extends AndroidViewModel {
    private final CompositeDisposable disposables = new CompositeDisposable();
    MutableLiveData<List<FavoriteItem>> FavoriteItemsMutableLiveData=new MutableLiveData<>();
    MutableLiveData<List<ItemModel>>listItemsMutableLiveData= new MutableLiveData<>();
    MutableLiveData<Integer>ScreenState=new MutableLiveData<>(1);
    private Application context;

    public FavoriteItemsViewModel(@NonNull Application application) {
        super(application);
        context = application;
    }

    // 0==Empty
    // 1 ==loading
    //2== Recycle
    //3== error
    public void getItems(List<Integer>Ids){
      disposables.add( ItemClient.getINSTANCE().getItemInterface().getItemsCart(Locale.getDefault().getLanguage(), Ids)
                .subscribeOn(Schedulers.io()).subscribe(
                        x-> {
                            listItemsMutableLiveData.postValue(x.getData());
                           ScreenState.postValue(2);
                        }, e-> {
                          ScreenState.postValue(3);
                      }
              ));
    }

    public void getFavoriteItems(){
        disposables.add(CartDataBase.getInstance(context).favoriteItemsDAO().FavoriteItems().subscribeOn(Schedulers.io()).subscribe(x->{
         FavoriteItemsMutableLiveData.postValue(x);
        }));
    }
    @Override
    protected void onCleared() {
        super.onCleared();
        disposables.clear();
        disposables.dispose();
    }
}