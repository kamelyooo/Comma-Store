package com.comma_store.shopping.ui.Categories;

import android.content.ClipData;
import android.util.Log;
import android.view.View;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.comma_store.shopping.data.ItemClient;
import com.comma_store.shopping.pojo.CategoryModel;
import com.comma_store.shopping.pojo.CategoryScreenResposnse;
import com.comma_store.shopping.pojo.Resource;
import com.comma_store.shopping.pojo.SubCategory;

import java.util.IdentityHashMap;
import java.util.List;
import java.util.Locale;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class CategoriesViewModel extends ViewModel {

    int categorySelected=0;
    boolean getCategoriesForFristTime=false;
    boolean showSpinKitCategories=false;
    MutableLiveData<Boolean>isConnected=new MutableLiveData<>();
    MutableLiveData<List<CategoryScreenResposnse>> mutableLiveDataCategoryScreen=
            new MutableLiveData<>();
    public void getCategoryScreen(){
        ItemClient.getINSTANCE().getItemInterface().getCategoriesScreen(Locale.getDefault().getLanguage())
                .subscribeOn(Schedulers.io()).subscribe(new Observer<Resource<List<CategoryScreenResposnse>>>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(@NonNull Resource<List<CategoryScreenResposnse>> listResource) {
                if (listResource.getStatus()==200){
                    mutableLiveDataCategoryScreen.postValue(listResource.getData());
                    isConnected.postValue(true);
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                isConnected.postValue(false);

            }

            @Override
            public void onComplete() {

            }
        });
    }

}