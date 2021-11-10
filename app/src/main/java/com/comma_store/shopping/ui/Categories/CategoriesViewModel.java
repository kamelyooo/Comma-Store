package com.comma_store.shopping.ui.Categories;

import android.content.ClipData;
import android.util.Log;
import android.view.View;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.comma_store.shopping.data.ItemClient;

import com.comma_store.shopping.pojo.CategoryScreenResposnse;
import com.comma_store.shopping.pojo.Resource;
import com.comma_store.shopping.pojo.SubCategory;

import java.util.IdentityHashMap;
import java.util.List;
import java.util.Locale;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class CategoriesViewModel extends ViewModel {

    int categorySelected = 0;
    boolean showSpinKitCategories = false;
    MutableLiveData<Boolean> isConnected = new MutableLiveData<>();
    MutableLiveData<List<CategoryScreenResposnse>> mutableLiveDataCategoryScreen =
            new MutableLiveData<>();
    CompositeDisposable disposable = new CompositeDisposable();

    public void getCategoryScreen() {
        disposable.add(ItemClient.getINSTANCE().getItemInterface().getCategoriesScreen(Locale.getDefault().getLanguage())
                .subscribeOn(Schedulers.io()).subscribe(x -> {
                            if (x.getStatus() == 200) {
                                mutableLiveDataCategoryScreen.postValue(x.getData());
                                isConnected.postValue(true);
                            } else isConnected.postValue(false);
                        }, e -> isConnected.postValue(false)
                ));
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.dispose();
        disposable.clear();
    }
}