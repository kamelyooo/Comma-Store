package com.comma_store.shopping.ui.OrderDetailsFragment;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.comma_store.shopping.data.ItemClient;
import com.comma_store.shopping.pojo.GetOrderModelResponse;
import com.comma_store.shopping.pojo.Resource;

import java.util.Locale;
import java.util.Scanner;

import io.reactivex.Single;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class OrderDetailsViewModel extends ViewModel {

    CompositeDisposable disposable=new CompositeDisposable();
    MutableLiveData<Integer>ScreenState=new MutableLiveData<>(0);
    //screen State
    //0 loading
    //1 connect
    //2 error
    MutableLiveData<GetOrderModelResponse> orderDetails=new MutableLiveData<>();
    public void GetOrderDetails(int id,String apiKey){
        Single<Resource<GetOrderModelResponse>> orderDetailsRescponse = ItemClient.getINSTANCE().getItemInterface().getOrderDetails(Locale.getDefault().getLanguage(), apiKey, id).subscribeOn(Schedulers.io());

        disposable.add(orderDetailsRescponse.subscribe(x->{
            if (x.getStatus()==200){
                orderDetails.postValue(x.getData());
            }else if (x.getStatus()==400){
                ScreenState.postValue(2);
            }
        },e->ScreenState.postValue(2)));
    }
}