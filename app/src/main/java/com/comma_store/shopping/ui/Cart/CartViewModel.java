package com.comma_store.shopping.ui.Cart;

import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.navigation.Navigation;

import com.comma_store.shopping.LogIn_Registration_Activity;
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
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class CartViewModel extends ViewModel {

    List<CartItem> cartItemsLocalLiveData = Arrays.asList();
    MutableLiveData<List<ItemModel>> cartItemsLiveData =new MutableLiveData<>();
    List<Integer> cartItemsIds;
    boolean vaild,validating;
    CompleteOrderModel lists;

    CartFragmentDirections.ActionCartFragmentToCompleteOrderFagment action;

    public void getItemsCartLocal(CartFragment cartFragment){
        CartDataBase.getInstance(cartFragment.getActivity()).itemDAO().GetItemsCart().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(
                new SingleObserver<List<CartItem>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onSuccess(@NonNull List<CartItem> cartItems) {
                        if (!cartItems.isEmpty()) {
                            cartItemsLocalLiveData= cartItems;
                            cartItemsIds = cartItems.stream().map(CartItem::getItem_id).collect(Collectors.toList());
                            getItem(cartFragment);
                        }else {
                            cartFragment.binding.cartScreen.setVisibility(View.INVISIBLE);
                            cartFragment.binding.ErrorScreenCartScreen.setVisibility(View.INVISIBLE);
                            cartFragment.binding.spinKitCartScreen.setVisibility(View.INVISIBLE);                                    cartFragment.binding.EmptyCartScreen.setVisibility(View.INVISIBLE);
                            cartFragment.binding.EmptyCartScreen.setVisibility(View.VISIBLE);
                        }
                    }
                    @Override
                    public void onError(@NonNull Throwable e) {

                    }
                }
        );
    }

    public void getItem(CartFragment cartFragment){
        ItemClient.getINSTANCE().getItemInterface().getItemsCart(Locale.getDefault().getLanguage(), cartItemsIds).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<Resource<List<ItemModel>>>() {
            @Override
            public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {

            }

            @Override
            public void onNext(@io.reactivex.annotations.NonNull Resource<List<ItemModel>> listResource) {
                cartItemsLiveData.postValue(listResource.getData());
                if (validating){
                    setVaild(listResource.getData(),cartFragment);
                }else {
                    getDataDone(cartFragment);
                }
            }

            @Override
            public void onError(@io.reactivex.annotations.NonNull Throwable e) {
                cartFragment.binding.cartScreen.setVisibility(View.INVISIBLE);
                cartFragment.binding.ErrorScreenCartScreen.setVisibility(View.VISIBLE);
                cartFragment.binding.spinKitCartScreen.setVisibility(View.INVISIBLE);
                cartFragment.binding.EmptyCartScreen.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onComplete() {

            }
        });

    }

    public void validateList() {
        vaild = true;
        validating=true;
    }

    private void setVaild(List<ItemModel>itemModels,CartFragment cartFragment){
        Optional<ItemModel> itemModelFound;
        for (int i = 0; i < cartItemsLocalLiveData.size(); i++) {
            int finalI = i;
            itemModelFound = itemModels.parallelStream().filter(x -> x.getId() ==cartItemsLocalLiveData.get(finalI).getItem_id()).findFirst();
            if (itemModelFound.isPresent()) {
                if (itemModelFound.get().getCount() < cartItemsLocalLiveData.get(finalI).getQuantity()||itemModelFound.get().getCount() == 0) {
                    vaild = false;
                    break;
                }
            }

        }

        if (vaild) {
            //make validat  if log in or not

            if (SharedPreferencesUtils.getInstance(cartFragment.getActivity()).getIsLogin()) {
                lists = new CompleteOrderModel(itemModels,cartItemsLocalLiveData);
                action = CartFragmentDirections.actionCartFragmentToCompleteOrderFagment(lists);
                action.setListsOfItem(lists);
                Navigation.findNavController(cartFragment.getView()).navigate(action);


            } else {
                Intent intent = new Intent(cartFragment.getActivity(), LogIn_Registration_Activity.class);
                intent.putExtra("FragmentKey", 1);
                cartFragment.startActivity(intent);

            }

        } else {

            Toast.makeText(cartFragment.getActivity(), "You Have To Make All Items Valid", Toast.LENGTH_SHORT).show();
        }
        getDataDone(cartFragment);
        validating=false;
    }




    private void getDataDone(CartFragment cartFragment){
        cartFragment.binding.cartScreen.setVisibility(View.VISIBLE);
        cartFragment.binding.spinKitCartScreen.setVisibility(View.INVISIBLE);
        cartFragment.binding.shadow.setVisibility(View.INVISIBLE);
        cartFragment.binding.CompleteOrderTV.setText("Complete Your Order");
        cartFragment.binding.spinKitCompleteYourOrderBtn.setVisibility(View.INVISIBLE);
        cartFragment.binding.CompleteOrderCartScreen.setEnabled(true);
        cartFragment.binding.EmptyCartScreen.setVisibility(View.INVISIBLE);
    }




    public void updateItemCart(FragmentActivity getActivity,CartItem cartItem){
        CartDataBase.getInstance(getActivity).itemDAO().update(cartItem).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleObserver<Integer>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onSuccess(@NonNull Integer integer) {

            }

            @Override
            public void onError(@NonNull Throwable e) {

            }
        });
    }
    public void DeleteItemCart(FragmentActivity getActivity,CartItem cartItem){
        CartDataBase.getInstance(getActivity).itemDAO().delete(cartItem).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).
                subscribe(new SingleObserver<Integer>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onSuccess(@NonNull Integer integer) {

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }
                });
    }
}