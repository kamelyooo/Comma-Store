package com.comma_store.shopping.ui.CompleteOrder;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.comma_store.shopping.R;
import com.comma_store.shopping.databinding.CompleteOrderFagmentBinding;
import com.comma_store.shopping.pojo.CompleteOrderModel;
import com.comma_store.shopping.pojo.ItemModel;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class CompleteOrderFagment extends Fragment {

    private CompleteOrderFagmentViewModel mViewModel;
    CompleteOrderFagmentBinding binding;
    CompleteOrderModel listsOfItem;
    int a=0;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(CompleteOrderFagmentViewModel.class);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.complete_order_fagment, container, false);
        if (getArguments() != null){
            listsOfItem = CompleteOrderFagmentArgs.fromBundle(getArguments()).getListsOfItem();
        }
        Optional<ItemModel> itemModel;
        for (int i = 0; i < listsOfItem.getCartItemList().size(); i++) {
            int finalI = i;
            itemModel = listsOfItem.getItemModels().parallelStream().filter(x -> x.getId() == listsOfItem.getCartItemList().get(finalI).getId()).findFirst();
            a=a+listsOfItem.getCartItemList().get(i).getQuantity()*itemModel.get().getPriceAfter();
        }

        Integer max = Collections.max(listsOfItem.getItemModels().stream().map(ItemModel::getDuration).collect(Collectors.toList()));
        // get max

        return binding.getRoot();
    }
}