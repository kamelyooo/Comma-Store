package com.comma_store.shopping.ui.Home;

import android.os.HardwarePropertiesManager;
import android.util.Log;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.comma_store.shopping.Utils.SharedPreferencesUtils;
import com.comma_store.shopping.data.ItemClient;
import com.comma_store.shopping.pojo.GetHomeResponse;
import com.comma_store.shopping.pojo.Resource;

import java.util.Locale;

import io.reactivex.Observer;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<GetHomeResponse> liveDatagetHomeResponse=new MutableLiveData<>();
    private MutableLiveData<String> onError = new MutableLiveData<>();
    private MutableLiveData<Boolean>Connect=new MutableLiveData<>();
    public MutableLiveData<Integer>SubId=new MutableLiveData<>();
    public void AddDeviceTokenGuest(String DeviceToken, FragmentActivity activity){
        ItemClient.getINSTANCE().getItemInterface().addDviceTokenGuest(DeviceToken,0).subscribeOn(Schedulers.io()).subscribe(new SingleObserver<Resource<String>>() {
            @Override
            public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {

            }

            @Override
            public void onSuccess(@io.reactivex.annotations.NonNull Resource<String> stringResource) {
                if (stringResource.getStatus()==200){
                    SharedPreferencesUtils.getInstance(activity).setDeviceTokenSentBoolean(true);
                }
            }

            @Override
            public void onError(@io.reactivex.annotations.NonNull Throwable e) {
                Log.i("xxx", e+"");

            }
        });

    }
    public void setConnect(boolean connect){
        if (connect){
            Connect.postValue(true);
        }else if (!connect){
            Connect.postValue(false);
        }
    }
    public void getHome(){
        ItemClient.getINSTANCE().getItemInterface().gethome(Locale.getDefault().getLanguage()).subscribeOn(Schedulers.io()).subscribe(new Observer<Resource<GetHomeResponse>>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
            }

            @Override
            public void onNext(@NonNull Resource<GetHomeResponse> getHomeResponseResource) {
                if(getHomeResponseResource.getStatus()==200){
                    liveDatagetHomeResponse.postValue(getHomeResponseResource.getData());
                }else {
                    if(getHomeResponseResource.getFirstMessage()!=null){
                        onError.postValue(getHomeResponseResource.getFirstMessage());
                    }
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.i("xxx",e+"");
                Connect.postValue(false);
            }

            @Override
            public void onComplete() {

            }
        });
    }

    public MutableLiveData<GetHomeResponse> getLiveDatagetHomeResponse() {

        return liveDatagetHomeResponse;
    }

    public MutableLiveData<String> getOnError() {
        return onError;
    }

    public MutableLiveData<Boolean> getConnect() {
        return Connect;
    }

}