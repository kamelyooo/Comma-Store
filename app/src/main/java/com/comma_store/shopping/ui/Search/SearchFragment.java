package com.comma_store.shopping.ui.Search;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.paging.PagedList;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.comma_store.shopping.R;
import com.comma_store.shopping.databinding.SearchFragmentBinding;
import com.comma_store.shopping.pojo.ItemModel;
import com.crowdfire.cfalertdialog.CFAlertDialog;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;

public class SearchFragment extends Fragment {
    int sortBySelected=0;
    FlexboxLayoutManager layoutManager;
    ItemAdapterSearchScreen adapter;
  SearchViewModel mViewModel;
    SearchFragmentBinding binding;
Button TryAgain;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(SearchViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.search_fragment, container, false);
        View root = binding.getRoot();
        TryAgain=root.findViewById(R.id.Error_Conection_Retry_Btn);
        visiblty(mViewModel.recycleVisible,mViewModel.SearchForSomeThingImage
        ,mViewModel.nothigeFoundImge,mViewModel.errorScreen,mViewModel.SearchScreen);
        binding.popUpSearchScreen.setOnClickListener(v -> Navigation.findNavController(v).navigateUp());
        binding.sortBySearchScreen.setOnClickListener(v -> {
            showSortByDialog();
        });
        mViewModel.searchingForSuggition(this);

        adapter = new ItemAdapterSearchScreen(getActivity());
        layoutManager = new FlexboxLayoutManager(getActivity());
        layoutManager.setJustifyContent(JustifyContent.SPACE_EVENLY);
        binding.recycleViewSearchScreen.setLayoutManager(layoutManager);
        binding.recycleViewSearchScreen.setAdapter(adapter);
        binding.etSearchViewSearchScreen.requestFocus();
        if (mViewModel.itemPagedList!=null)
            observeItemPagedList();

        TryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.noThingFoundImage.setVisibility(View.INVISIBLE);
                mViewModel.nothigeFoundImge=false;
                binding.errorScreenSearchScreen.setVisibility(View.INVISIBLE);
                mViewModel.errorScreen=false;
                binding.SearchScreen.setVisibility(View.VISIBLE);
                mViewModel.SearchScreen=true;
                mViewModel.setQueryAndSortBy(mViewModel.queryText,mViewModel.sortByText);
            }
        });


        return root;
    }

    public void observeItemPagedList() {
        mViewModel.itemPagedList.observe(getViewLifecycleOwner(),(Observer<PagedList<ItemModel>>) itemModels -> {
            adapter.submitList(itemModels);
        });
        mViewModel.observe=true;
    }
    private void showSortByDialog() {
        CFAlertDialog.Builder builder = new CFAlertDialog.Builder(getActivity());
        builder.setDialogStyle(CFAlertDialog.CFAlertStyle.ALERT);
        builder.setTextGravity(Gravity.RELATIVE_HORIZONTAL_GRAVITY_MASK);
        builder.setTitle(R.string.Select_Sort_Option);
        builder.setSingleChoiceItems(getActivity().getResources().getStringArray(R.array.DialgoArray), sortBySelected, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int index) {
                sortBySelected=index;
                switch (index){
                    case 0:
                        mViewModel.sortByText="idDesc";
                        mViewModel.setQueryAndSortBy(mViewModel.queryText,"idDesc");
                        dialogInterface.dismiss();
                        break;
                    case 1:
                        mViewModel.sortByText="idAsc";
                        mViewModel.setQueryAndSortBy(mViewModel.queryText,"idAsc");
                        dialogInterface.dismiss();

                        break;
                    case 2:
                        mViewModel.sortByText="priceDesc";
                        mViewModel.setQueryAndSortBy(mViewModel.queryText,"priceDesc");
                        dialogInterface.dismiss();

                        break;
                    case 3:
                        mViewModel.sortByText="priceAsc";
                        mViewModel.setQueryAndSortBy(mViewModel.queryText,"priceAsc");
                        dialogInterface.dismiss();
                        break;
                }
            }
        });
        builder.setIcon(R.drawable.ic_notification).setCornerRadius(20);
        builder.show();
    }
    public void visiblty(boolean Recycle, boolean SearchForImage,boolean nothigeFoundImge,boolean errorScreen
    ,boolean SearchScreen){
        if (Recycle)binding.recycleViewSearchScreen.setVisibility(View.VISIBLE);
        else binding.recycleViewSearchScreen.setVisibility(View.INVISIBLE);
        if (SearchForImage)binding.SearchForSomeThingeImage.setVisibility(View.VISIBLE);
        else binding.SearchForSomeThingeImage.setVisibility(View.INVISIBLE);
        if (nothigeFoundImge)binding.noThingFoundImage.setVisibility(View.VISIBLE);
        else binding.noThingFoundImage.setVisibility(View.INVISIBLE);
        if (errorScreen)binding.errorScreenSearchScreen.setVisibility(View.VISIBLE);
        else binding.errorScreenSearchScreen.setVisibility(View.INVISIBLE);
        if (SearchScreen)binding.SearchScreen.setVisibility(View.VISIBLE);
        else binding.SearchScreen.setVisibility(View.INVISIBLE);

    }
}