package com.comma_store.shopping.ui.MyProfileFragment;

import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.comma_store.shopping.R;
import com.comma_store.shopping.Utils.SharedPreferencesUtils;
import com.comma_store.shopping.data.ItemClient;
import com.comma_store.shopping.pojo.CustomerModel;
import com.comma_store.shopping.pojo.Resource;
import com.crowdfire.cfalertdialog.CFAlertDialog;

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
    boolean gps_enabled;
    boolean network_enabled;
    LocationManager lm;

    MutableLiveData<String>msg=new MutableLiveData<>();
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
                msg.postValue("Saved");
            }else if (x.getStatus()==400){
                msg.postValue( x.getFirstMessage());
            }
        },e->{
            isSaveBtnLoading.postValue(false);
            msg.postValue("Some Thing went Wrong Please Try Again");
        }));
    }
    public void ChangePassWord(String apiKey,String OldPass,String newPass){
        Single<Resource<String>> ChangePass = ItemClient.getINSTANCE().getItemInterface().ChangePassword(apiKey, OldPass, newPass, SharedPreferencesUtils.getInstance(context).getLangKey()==0?"en":"ar").subscribeOn(Schedulers.io());
        disposable.add(ChangePass.subscribe(x->{
            isChangBtnLoading.postValue(false);
            if (x.getStatus()==200){
                msg.postValue("Your Pass is Changed");
            }else if (x.getStatus()==400){
                msg.postValue(x.getFirstMessage());
            }
        },e->{
            isChangBtnLoading.postValue(false);
            msg.postValue("Some Thing went Wrong Please Try Again");
        }));
    }
    public void checkLocationOpened() {
        gps_enabled = false;
        network_enabled = false;
        lm = (LocationManager) context.getSystemService(context.LOCATION_SERVICE);

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
        }

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
        }

    }
    public void showLocationSettingDiaog(FragmentActivity activity) {

        new CFAlertDialog.Builder(activity)
                .setDialogStyle(CFAlertDialog.CFAlertStyle.ALERT)
                .setTitle("Location not enabled!!")
                .setIcon(R.drawable.ic_baseline_location_on_24)
                .setTextColor(activity.getResources().getColor(R.color.HeaderTextColor))
                .addButton("open location settings", activity.getResources().getColor(R.color.white), activity.getResources().getColor(R.color.Buttons), CFAlertDialog.CFAlertActionStyle.POSITIVE, CFAlertDialog.CFAlertActionAlignment.JUSTIFIED, (dialog, which) -> {
                    activity.startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    dialog.dismiss();
                })
                .addButton("Cancel", activity.getResources().getColor(R.color.Buttons), activity.getResources().getColor(R.color.white), CFAlertDialog.CFAlertActionStyle.DEFAULT, CFAlertDialog.CFAlertActionAlignment.JUSTIFIED, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();

    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.dispose();
        disposable.clear();
    }
}