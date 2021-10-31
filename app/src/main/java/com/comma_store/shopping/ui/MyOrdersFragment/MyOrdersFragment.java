package com.comma_store.shopping.ui.MyOrdersFragment;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.ClipData;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.comma_store.shopping.R;
import com.comma_store.shopping.Utils.NetworkUtils;
import com.comma_store.shopping.Utils.SharedPreferencesUtils;
import com.comma_store.shopping.data.ItemClient;
import com.comma_store.shopping.databinding.MyOrdersFragmentBinding;
import com.comma_store.shopping.pojo.GetOrderModelResponse;
import com.comma_store.shopping.pojo.Resource;

import java.util.List;
import java.util.Locale;
import java.util.Objects;

import io.reactivex.Scheduler;
import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MyOrdersFragment extends Fragment implements MyOrdersAdapterOnclick {

    private MyOrdersViewModel mViewModel;
    View root;
    Button tryAgainBtn;
    MyOrdersAdapter adapter;

    public static MyOrdersFragment newInstance() {
        return new MyOrdersFragment();
    }

    MyOrdersFragmentBinding binding;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(MyOrdersViewModel.class);

    }

    private void loadData(){
        //check if network found or not
        if (NetworkUtils.isNetworkConnected(requireContext())) {
            mViewModel.getMyOrders(SharedPreferencesUtils.getInstance(getActivity()).getApiKey());
        } else {
            mViewModel.ScreenState.postValue(3);
        }
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding= DataBindingUtil.inflate(inflater,R.layout.my_orders_fragment, container, false);
        root = binding.getRoot();
        loadData();
        //state 0 loading
        //state 1 Empty
        //state 2 list
        //state 3 error
        binding.PopUpBtnMyOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigateUp();
            }
        });
        mViewModel.ScreenState.observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer state) {
                if (state==0){
                    binding.spinKitMyOrdersScreen.setVisibility(View.VISIBLE);
                    binding.EmptyMyOrderScreen.setVisibility(View.INVISIBLE);
                    binding.MyOrderScreen.setVisibility(View.INVISIBLE);
                    binding.errorScreenMyOrders.setVisibility(View.INVISIBLE);
                }else if (state==1){
                    binding.spinKitMyOrdersScreen.setVisibility(View.INVISIBLE);
                    binding.EmptyMyOrderScreen.setVisibility(View.VISIBLE);
                    binding.MyOrderScreen.setVisibility(View.INVISIBLE);
                    binding.errorScreenMyOrders.setVisibility(View.INVISIBLE);

                }else if (state==2){
                    binding.spinKitMyOrdersScreen.setVisibility(View.INVISIBLE);
                    binding.EmptyMyOrderScreen.setVisibility(View.INVISIBLE);
                    binding.MyOrderScreen.setVisibility(View.VISIBLE);
                    binding.errorScreenMyOrders.setVisibility(View.INVISIBLE);
                    setListAdatper();
                }else {
                    binding.spinKitMyOrdersScreen.setVisibility(View.INVISIBLE);
                    binding.EmptyMyOrderScreen.setVisibility(View.INVISIBLE);
                    binding.MyOrderScreen.setVisibility(View.INVISIBLE);
                    binding.errorScreenMyOrders.setVisibility(View.VISIBLE);
                }
            }
        });
        tryAgainBtn = root.findViewById(R.id.Error_Conection_Retry_Btn);
        tryAgainBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewModel.ScreenState.postValue(0);
                loadData();
            }
        });
        return root;
    }

    private void setListAdatper() {
        adapter = new MyOrdersAdapter(mViewModel.MyOrders,getActivity().getResources().getString(R.string.EGP),
                getResources().getString(R.string.Day) ,getActivity(),this);
        binding.myOrdersRecycleView.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.myOrdersRecycleView.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mViewModel.ScreenState.postValue(0);
    }

    @Override
    public void onClickListener(int postion) {
        MyOrdersFragmentDirections.ActionMyOrdersFragmentToOrderDetailsFragment action=MyOrdersFragmentDirections.actionMyOrdersFragmentToOrderDetailsFragment();
        action.setOrderId(mViewModel.MyOrders.get(postion).getId());
        Navigation.findNavController(requireView()).navigate(action);
    }
}