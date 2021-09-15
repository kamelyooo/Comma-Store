package com.comma_store.shopping.ui.Search;

import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.paging.PageKeyedDataSource;

import com.comma_store.shopping.data.ItemClient;
import com.comma_store.shopping.pojo.ItemModel;
import com.comma_store.shopping.pojo.Resource;
import com.comma_store.shopping.ui.Deals.DealsFragment;

import java.util.List;
import java.util.Locale;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ItemDataSourceSearch extends PageKeyedDataSource<Integer, ItemModel> {

    private static final int FIRST_PAGE = 1;
    private String orderBy;
    private String q;
    SearchFragment searchFragment;



    public ItemDataSourceSearch(String orderBy, String q,SearchFragment searchFragment) {
        this.orderBy = orderBy;
        this.q = q;
        this.searchFragment = searchFragment;
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull LoadInitialCallback<Integer, ItemModel> callback) {


        ItemClient.getINSTANCE()
                .getItemInterface().getSearchItems(Locale.getDefault().getLanguage(),q,orderBy,FIRST_PAGE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Resource<List<ItemModel>>>() {
            @Override
            public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {

            }

            @Override
            public void onNext(@io.reactivex.annotations.NonNull Resource<List<ItemModel>> listResource) {
                callback.onResult(listResource.getData(),null,FIRST_PAGE+1);

                if (listResource.getData().size()==0){
                    searchFragment.binding.spinKitSearchScreen.setVisibility(View.INVISIBLE);
                    searchFragment.binding.noThingFoundImage.setVisibility(View.VISIBLE);
                    searchFragment.mViewModel.nothigeFoundImge=true;
                    searchFragment.binding.recycleViewSearchScreen.setVisibility(View.INVISIBLE);
                    searchFragment.mViewModel.recycleVisible=false;
                }else {
                    searchFragment.binding.spinKitSearchScreen.setVisibility(View.INVISIBLE);
                    searchFragment.binding.recycleViewSearchScreen.setVisibility(View.VISIBLE);
                    searchFragment.mViewModel.recycleVisible=true;
                }
            }

            @Override
            public void onError(@io.reactivex.annotations.NonNull Throwable e) {
                searchFragment.binding.SearchScreen.setVisibility(View.INVISIBLE);
                searchFragment.mViewModel.SearchScreen=false;
                searchFragment.binding.errorScreenSearchScreen.setVisibility(View.VISIBLE);
                searchFragment.mViewModel.errorScreen=true;
                searchFragment.binding.spinKitSearchScreen.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onComplete() {

            }
        });

    }

    @Override
    public void loadBefore(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, ItemModel> callback) {

        ItemClient.getINSTANCE()
                .getItemInterface().getSearchItems(Locale.getDefault().getLanguage(),q,orderBy,params.key)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())

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

                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    @Override
    public void loadAfter(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, ItemModel> callback) {

        ItemClient.getINSTANCE()
                .getItemInterface().getSearchItems(Locale.getDefault().getLanguage(),q,orderBy,params.key)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
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

                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }
//    private void errorConnection(){
//        dealsFragment.mViewModel.setShowSpinKit(false);
//        dealsFragment.binding.spinKitDeals.setVisibility(View.INVISIBLE);
//        dealsFragment.binding.DealsScreen.setVisibility(View.INVISIBLE);
//        dealsFragment.binding.dealErrorConnection.setVisibility(View.VISIBLE);
//    }
}
