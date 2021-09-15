package com.comma_store.shopping.ui.LogInFragment;

import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.navigation.Navigation;

import com.awesomedialog.blennersilva.awesomedialoglibrary.AwesomeErrorDialog;
import com.awesomedialog.blennersilva.awesomedialoglibrary.interfaces.Closure;
import com.comma_store.shopping.R;
import com.comma_store.shopping.Utils.SharedPreferencesUtils;
import com.comma_store.shopping.data.ItemClient;
import com.comma_store.shopping.pojo.CustomerModel;
import com.comma_store.shopping.pojo.Resource;

import java.util.Locale;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class LogInViewModel extends ViewModel {
    MutableLiveData<Boolean> IsLoading=new MutableLiveData<>();
    public void login(LogInFragment logInFragment, String email, String password, String deviceToken) {

        ItemClient.getINSTANCE().getItemInterface().login(email, password, 0, deviceToken, Locale.getDefault().getLanguage())
                .subscribeOn(Schedulers.io()).subscribe(new SingleObserver<Resource<CustomerModel>>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onSuccess(@NonNull Resource<CustomerModel> customerModelResource) {
                if (customerModelResource.getStatus() == 200) {
                    SharedPreferencesUtils.getInstance(logInFragment.getActivity()).setCustomerInfo(customerModelResource.getData().getName(),
                            customerModelResource.getData().getEmail(),
                            customerModelResource.getData().getPhone(), customerModelResource.getData().getLatitude() + "",
                            customerModelResource.getData().getLongitude() + "", customerModelResource.getData().getAddress());
                    SharedPreferencesUtils.getInstance(logInFragment.getActivity()).setIsLogin(true);
                    logInFragment.getActivity().finish();
                } else if (customerModelResource.getStatus()==400){
                    IsLoading.postValue(false);
                    if (customerModelResource.getMessage().containsKey("error")){
                        logInFragment.getActivity().runOnUiThread(() -> ShowDialog(logInFragment));

                    }
                    if (customerModelResource.getMessage().containsKey("email")){
                        logInFragment.getActivity().runOnUiThread(() -> logInFragment.binding.textInputEmail.setError(customerModelResource.getMessage().get("email")));
                    }
                    if (customerModelResource.getMessage().containsKey("password")){
                        logInFragment.getActivity().runOnUiThread(() -> {
                            logInFragment.binding.passwordInputLogInScreen.setError(customerModelResource.getMessage().get("password"));
                            logInFragment.binding.passwordInputLogInScreen.setErrorIconDrawable(null);
                        });

                    }

                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                IsLoading.postValue(false);
                logInFragment.getActivity().runOnUiThread(() -> Toast.makeText(logInFragment.getActivity(), "Some Thing Went Wrong Please Try Again", Toast.LENGTH_SHORT).show());
            }
        });
    }

        private void ShowDialog(LogInFragment logInFragment){

            String tmpToken = SharedPreferencesUtils.getInstance(logInFragment.getActivity()).getTmpToken();
            new AwesomeErrorDialog(logInFragment.getActivity())
                .setTitle("Validation Error")
                .setMessage("This Email is Not Valid")
                .setColoredCircle(R.color.dialogErrorBackgroundColor)
                .setDialogIconAndColor(R.drawable.ic_dialog_error, R.color.white)
                .setCancelable(true)
                .setButtonText("Validate Email")

                .setErrorButtonClick(new Closure() {
                    @Override
                    public void exec() {
                        // click
                        if (tmpToken!=null){
                            LogInFragmentDirections.ActionLogInFragmentToRecieveCodeVerificationFragment action=
                                    LogInFragmentDirections.actionLogInFragmentToRecieveCodeVerificationFragment();
                            action.setEmail(logInFragment.binding.EmailETLogINScreen.getText().toString());
                            action.setTmpToken(tmpToken);
                            Navigation.findNavController(logInFragment.getView()).navigate(action);
                        }else {
                            LogInFragmentDirections.ActionLogInFragmentToSendCodeVerificationFragment action=
                                    LogInFragmentDirections.actionLogInFragmentToSendCodeVerificationFragment();
                            action.setResone(1);
                            Navigation.findNavController(logInFragment.getView()).navigate(action);

                        }
                    }
                })
                .show();
    }
}