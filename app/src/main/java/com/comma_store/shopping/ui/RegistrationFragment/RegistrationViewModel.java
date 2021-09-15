package com.comma_store.shopping.ui.RegistrationFragment;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.exifinterface.media.ExifInterface;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModel;
import androidx.navigation.Navigation;

import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeErrorDialog;
import com.awesomedialog.blennersilva.awesomedialoglibrary.interfaces.Closure;
import com.comma_store.shopping.R;
import com.comma_store.shopping.Utils.SharedPreferencesUtils;
import com.comma_store.shopping.data.ItemClient;
import com.comma_store.shopping.pojo.RegisterResponse;
import com.crowdfire.cfalertdialog.CFAlertDialog;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import java.util.Locale;

public class RegistrationViewModel extends ViewModel {
    // TODO: Implement the ViewModel
    boolean gps_enabled;
    boolean network_enabled;
    LocationManager lm;
    public void Registration(String userName, String PhoneNumber, String Password, String Email, double longg, double lat,
                             String Location, String DeviceToken, View view,RegistrationFragment registrationFragment){
                            ItemClient.getINSTANCE().getItemInterface()
                            .Register(
                                    userName,
                                    PhoneNumber,
                                    Password,
                                    0,
                                    Email,
                                    longg,
                                    lat,
                                    Location,
                                    Locale.getDefault().getLanguage(),
                                    DeviceToken).subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleObserver<RegisterResponse>() {
                        @Override
                        public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {

                        }

                        @Override
                        public void onSuccess(@io.reactivex.annotations.NonNull RegisterResponse registerResponse) {
                            registrationFragment.binding.spinKitSignUpBtn.setVisibility(View.GONE);
                            registrationFragment.binding.signUpTV.setText("Sign Up");
                            registrationFragment.binding.signUpButtonRegisterScreen.setEnabled(true);
                           if (registerResponse.getStatus()==200){
                               SharedPreferencesUtils.getInstance(registrationFragment.getActivity())
                                       .setTmpToken(registerResponse.getTmpToken());
                              RegistrationFragmentDirections.ActionRegistrationFragmentToRecieveCodeVerificationFragment action=
                                      RegistrationFragmentDirections.actionRegistrationFragmentToRecieveCodeVerificationFragment();
                              action.setEmail(Email);
                              action.setTmpToken(registerResponse.getTmpToken());
                              Navigation.findNavController(view).navigate(action);
                           }else if(registerResponse.getStatus()==400){
                               registrationFragment.binding.ErrorMassegeTVRegisterScreen.setVisibility(View.VISIBLE);
                            }
                        }

                        @Override
                        public void onError(@io.reactivex.annotations.NonNull Throwable e) {
                            Toast.makeText(registrationFragment.getActivity(), "Some Thing Went Wrong Please Try Again", Toast.LENGTH_SHORT).show();
                        }
                    });
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
//    private void ShowDialog(FragmentActivity activity){
//        new AwesomeErrorDialog(activity)
//                .setTitle(R.string.app_name)
//                .setMessage(R.string.app_name)
//                .setColoredCircle(R.color.dialogErrorBackgroundColor)
//                .setDialogIconAndColor(R.drawable.ic_dialog_error, R.color.white)
//                .setCancelable(true)
//                .setButtonText("Ok")
//                .setButtonBackgroundColor(R.color.dialogErrorBackgroundColor)
//
//                .setErrorButtonClick(new Closure() {
//                    @Override
//                    public void exec() {
//                        // click
//
//                    }
//                })
//                .show();
//    }
}