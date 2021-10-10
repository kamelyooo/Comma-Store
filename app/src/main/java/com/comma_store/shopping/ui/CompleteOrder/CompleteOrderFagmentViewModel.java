package com.comma_store.shopping.ui.CompleteOrder;

import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.provider.Settings;
import android.util.Log;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.comma_store.shopping.R;
import com.comma_store.shopping.data.ItemClient;
import com.comma_store.shopping.pojo.CheckPromoCodeResponse;
import com.comma_store.shopping.pojo.CompleteOrderModel;
import com.comma_store.shopping.pojo.ItemModel;
import com.comma_store.shopping.pojo.Resource;
import com.comma_store.shopping.pojo.SettingResponse;
import com.crowdfire.cfalertdialog.CFAlertDialog;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import io.reactivex.SingleObserver;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class CompleteOrderFagmentViewModel extends ViewModel {
    MutableLiveData<Boolean> isConnected = new MutableLiveData<>();
    MutableLiveData<Boolean> isPromoCodeLoading = new MutableLiveData<>();
    MutableLiveData<Integer> promoCodeMin_cost = new MutableLiveData<>();
    MutableLiveData<String> ErrorMsg=new MutableLiveData<>();

    int promoCodeCut = 0;
    List<SettingResponse> DataResponse = Arrays.asList();
    boolean spinKitIsShow = false;
    boolean gps_enabled;
    boolean network_enabled;
    String promoCodeSubmited;
    LocationManager lm;

    public void getSettings() {
        ItemClient.getINSTANCE().getItemInterface().getSettings(Locale.getDefault().getLanguage()).subscribeOn(Schedulers.io())
                .subscribe(new SingleObserver<Resource<List<SettingResponse>>>() {
                    @Override
                    public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {

                    }

                    @Override
                    public void onSuccess(@io.reactivex.annotations.NonNull Resource<List<SettingResponse>> listResource) {
                        DataResponse = listResource.getData();
                        isConnected.postValue(true);
                    }

                    @Override
                    public void onError(@io.reactivex.annotations.NonNull Throwable e) {
                        isConnected.postValue(false);
                    }
                });
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

    public void checkLocationOpened(FragmentActivity activity) {
        gps_enabled = false;
        network_enabled = false;
        lm = (LocationManager) activity.getSystemService(activity.LOCATION_SERVICE);

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
        }

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
        }

    }

    public void checkPromoCode(String apiKey, String promoCode) {
        ItemClient.getINSTANCE().getItemInterface().checkPromoCode(Locale.getDefault().getLanguage(), apiKey, promoCode)
                .subscribeOn(Schedulers.io()).subscribe(new SingleObserver<Resource<CheckPromoCodeResponse>>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onSuccess(@NonNull Resource<CheckPromoCodeResponse> checkPromoCodeResponseResource) {
                isPromoCodeLoading.postValue(false);
                if (checkPromoCodeResponseResource.getStatus() == 200) {
                    promoCodeMin_cost.postValue(checkPromoCodeResponseResource.getData().getMin_cost());
                    promoCodeCut = checkPromoCodeResponseResource.getData().getCost();
                    promoCodeSubmited=promoCode;
                }
                Log.i("xxx", checkPromoCodeResponseResource.getStatus() + "");
                if (checkPromoCodeResponseResource.getStatus() == 400)
                    Log.i("xxx", checkPromoCodeResponseResource.getFirstMessage());
            }

            @Override
            public void onError(@NonNull Throwable e) {
                isPromoCodeLoading.postValue(false);

            }
        });
    }

}