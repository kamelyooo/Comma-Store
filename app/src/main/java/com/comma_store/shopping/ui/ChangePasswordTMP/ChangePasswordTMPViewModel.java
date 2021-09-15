package com.comma_store.shopping.ui.ChangePasswordTMP;

import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.navigation.Navigation;

import com.comma_store.shopping.R;
import com.comma_store.shopping.Utils.SharedPreferencesUtils;
import com.comma_store.shopping.data.ItemClient;
import com.comma_store.shopping.pojo.Resource;

import java.util.IdentityHashMap;
import java.util.Locale;

import io.reactivex.SingleObserver;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ChangePasswordTMPViewModel extends ViewModel {
    MutableLiveData<String> TmpToken=new MutableLiveData<>();
    MutableLiveData<Boolean> isLoading=new MutableLiveData<>();
    public void SendAgain(String email, FragmentActivity activity){
        ItemClient.getINSTANCE().getItemInterface().forgetPassword(email, Locale.getDefault().getLanguage()).subscribeOn(Schedulers.io())
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

    public void ChangePasswordTmp(String TmpToken,String passWord,String activationKen,ChangePasswordTMPFragment fragment){
        ItemClient.getINSTANCE().getItemInterface().ChangePasswordTmp(TmpToken,passWord,activationKen,Locale.getDefault().getLanguage()).subscribeOn(Schedulers.io())
                .subscribe(new SingleObserver<Resource<NullPointerException>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onSuccess(@NonNull Resource<NullPointerException> nullPointerExceptionResource) {
                        if (nullPointerExceptionResource.getStatus()==200){
                            fragment.getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Navigation.findNavController(fragment.getView()).navigate(R.id.action_changePasswordTMPFragment_to_logInFragment);
                                }
                            });
                        }else if (nullPointerExceptionResource.getStatus()==400){
                            isLoading.postValue(false);
                            if (nullPointerExceptionResource.getMessage().containsKey("error")){
                                fragment.getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(fragment.getActivity(), nullPointerExceptionResource.getMessage().get("error")+"", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                            if (nullPointerExceptionResource.getMessage().containsKey("password")){
                                fragment.getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        fragment.binding.enterNewPassInput.setError(nullPointerExceptionResource.getMessage().get("password"));
                                        fragment.binding.confirmPassInput.setError(nullPointerExceptionResource.getMessage().get("password"));
                                        fragment.binding.enterNewPassInput.setErrorIconDrawable(null);
                                        fragment.binding.confirmPassInput.setErrorIconDrawable(null);

                                    }
                                });
                                }
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        isLoading.postValue(false);
                    }
                });
    }

}