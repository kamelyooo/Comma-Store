package com.comma_store.shopping.ui.Search;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyboardShortcutGroup;
import android.view.View;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.navigation.Navigation;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PageKeyedDataSource;
import androidx.paging.PagedList;

import com.comma_store.shopping.R;
import com.comma_store.shopping.data.ItemClient;
import com.comma_store.shopping.pojo.ItemModel;
import com.comma_store.shopping.pojo.Resource;

import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class SearchViewModel extends ViewModel {
    LiveData itemPagedList;
    String sortByText="idDesc";
    String queryText="";
    LiveData<PageKeyedDataSource<Integer, ItemModel>> liveDataSource;
    ItemDataSourceFactorySearch itemDataSourceFactory;
    CompositeDisposable disposable=new CompositeDisposable();
    boolean observe =false;
    boolean recycleVisible=false;
    boolean SearchForSomeThingImage=true;
    boolean nothigeFoundImge=false;
    boolean errorScreen=false;
    boolean SearchScreen=true;
    public void GetSearchResult(SearchFragment searchFragment){

        if (itemDataSourceFactory==null){
            itemDataSourceFactory = new ItemDataSourceFactorySearch(searchFragment);
            //getting the live data source from data source factory
            liveDataSource = itemDataSourceFactory.getItemLiveDataSource();
        //Getting PagedList config

        PagedList.Config pagedListConfig =
                (new PagedList.Config.Builder())
                        .setEnablePlaceholders(false)
                        .setPageSize(25)
                        .build();

        //Building the paged list
        itemPagedList=(new LivePagedListBuilder(itemDataSourceFactory,pagedListConfig)).build();
              }

    }
    public void setQueryAndSortBy(String query,String sortBy){
        itemDataSourceFactory.setOrderBy(sortBy);
        itemDataSourceFactory.setQuery(query);
        if (itemDataSourceFactory.itemDataSource!=null)
        itemDataSourceFactory.itemDataSource.invalidate();
    }
    public void searchingForSuggition(SearchFragment searchFragment) {
        disposable.add(Observable.create(emitter -> searchFragment.binding.etSearchViewSearchScreen.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                emitter.onNext(s);
                if(s.toString().trim().length()==0){
                   searchFragment. binding.sortBySearchScreen.setEnabled(false);
                    searchFragment. binding.sortBySearchScreen.setTextColor(searchFragment.getActivity().getResources().getColor(R.color.DisAbleColor));
                } else {
                    searchFragment. binding.sortBySearchScreen.setEnabled(true);
                    searchFragment. binding.sortBySearchScreen.setTextColor(searchFragment.getActivity().getResources().getColor(R.color.HeaderTextColor));

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        }))
                .doOnNext(c-> {

                })

                .debounce(2, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(d-> {

                    if (!d.toString().equals("")&&!d.toString().equals(queryText)){
                        queryText=d.toString();
                        searchFragment.binding.spinKitSearchScreen.setVisibility(View.VISIBLE);
                        searchFragment.binding.SearchForSomeThingeImage.setVisibility(View.INVISIBLE);
                        SearchForSomeThingImage=false;
                        searchFragment.binding.noThingFoundImage.setVisibility(View.INVISIBLE);
                        searchFragment.mViewModel.nothigeFoundImge=false;
                        GetSearchResult(searchFragment);
                        setQueryAndSortBy(queryText,sortByText);
                        if (!observe)
                           searchFragment. observeItemPagedList();
                    }

                }));
    }

}