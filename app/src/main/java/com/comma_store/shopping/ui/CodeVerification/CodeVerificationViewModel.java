package com.comma_store.shopping.ui.CodeVerification;

import android.util.Log;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

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

public class CodeVerificationViewModel extends ViewModel {
    MutableLiveData<String>TmpToken=new MutableLiveData<>();
    public void SendAgain(String email,FragmentActivity activity){
        ItemClient.getINSTANCE().getItemInterface().forgetPassword(email,Locale.getDefault().getLanguage()).subscribeOn(Schedulers.io())
                .subscribe(new SingleObserver<Resource<NullPointerException>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onSuccess(@NonNull Resource<NullPointerException> nullPointerExceptionResource) {
                        if (nullPointerExceptionResource.getStatus()==200){
                            SharedPreferencesUtils.getInstance(activity).setTmpToken(nullPointerExceptionResource.getTmpToken());
                            TmpToken.postValue(nullPointerExceptionResource.getTmpToken());
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }
                });
    }
    public  void validateCode(String TmpToken, String ActivationKey, CodeVerificationFragment codeVerificationFragment){
        ItemClient.getINSTANCE().getItemInterface().validateCode(TmpToken,ActivationKey, Locale.getDefault().getLanguage()).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleObserver<Resource<CustomerModel>>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onSuccess(@NonNull Resource<CustomerModel> customerModelResource) {
                if (customerModelResource.getStatus()==200){
                    SharedPreferencesUtils.getInstance(codeVerificationFragment.getActivity()).setCustomerInfo(customerModelResource.getData().getName(),
                            customerModelResource.getData().getEmail(),
                            customerModelResource.getData().getPhone(), customerModelResource.getData().getLatitude() + "",
                            customerModelResource.getData().getLongitude() + "", customerModelResource.getData().getAddress());
                    SharedPreferencesUtils.getInstance(codeVerificationFragment.getActivity()).setIsLogin(true);
                    codeVerificationFragment.getActivity().finish();
                }
                else if (customerModelResource.getStatus()==400){
                    codeVerificationFragment. binding.VerifyCodeBtn.setEnabled(true);
                    codeVerificationFragment. binding.VerifyTV.setText("Verify");
                    codeVerificationFragment.binding.spinKitVerifyBtn.setVisibility(View.GONE);
                    Toast.makeText(codeVerificationFragment.getActivity(), customerModelResource.getMessage().get("first_message"), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                codeVerificationFragment. binding.VerifyCodeBtn.setEnabled(true);
                codeVerificationFragment. binding.VerifyTV.setText("Verify");
                codeVerificationFragment.binding.spinKitVerifyBtn.setVisibility(View.GONE);
            }
        });
    }
}