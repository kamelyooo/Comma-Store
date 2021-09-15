package com.comma_store.shopping.ui.SendCodeVerification;

import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.navigation.Navigation;

import com.comma_store.shopping.data.ItemClient;
import com.comma_store.shopping.pojo.Resource;

import java.util.Locale;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class SendCodeVerificationViewModel extends ViewModel {
    MutableLiveData<Boolean>IsLoading=new MutableLiveData<>();
    public void SendCode(String email,SendCodeVerificationFragment fragment,int reson){
        ItemClient.getINSTANCE().getItemInterface().forgetPassword(email, Locale.getDefault().getLanguage()).subscribeOn(Schedulers.io())
                .subscribe(new SingleObserver<Resource<NullPointerException>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onSuccess(@NonNull Resource<NullPointerException> nullPointerExceptionResource) {
                        if (nullPointerExceptionResource.getStatus()==200){
                            if (reson==0){
                                SendCodeVerificationFragmentDirections.ActionSendCodeVerificationFragmentToChangePasswordTMPFragment action=
                                        SendCodeVerificationFragmentDirections.actionSendCodeVerificationFragmentToChangePasswordTMPFragment();
                                action.setTmpToken(nullPointerExceptionResource.getTmpToken());
                                action.setEmail(email);
                                fragment.getActivity().runOnUiThread(() -> Navigation.findNavController(fragment.getView()).navigate(action));

                            }else if (reson==1){
                                SendCodeVerificationFragmentDirections.ActionSendCodeVerificationFragmentToRecieveCodeVerificationFragment action=
                                        SendCodeVerificationFragmentDirections.actionSendCodeVerificationFragmentToRecieveCodeVerificationFragment();
                                action.setEmail(email);
                                action.setTmpToken(nullPointerExceptionResource.getTmpToken());
                                fragment.getActivity().runOnUiThread(() -> Navigation.findNavController(fragment.getView()).navigate(action));

                            }

                        }else if (nullPointerExceptionResource.getStatus()==400){
                            fragment.getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    fragment.binding.textInputEmail.setError(nullPointerExceptionResource.getFirstMessage());

                                }
                            });
                            IsLoading.postValue(false);
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e)
                    {
                        IsLoading.postValue(false);
                        fragment.getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(fragment.getActivity(), "Some Thing Went Wrong Please Try Again", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                });
    }
}