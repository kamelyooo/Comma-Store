package com.comma_store.shopping.ui.Deals;

import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.paging.PageKeyedDataSource;

import com.comma_store.shopping.data.ItemClient;
import com.comma_store.shopping.pojo.ItemModel;
import com.comma_store.shopping.pojo.Resource;

import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ItemDataSourceDeals extends PageKeyedDataSource<Integer, ItemModel> {

    private static final int FIRST_PAGE = 1;
    private String orderBy;
    private String lang;
DealsFragment dealsFragment;

    public ItemDataSourceDeals(String orderBy, String lang, DealsFragment dealsFragment) {
        this.orderBy = orderBy;
        this.lang = lang;
        this.dealsFragment = dealsFragment;
    }




    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull LoadInitialCallback<Integer, ItemModel> callback) {
        ItemClient.getINSTANCE().getItemInterface().getDealsItems(lang,orderBy,FIRST_PAGE).subscribeOn(Schedulers.io())
               .subscribe(new Observer<Resource<List<ItemModel>>>() {
                    @Override
                    public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@io.reactivex.annotations.NonNull Resource<List<ItemModel>> listResource) {
                        if (listResource!=null) {
                            callback.onResult(listResource.getData(), null, FIRST_PAGE + 1);
                            dealsFragment.mViewModel.isConnected.postValue(true);
                        }
                    }

                    @Override
                    public void onError(@io.reactivex.annotations.NonNull Throwable e) {


                        dealsFragment.mViewModel.isConnected.postValue(false);

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void loadBefore(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, ItemModel> callback) {
        ItemClient.getINSTANCE().getItemInterface().getDealsItems(lang,orderBy,params.key).subscribeOn(Schedulers.io())
                .subscribe(new Observer<Resource<List<ItemModel>>>() {
                    @Override
                    public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@io.reactivex.annotations.NonNull Resource<List<ItemModel>> listResource) {
                        Integer adjacentKey = (params.key > 1) ? params.key - 1 : null;
                        if (listResource!=null)
                            callback.onResult(listResource.getData(),adjacentKey);
                    }

                    @Override
                    public void onError(@io.reactivex.annotations.NonNull Throwable e) {

                        dealsFragment.mViewModel.isConnected.postValue(false);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void loadAfter(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, ItemModel> callback) {
        ItemClient.getINSTANCE().getItemInterface().getDealsItems(lang,orderBy,params.key).subscribeOn(Schedulers.io())
                .subscribe(new Observer<Resource<List<ItemModel>>>() {
                    @Override
                    public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@io.reactivex.annotations.NonNull Resource<List<ItemModel>> listResource) {


                        if (listResource!=null){
                            Integer key = listResource.isMore() ? params.key + 1 : null;
                            callback.onResult(listResource.getData(), key);
                        }
                    }

                    @Override
                    public void onError(@io.reactivex.annotations.NonNull Throwable e) {
                        dealsFragment.mViewModel.isConnected.postValue(false);

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
