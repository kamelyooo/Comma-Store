package com.comma_store.shopping.ui.CompleteOrder;

import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.navigation.Navigation;

import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeErrorDialog;
import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeSuccessDialog;
import com.awesomedialog.blennersilva.awesomedialoglibrary.interfaces.Closure;
import com.comma_store.shopping.R;
import com.comma_store.shopping.data.CartDataBase;
import com.comma_store.shopping.data.ItemClient;
import com.comma_store.shopping.pojo.AddOrderModel;
import com.comma_store.shopping.pojo.CartItem;
import com.comma_store.shopping.pojo.CheckPromoCodeResponse;
import com.comma_store.shopping.pojo.Resource;
import com.comma_store.shopping.pojo.SettingResponse;
import com.crowdfire.cfalertdialog.CFAlertDialog;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import io.reactivex.SingleObserver;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class CompleteOrderFagmentViewModel extends ViewModel {
    MutableLiveData<Boolean> isConnected = new MutableLiveData<>();
    MutableLiveData<Boolean> isPromoCodeLoading = new MutableLiveData<>();
    MutableLiveData<Boolean>isSubmitLoading = new MutableLiveData<>();

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
                promoCodeSubmited = promoCode;
                isPromoCodeLoading.postValue(false);
                if (checkPromoCodeResponseResource.getStatus() == 200) {
                    promoCodeMin_cost.postValue(checkPromoCodeResponseResource.getData().getMin_cost());
                    promoCodeCut = checkPromoCodeResponseResource.getData().getCost();
                } else if (checkPromoCodeResponseResource.getStatus() == 400){
                    ErrorMsg.postValue(checkPromoCodeResponseResource.getFirstMessage());
                }

            }

            @Override
            public void onError(@NonNull Throwable e) {
                promoCodeSubmited = promoCode;
                isPromoCodeLoading.postValue(false);
                ErrorMsg.postValue("Some Thing Went Wrong Please Check The Internet And Try Again");
            }
        });
    }
   public  void AddOrder(AddOrderModel order, CompleteOrderFagment completeOrderFagment){
        ItemClient.getINSTANCE().getItemInterface().AddOrder2(order).subscribeOn(Schedulers.io())
                .subscribe(new SingleObserver<Resource<String>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onSuccess(@NonNull Resource<String> stringResource) {
                        isSubmitLoading.postValue(false);
                        completeOrderFagment.getActivity().runOnUiThread(() -> {
                            if (stringResource.getStatus()==200){
                                DeleteAll(completeOrderFagment);
                                Navigation.findNavController(completeOrderFagment.getView()).navigate(R.id.action_completeOrderFagment_to_cartFragment);
                                ShowSucsessDialog(completeOrderFagment);
                            }else if(stringResource.getStatus()==400){
                                if (stringResource.getMessage().containsKey("promocode")){
                                    ShowErrorDialog(completeOrderFagment,stringResource.getMessage().get("promocode"));

                                }else {
                                    ShowErrorDialog(completeOrderFagment,stringResource.getMessage().get("error"));
                                    Navigation.findNavController(completeOrderFagment.getView()).navigate(R.id.action_completeOrderFagment_to_cartFragment);

                                }
                            }else if (stringResource.getStatus()==401){
                                Navigation.findNavController(completeOrderFagment.getView()).navigate(R.id.action_completeOrderFagment_to_cartFragment);
                                ShowErrorDialog(completeOrderFagment,"You Are Not Allowed To make Order (You Are Sales)");
                            }
                        });
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        isSubmitLoading.postValue(false);
                        completeOrderFagment.getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(completeOrderFagment.getActivity(), "Some Thing Went Wrong Please Check the Internet And Try Again", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
   }


    private void ShowErrorDialog(CompleteOrderFagment completeOrderFagment,String ErrorMsg){
        new AwesomeErrorDialog(completeOrderFagment.getActivity())
                .setTitle("Order Error")
                .setMessage(ErrorMsg)
                .setColoredCircle(R.color.dialogErrorBackgroundColor)
                .setDialogIconAndColor(R.drawable.ic_dialog_error, R.color.white)
                .setButtonBackgroundColor(R.color.dialogErrorBackgroundColor)
                .setCancelable(true)
                .setButtonText("OK")
                .setErrorButtonClick(new Closure() {
                    @Override
                    public void exec() {
                        // click

                    }
                })
                .show();
    }

    private void ShowSucsessDialog(CompleteOrderFagment completeOrderFagment){
        new AwesomeSuccessDialog(completeOrderFagment.getActivity())
                .setTitle("Order State")
                .setMessage("Order Submitted")
                .setColoredCircle(R.color.dialogSuccessBackgroundColor)
                .setDialogIconAndColor(R.drawable.ic_success, R.color.white)
                .setPositiveButtonText("Ok")
                .setCancelable(true)
                .setPositiveButtonbackgroundColor(R.color.dialogSuccessBackgroundColor)
                .setPositiveButtonTextColor(R.color.white)
                .setPositiveButtonClick(new Closure() {
                    @Override
                    public void exec() {
                        //click

                    }
                })
                .show();

    }
    private void DeleteAll(CompleteOrderFagment completeOrderFagment){
        CartDataBase.getInstance(completeOrderFagment.getActivity()).itemDAO().deleteAll().subscribeOn(Schedulers.io())
                .subscribe(new SingleObserver<Void>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onSuccess(@NonNull Void aVoid) {
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }
                });

    }
}