package com.comma_store.shopping.ui.Home;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.comma_store.shopping.Utils.NetworkUtils;
import com.comma_store.shopping.R;
//import com.comma_store.shopping.databinding.FragmentHomeBinding;

import com.comma_store.shopping.Utils.SharedPreferencesUtils;
import com.comma_store.shopping.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    FragmentHomeBinding binding;
    Button RetryAgainBtn;
    String deviceToken;
    boolean deviceTokenSentBoolean;

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
        View root = binding.getRoot();
        RetryAgainBtn = root.findViewById(R.id.Error_Conection_Retry_Btn);
        deviceToken = SharedPreferencesUtils.getInstance(getActivity()).getDeviceToken();
        deviceTokenSentBoolean = SharedPreferencesUtils.getInstance(getActivity()).getDeviceTokenSentBoolean();
        if (deviceToken != null&&!deviceTokenSentBoolean) {
          homeViewModel.AddDeviceTokenGuest(deviceToken, getActivity());

        }
        homeViewModel.getConnect().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean connect) {
                if (connect) {
                    homeViewModel.getOnError().observe(getViewLifecycleOwner(), error -> Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show());
                    homeViewModel.getLiveDatagetHomeResponse().observe(getViewLifecycleOwner(), getHomeResponse -> {
                        binding.recycleParent.setAdapter(new HomeRecycleParentAdapter(getHomeResponse,getActivity()));
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


}