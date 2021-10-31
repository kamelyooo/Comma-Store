package com.comma_store.shopping.ui.OrderDetailsFragment;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

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
import android.widget.Toast;

import com.comma_store.shopping.R;
import com.comma_store.shopping.Utils.NetworkUtils;
import com.comma_store.shopping.Utils.SharedPreferencesUtils;
import com.comma_store.shopping.databinding.OrderDetailsFragmentBinding;
import com.comma_store.shopping.pojo.GetOrderModelResponse;

import java.util.Objects;

public class OrderDetailsFragment extends Fragment {
    OrderDetailsFragmentBinding binding;
    private OrderDetailsViewModel mViewModel;
    int orderId;
    Button tryAgainBtn;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(OrderDetailsViewModel.class);

    }
    private void loadData(int id){
        if (NetworkUtils.isNetworkConnected(requireActivity())){
            mViewModel.GetOrderDetails(id, SharedPreferencesUtils.getInstance(getActivity()).getApiKey());
        }else {
            mViewModel.ScreenState.postValue(2);
        }
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,R.layout.order_details_fragment,container,false);
        View root = binding.getRoot();
        if (getArguments() != null){
            orderId = OrderDetailsFragmentArgs.fromBundle(getArguments()).getOrderId();
            if (mViewModel.ScreenState.getValue()!=1){
                loadData(orderId);
            }
        }

        mViewModel.ScreenState.observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer screenState) {
                //screen State
                //0 loading
                //1 connect
                //2 error
                if (screenState==0){
                    binding.ErrorConnectionOrderDetails.setVisibility(View.INVISIBLE);
                    binding.spinKitOrdersDetailsScreen.setVisibility(View.VISIBLE);
                    binding.MyorderDetailsScreen.setVisibility(View.INVISIBLE);
                }else if (screenState==1){
                    binding.ErrorConnectionOrderDetails.setVisibility(View.INVISIBLE);
                    binding.spinKitOrdersDetailsScreen.setVisibility(View.INVISIBLE);
                    binding.MyorderDetailsScreen.setVisibility(View.VISIBLE);
                }else if (screenState==2){
                    binding.ErrorConnectionOrderDetails.setVisibility(View.VISIBLE);
                    binding.spinKitOrdersDetailsScreen.setVisibility(View.INVISIBLE);
                    binding.MyorderDetailsScreen.setVisibility(View.INVISIBLE);
                }
            }
        });
        mViewModel.orderDetails.observe(getViewLifecycleOwner(), new Observer<GetOrderModelResponse>() {
            @Override
            public void onChanged(GetOrderModelResponse getOrderModelResponse) {
                mViewModel.ScreenState.postValue(1);
                setScreenViews(getOrderModelResponse);
            }
        });
        tryAgainBtn = root.findViewById(R.id.Error_Conection_Retry_Btn);
        tryAgainBtn.setOnClickListener(v -> {
            loadData(orderId);
            mViewModel.ScreenState.postValue(0);
        });
        binding.PopUpBtnOrderDetailsDetails.setOnClickListener(v -> Navigation.findNavController(v).navigateUp());
        return root;
    }

    private void setScreenViews(GetOrderModelResponse getOrderModelResponse) {
        String Egp = requireActivity().getResources().getString(R.string.EGP);
        String day = requireActivity().getResources().getString(R.string.Day);
        binding.orderDetailsNumberTV.setText(getOrderModelResponse.getCode());
        binding.orderDetailsStateTV.setText(getOrderModelResponse.getStatus_name());
        binding.orderDetailsCostTV.setText(getOrderModelResponse.getCost()+Egp);
        binding.orderDetailsDiscontAmountTV.setText(getOrderModelResponse.getDiscount()+Egp);
        binding.orderDetailsDeliveryCostTV.setText(getOrderModelResponse.getDriver_cost()+Egp);
        binding.orderDetailsTotalCostTV.setText(getOrderModelResponse.getTotal()+Egp);
        binding.orderDetailsDurationTV.setText(getOrderModelResponse.getDuration()+day);
        binding.orderDetailsAddressTV.setText(getOrderModelResponse.getAddress());
        binding.OrderDetailsDetailsRecycle.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.OrderDetailsDetailsRecycle.setAdapter(new OrderDetailsAdapter(getOrderModelResponse.getOrderDetails(),getActivity()));

    }

}