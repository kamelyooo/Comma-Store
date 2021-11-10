package com.comma_store.shopping.ui.Home;

import android.app.Application;
import android.os.HardwarePropertiesManager;
import android.util.Log;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.comma_store.shopping.Utils.SharedPreferencesUtils;
import com.comma_store.shopping.data.CartDataBase;
import com.comma_store.shopping.data.ItemClient;
import com.comma_store.shopping.pojo.FavoriteItem;
import com.comma_store.shopping.pojo.GetHomeResponse;
import com.comma_store.shopping.pojo.Resource;

import java.util.Locale;

import io.reactivex.Observer;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class HomeViewModel extends AndroidViewModel {

    private MutableLiveData<GetHomeResponse> liveDatagetHomeResponse=new MutableLiveData<>();
    private MutableLiveData<String> onError = new MutableLiveData<>();
    private MutableLiveData<Boolean>Connect=new MutableLiveData<>();
    CompositeDisposable disposable=new CompositeDisposable();
    private Application context;

    public HomeViewModel(@androidx.annotation.NonNull Application application) {
        super(application);
        context = application;
    }

    public void AddDeviceTokenGuest(String DeviceToken){
       disposable.add( ItemClient.getINSTANCE().getItemInterface().addDviceTokenGuest(DeviceToken,0).subscribeOn(Schedulers.io()).subscribe(x->{
        if (x.getStatus()==200){
            SharedPreferencesUtils.getInstance(context).setDeviceTokenSentBoolean(true);
        }
        }));

    }
    public void setConnect(boolean connect){
        if (connect){
            Connect.postValue(true);
        }else if (!connect){
            Connect.postValue(false);
        }
    }





    public void getHome(){
        disposable.add(ItemClient.getINSTANCE().getItemInterface().gethome(Locale.getDefault().getLanguage()).subscribeOn(Schedulers.io()).subscribe(x->{
            if(x.getStatus()==200){
                liveDatagetHomeResponse.postValue(x.getData());
            }else {
                if(x.getFirstMessage()!=null){
                    onError.postValue(x.getFirstMessage());
                }
            }
        },e->  Connect.postValue(false)));
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

    public void insertFavorite(int itemId){
        CartDataBase.getInstance(context).favoriteItemsDAO().insetFavoriteItem(new FavoriteItem(itemId))
                .subscribeOn(Schedulers.io()).subscribe();
    }
    public void DeleteFavorite(int itemId){
        CartDataBase.getInstance(context).favoriteItemsDAO().DeleteFavoriteItem(new FavoriteItem(itemId))
                .subscribeOn(Schedulers.io()).subscribe();
    }
    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.clear();
        disposable.dispose();
    }
}