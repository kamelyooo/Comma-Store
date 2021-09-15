package com.comma_store.shopping.ui.Cart;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.comma_store.shopping.LogIn_Registration_Activity;
import com.comma_store.shopping.Utils.NetworkUtils;
import com.comma_store.shopping.R;
import com.comma_store.shopping.Utils.SharedPreferencesUtils;
import com.comma_store.shopping.databinding.CartFragmentBinding;
import com.comma_store.shopping.pojo.CompleteOrderModel;
import com.comma_store.shopping.pojo.ItemModel;

import java.util.List;
import java.util.Optional;

public class CartFragment extends Fragment {

    CartViewModel mViewModel;

    CartFragmentBinding binding;
    CartItemsAdapter adapter;

    Button tryAgain;
    CompleteOrderModel completeOrderModel;
    CartFragmentDirections.ActionCartFragmentToCompleteOrderFagment action;
    boolean vaild,validating;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(CartViewModel.class);

    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.cart_fragment, container, false);
        View root = binding.getRoot();
        tryAgain = root.findViewById(R.id.Error_Conection_Retry_Btn);
//        mViewModel.ScreenState.observe(getViewLifecycleOwner(), new Observer<Integer>() {
//            @Override
//            public void onChanged(Integer integerState) {
//                //state 0 cart Screen
//                //state 1 error Screen
//                //state 2 empty Screen
//                //state 3 loading
//               if (integerState != null){
//                   if (integerState==0){
//                       binding.spinKitCartScreen.setVisibility(View.INVISIBLE);
//                       binding.cartScreen.setVisibility(View.VISIBLE);
//                       binding.ErrorScreenCartScreen.setVisibility(View.INVISIBLE);
//                       binding.EmptyCartScreen.setVisibility(View.INVISIBLE);
//                       binding.shadow.setVisibility(View.INVISIBLE);
//                       binding.CompleteOrderTV.setText("Complete Your Order");
//                       binding.spinKitCompleteYourOrderBtn.setVisibility(View.INVISIBLE);
//                       binding.CompleteOrderCartScreen.setEnabled(true);
//                   }else if (integerState==1){
//                       binding.spinKitCartScreen.setVisibility(View.INVISIBLE);
//                       binding.cartScreen.setVisibility(View.INVISIBLE);
//                       binding.ErrorScreenCartScreen.setVisibility(View.VISIBLE);
//                       binding.EmptyCartScreen.setVisibility(View.INVISIBLE);
//                   }else if (integerState==2){
//                       binding.spinKitCartScreen.setVisibility(View.INVISIBLE);
//                       binding.cartScreen.setVisibility(View.INVISIBLE);
//                       binding.ErrorScreenCartScreen.setVisibility(View.INVISIBLE);
//                       binding.EmptyCartScreen.setVisibility(View.VISIBLE);
//                   }
//               }else {
//                   binding.spinKitCartScreen.setVisibility(View.VISIBLE);
//                   binding.cartScreen.setVisibility(View.INVISIBLE);
//                   binding.ErrorScreenCartScreen.setVisibility(View.INVISIBLE);
//                   binding.EmptyCartScreen.setVisibility(View.INVISIBLE);
//               }
//
//
//            }
//        });

        tryAgain.setOnClickListener(v -> {
            binding.ErrorScreenCartScreen.setVisibility(View.INVISIBLE);
            binding.spinKitCartScreen.setVisibility(View.VISIBLE);
            loadData();
        });
        if (NetworkUtils.isNetworkConnected(requireContext())) {
            loadData();
        } else {
            binding.ErrorScreenCartScreen.setVisibility(View.VISIBLE);
            binding.spinKitCartScreen.setVisibility(View.INVISIBLE);
        }

        binding.shadow.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        binding.CompleteOrderCartScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.CompleteOrderCartScreen.setEnabled(false);
                binding.shadow.setVisibility(View.VISIBLE);
                binding.CompleteOrderTV.setText("Please Wait");
                binding.spinKitCompleteYourOrderBtn.setVisibility(View.VISIBLE);
                binding.spinKitCartScreen.setVisibility(View.VISIBLE);
               mViewModel. validateList(CartFragment.this);
            }
        });
        return root;
    }

    private void loadData() {
        mViewModel.getItemsCartLocal(this);

        mViewModel.cartItemsLiveData.observe(getViewLifecycleOwner(), new Observer<List<ItemModel>>() {
            @Override
            public void onChanged(List<ItemModel> itemModels) {
                if (itemModels != null) {
                    adapter = new CartItemsAdapter(CartFragment.this, mViewModel.cartItemsLocalLiveData, itemModels);
                    binding.CartRecycleView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    binding.CartRecycleView.setAdapter(adapter);
                    adapter.submitList(mViewModel.cartItemsLocalLiveData);
                    mViewModel.cartItemsLiveData.postValue(null);

                }

            }
        });

    }


}