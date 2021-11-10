package com.comma_store.shopping.ui.ItemDetails;

import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.andrognito.flashbar.Flashbar;
import com.andrognito.flashbar.anim.FlashAnim;
import com.comma_store.shopping.R;
import com.comma_store.shopping.data.CartDataBase;
import com.comma_store.shopping.pojo.CartItem;
import com.comma_store.shopping.pojo.ItemModel;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static android.telecom.TelecomManager.DURATION_LONG;
import static android.telecom.TelecomManager.DURATION_SHORT;

public class ItemDetailsViewModel extends ViewModel {
    Flashbar flashbar;

    MutableLiveData<CartItem> cartItem = new MutableLiveData<>();
    CompositeDisposable disposable = new CompositeDisposable();

    public void getItemFromDataBase(int id, FragmentActivity context) {
        disposable.add(CartDataBase.getInstance(context).itemDAO().getitembyId(id).subscribeOn(Schedulers.computation())
                .subscribe(x -> cartItem.postValue(x), e -> cartItem.postValue(null)));

    }

    public void insertItemCart(FragmentActivity getActivity, CartItem cartItem) {
        disposable.add(CartDataBase.getInstance(getActivity).itemDAO().insert(cartItem).subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).subscribe(x -> {
            flashbar = showFlashBar("Add Your Order", "Your Order has been Added To Cart Successfuly", getActivity, R.color.Green);
            flashbar.show();
        }, e -> {
            flashbar = showFlashBar("Error", "Some Thing Went Wrong", getActivity, R.color.DarkRed);
            flashbar.show();
        }));
    }

    public void updateItemCart(FragmentActivity getActivity, CartItem cartItem) {


        disposable.add(CartDataBase.getInstance(getActivity).itemDAO().update(cartItem).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
                x -> {
                    flashbar = showFlashBar("Update Your Order", "Your Order has been Updated Successfuly", getActivity, R.color.ToolBar);
                    flashbar.show();
                }, e -> {
                    flashbar = showFlashBar("Error", "Some Thing Went Wrong", getActivity, R.color.DarkRed);
                    flashbar.show();
                }
        ));
    }

    public void DeleteItemCart(FragmentActivity getActivity, CartItem cartItem) {


        disposable.add(CartDataBase.getInstance(getActivity).itemDAO().delete(cartItem).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).
                subscribe(x -> {
                    flashbar = showFlashBar("Delete Your Order", "Your Order has been Deleted Form Cart Successfuly", getActivity, R.color.DarkRed);
                    flashbar.show();
                }, e -> {
                    flashbar = showFlashBar("Error", "Some Thing Went Wrong", getActivity, R.color.DarkRed);
                    flashbar.show();
                }));
    }

    public Flashbar showFlashBar(String title, String msg, FragmentActivity getActivity, int backGroundColor) {
        return new Flashbar.Builder(getActivity)
                .gravity(Flashbar.Gravity.TOP)
                .title(title)
                .message(msg)
                .backgroundColorRes(backGroundColor)
                .enterAnimation(FlashAnim.with(getActivity)
                        .animateBar()
                        .duration(400)
                        .overshoot())
                .exitAnimation(FlashAnim.with(getActivity)
                        .animateBar()
                        .duration(400)
                        .accelerateDecelerate())
                .duration(2000)
                .icon(R.drawable.ic_notification)
                .enableSwipeToDismiss()
                .dismissOnTapOutside()
                .build();
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.dispose();
        disposable.clear();
    }
}