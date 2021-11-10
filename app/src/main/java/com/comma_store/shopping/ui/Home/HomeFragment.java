package com.comma_store.shopping.ui.Home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.comma_store.shopping.Utils.NetworkUtils;
import com.comma_store.shopping.R;
//import com.comma_store.shopping.databinding.FragmentHomeBinding;

import com.comma_store.shopping.Utils.SharedPreferencesUtils;
import com.comma_store.shopping.data.CartDataBase;
import com.comma_store.shopping.databinding.FragmentHomeBinding;
import com.comma_store.shopping.pojo.FavoriteItem;
import com.comma_store.shopping.pojo.ItemModel;

import io.reactivex.schedulers.Schedulers;


public class HomeFragment extends Fragment implements HomeAdapterOnClick {

    private HomeViewModel homeViewModel;
    FragmentHomeBinding binding;
    Button RetryAgainBtn;
    String deviceToken;
    boolean deviceTokenSentBoolean;
    HomeFragmentDirections.ActionHomeFragmentToGetItemsGraph action;
    HomeFragmentDirections.ActionHomeFragmentToOrderDetailsFragment2 actionToOrderDetailsFragment;
    HomeFragmentDirections.ActionHomeFragmentToItemDetailsFragment2 actionToItemDetails;


    View root;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        loadData();
    }

    private void loadData() {
        //check if network found or not
        if (NetworkUtils.isNetworkConnected(requireContext())) {
            homeViewModel.getHome();
            homeViewModel.setConnect(true);
        } else {
            homeViewModel.setConnect(false);
        }
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
        root = binding.getRoot();
        RetryAgainBtn = root.findViewById(R.id.Error_Conection_Retry_Btn);
        deviceToken = SharedPreferencesUtils.getInstance(getActivity()).getDeviceToken();
        deviceTokenSentBoolean = SharedPreferencesUtils.getInstance(getActivity()).getDeviceTokenSentBoolean();
        if (deviceToken != null && !deviceTokenSentBoolean) {
            homeViewModel.AddDeviceTokenGuest(deviceToken);

        }

        if (SharedPreferencesUtils.getInstance(getActivity()).getNotificationNavigation() != null) {
            int notificationId = SharedPreferencesUtils.getInstance(getActivity()).getNotificationId();
            switch (SharedPreferencesUtils.getInstance(getActivity()).getNotificationNavigation()) {
                case "order":
                    actionToOrderDetailsFragment = HomeFragmentDirections.actionHomeFragmentToOrderDetailsFragment2();
                    actionToOrderDetailsFragment.setOrderId(notificationId);
                    Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(actionToOrderDetailsFragment);
                    break;
                case "promoCode":
//                    Toast.makeText(this, "promoCode"+id, Toast.LENGTH_SHORT).show();
                    break;
                case "offers_sub":
                    action = HomeFragmentDirections.actionHomeFragmentToGetItemsGraph();
                    action.setSubCategoryId(notificationId);
                    Navigation.findNavController(getActivity(), R.id.nav_host_fragment).navigate(action);
                    break;
            }

            SharedPreferencesUtils.getInstance(getActivity()).setNotificationId(-1);
            SharedPreferencesUtils.getInstance(getActivity()).setNotificationNavigation(null);
        }
        homeViewModel.getConnect().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean connect) {
                if (connect) {
                    homeViewModel.getOnError().observe(getViewLifecycleOwner(), error -> Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show());
                    homeViewModel.getLiveDatagetHomeResponse().observe(getViewLifecycleOwner(), getHomeResponse -> {
                        binding.recycleParent.setAdapter(new HomeRecycleParentAdapter(getHomeResponse, getActivity(),HomeFragment.this));
                        binding.homeLayout.setVisibility(View.VISIBLE);
                        binding.homeErrorConnection.setVisibility(View.INVISIBLE);
                        binding.spinKit.setVisibility(View.INVISIBLE);
                    });
                } else if (!connect) {
                    binding.homeErrorConnection.setVisibility(View.VISIBLE);
                    binding.homeLayout.setVisibility(View.INVISIBLE);
                    binding.spinKit.setVisibility(View.INVISIBLE);

                }
            }
        });

        binding.etSearchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_homeFragment_to_search_graph);
            }
        });
        RetryAgainBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.homeErrorConnection.setVisibility(View.INVISIBLE);
                binding.homeLayout.setVisibility(View.INVISIBLE);
                binding.spinKit.setVisibility(View.VISIBLE);
                loadData();
            }
        });
        return root;
    }

    @Override
    public void ClickToSubCategoryItems(int CategoryId) {
        action = HomeFragmentDirections.actionHomeFragmentToGetItemsGraph();
        action.setSubCategoryId(CategoryId);
        Navigation.findNavController(getView()).navigate(action);
    }

    @Override
    public void ClickToItemDetails(ItemModel itemModel) {
        actionToItemDetails = HomeFragmentDirections.actionHomeFragmentToItemDetailsFragment2(itemModel);
        actionToItemDetails.setItemDetails(itemModel);
        Navigation.findNavController(getView()).navigate(actionToItemDetails);
    }

    @Override
    public void ClickOnFavorite(int itemId) {
       homeViewModel.insertFavorite(itemId);
    }

    @Override
    public void ClickOnUnFavorite(int itemId) {
     homeViewModel.DeleteFavorite(itemId);
    }
}