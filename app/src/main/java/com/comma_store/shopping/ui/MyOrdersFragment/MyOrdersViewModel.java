package com.comma_store.shopping.ui.MyOrdersFragment;

import android.util.Log;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.comma_store.shopping.Utils.SharedPreferencesUtils;
import com.comma_store.shopping.data.ItemClient;
import com.comma_store.shopping.pojo.GetOrderModelResponse;
import com.comma_store.shopping.pojo.Resource;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MyOrdersViewModel extends ViewModel {
    // TODO: Implement the ViewModel
    List<GetOrderModelResponse> MyOrders= Arrays.asList();
    MutableLiveData<Integer> ScreenState = new MutableLiveData<>(0);
    CompositeDisposable disposable = new CompositeDisposable();
    //state 0 loading
    //state 1 Empty
    //state 2 list
    //state 3 error\


    public void getMyOrders(String api) {
        Single<Resource<List<GetOrderModelResponse>>> resourceSingle = ItemClient.getINSTANCE().getItemInterface().getOrders(Locale.getDefault().getLanguage(), api)
                .subscribeOn(Schedulers.io());
        disposable.add(resourceSingle.subscribe(x -> {
            if (x.getStatus() == 200) {
                if (x.getData().size()==0){
                    ScreenState.postValue(1);
                }else {
                    MyOrders = x.getData();
                    ScreenState.postValue(2);
                }

            } else {
                ScreenState.postValue(3);
            }
        } ,e-> {
            ScreenState.postValue(3);
        }));
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.clear();
        disposable.dispose();
    }
}