package com.comma_store.shopping.ui.Cart;

import android.app.Application;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.navigation.Navigation;

import com.comma_store.shopping.LogIn_Registration_Activity;
import com.comma_store.shopping.R;
import com.comma_store.shopping.Utils.SharedPreferencesUtils;
import com.comma_store.shopping.data.CartDataBase;
import com.comma_store.shopping.data.ItemClient;
import com.comma_store.shopping.pojo.CartItem;
import com.comma_store.shopping.pojo.CompleteOrderModel;
import com.comma_store.shopping.pojo.ItemModel;
import com.comma_store.shopping.pojo.Resource;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

import io.reactivex.Observer;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class CartViewModel extends AndroidViewModel {

    List<CartItem> cartItemsLocalLiveData = Arrays.asList();
    MutableLiveData<List<ItemModel>> cartItemsLiveData = new MutableLiveData<>();
    List<Integer> cartItemsIds;
    boolean vaild;
    CompleteOrderModel lists;
    MutableLiveData<Integer> screenState = new MutableLiveData<>(3);
    CompositeDisposable disposable = new CompositeDisposable();
    private Application context;
    MutableLiveData<Integer> Navigate = new MutableLiveData<>();

    public CartViewModel(@androidx.annotation.NonNull Application application) {
        super(application);
        context = application;
    }

    //state 0 cart Screen
    //state 1 error Screen
    //state 2 empty Screen
    //state 3 loading

    public void getItemCartLocal(boolean isValidating) {
        disposable.add(CartDataBase.getInstance(context).itemDAO().GetItemsCart().subscribeOn(Schedulers.io())
                .subscribe(itemCartLocal -> {
                            if (!itemCartLocal.isEmpty()) {
                                cartItemsLocalLiveData = itemCartLocal;
                                cartItemsIds = itemCartLocal.stream().map(CartItem::getItem_id).collect(Collectors.toList());
                                getItem(isValidating);
                            } else {
                                screenState.postValue(2);
                            }
                        }
                ));
    }

    public void getItem(boolean isValidatin) {
        disposable.add(ItemClient.getINSTANCE().getItemInterface().getItemsCart(Locale.getDefault().getLanguage(), cartItemsIds).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(x -> {
                            if (x.getStatus() == 200) {
                                if (isValidatin) {
                                    setVaild(x.getData());
                                } else {
                                    cartItemsLiveData.postValue(x.getData());
                                    screenState.postValue(0);
                                }
                            }
                        }, e ->
                                screenState.postValue(1)
                ));

    }

    private void setVaild(List<ItemModel> itemModels) {
        vaild = true;
        Optional<ItemModel> itemModelFound;
        for (int i = 0; i < cartItemsLocalLiveData.size(); i++) {
            int finalI = i;
            itemModelFound = itemModels.parallelStream().filter(x -> x.getId() == cartItemsLocalLiveData.get(finalI).getItem_id()).findFirst();
            if (itemModelFound.isPresent()) {
                if (itemModelFound.get().getCount() < cartItemsLocalLiveData.get(finalI).getQuantity() || itemModelFound.get().getCount() == 0) {
                    vaild = false;
                    break;
                }
            }
        }
        if (vaild) {
            //make validat  if log in or not
            if (SharedPreferencesUtils.getInstance(context).getIsLogin()) {
                lists = new CompleteOrderModel(itemModels, cartItemsLocalLiveData);
                Navigate.postValue(0);
            } else {
                Navigate.postValue(1);
            }
        } else {
            cartItemsLiveData.postValue(itemModels);
            Toast.makeText(context.getApplicationContext(), "You Have To Make All Items Valid", Toast.LENGTH_SHORT).show();
        }
        screenState.postValue(0);

    }

    public void updateItemCart( CartItem cartItem) {
       disposable.add(CartDataBase.getInstance(context).itemDAO().update(cartItem).subscribeOn(Schedulers.io()).subscribe());
    }

    public void DeleteItemCart(CartItem cartItem) {
        disposable.add(CartDataBase.getInstance(context).itemDAO().delete(cartItem).subscribeOn(Schedulers.io()).
                subscribe());
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.clear();
        disposable.dispose();
    }
}