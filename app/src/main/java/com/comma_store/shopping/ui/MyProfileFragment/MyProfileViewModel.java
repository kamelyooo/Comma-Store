package com.comma_store.shopping.ui.MyProfileFragment;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.comma_store.shopping.Utils.SharedPreferencesUtils;
import com.comma_store.shopping.data.ItemClient;
import com.comma_store.shopping.pojo.CustomerModel;
import com.comma_store.shopping.pojo.Resource;

import java.util.Locale;

import io.reactivex.Single;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class MyProfileViewModel extends AndroidViewModel {
    // TODO: Implement the ViewModel
    MutableLiveData<Boolean> isSaveBtnLoading = new MutableLiveData<>();
    MutableLiveData<Boolean> isChangBtnLoading = new MutableLiveData<>();
    CompositeDisposable disposable=new CompositeDisposable();
    private Application context;

    public MyProfileViewModel(@NonNull Application application) {
        super(application);

        context = application;
    }

    public void UpdateProfile(String name,String apiKey,double lat,double longg,String address) {
        Single<Resource<CustomerModel>> customerModelSingle = ItemClient.getINSTANCE().getItemInterface().UpdateProfile(name, apiKey, longg, lat, address, Locale.getDefault().getLanguage()).subscribeOn(Schedulers.io());

        disposable.add(customerModelSingle.subscribe(x->{
            isSaveBtnLoading.postValue(false);
            if (x.getStatus()==200){
                SharedPreferencesUtils.getInstance(context).setCustomerName(x.getData().getName());
                SharedPreferencesUtils.getInstance(context).setCustomerAddress(x.getData().getAddress());
                SharedPreferencesUtils.getInstance(context).setCustomerLat(x.getData().getLatitude()+"");
                SharedPreferencesUtils.getInstance(context).setCustomerLong(x.getData().getLongitude()+"");
                Log.i("xxx200","suc");
            }else if (x.getStatus()==400){
                Log.i("xxx400",x.getFirstMessage());
            }
        },e->{
            isSaveBtnLoading.postValue(false);
            Log.i("xxxerror",e.getMessage());
        }));
    }
}