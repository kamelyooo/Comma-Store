package com.comma_store.shopping.ui.MyProfileFragment;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.comma_store.shopping.R;
import com.comma_store.shopping.Utils.SharedPreferencesUtils;
import com.comma_store.shopping.databinding.MyProfileFragmentBinding;
import com.schibstedspain.leku.LocationPickerActivity;
import com.schibstedspain.leku.LocationPickerActivityKt;

import static android.provider.MediaStore.Video.VideoColumns.LATITUDE;
import static android.provider.MediaStore.Video.VideoColumns.LONGITUDE;

public class MyProfileFragment extends Fragment {

    private MyProfileViewModel mViewModel;
    View root;
    MyProfileFragmentBinding binding;
    String NewPassword;
    String CurrentPassword;
    String UserName,address;
    double lat,longg;
    Intent locationPickerIntent;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(MyProfileViewModel.class);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.my_profile_fragment, container, false);
        root = binding.getRoot();
        binding.saveButtonMyProfileScreen.setEnabled(false);
        binding.ChangeButtonMyProfileScreen.setEnabled(false);
        binding.userNameETMyProfle.addTextChangedListener(userNameWatcher);
        binding.newPasswordETMyProfile.addTextChangedListener(ChangePasswordWatcher);
        binding.CurrentPasswordETMyProfile.addTextChangedListener(ChangePasswordWatcher);
       lat= Double.parseDouble(SharedPreferencesUtils.getInstance(getActivity()).getCustomerLat()) ;
       longg= Double.parseDouble(SharedPreferencesUtils.getInstance(getActivity()).getCustomerLong()) ;
       binding.userNameETMyProfle.setText(SharedPreferencesUtils.getInstance(getActivity()).getCustomerName());
       binding.etLocationMyProfile.setText(SharedPreferencesUtils.getInstance(getActivity()).getCustomerAddress());
       binding.EmailETMyProfile.setText(SharedPreferencesUtils.getInstance(getActivity()).getCustomerEmail());
        mViewModel.isChangBtnLoading.observe(getViewLifecycleOwner(), isChangBtnLoading -> {
            if (isChangBtnLoading){
                binding.ChangeButtonMyProfileScreen.setEnabled(false);
                binding.ChangeTV.setText("Please Wait");
                binding.spinKitChangeBtn.setVisibility(View.VISIBLE);
            }else {
                binding.ChangeButtonMyProfileScreen.setEnabled(true);
                binding.ChangeTV.setText("Change");
                binding.spinKitChangeBtn.setVisibility(View.INVISIBLE);
            }
        });

        mViewModel.isSaveBtnLoading.observe(getViewLifecycleOwner(), isSaveBtnLoading -> {
            if (isSaveBtnLoading){
                binding.saveButtonMyProfileScreen.setEnabled(false);
                binding.saveTV.setText("Please Wait");
                binding.spinKitSaveUpBtn.setVisibility(View.VISIBLE);
            }else {
                binding.saveButtonMyProfileScreen.setEnabled(true);
                binding.saveTV.setText("Save");
                binding.spinKitSaveUpBtn.setVisibility(View.INVISIBLE);
            }
        });
        mViewModel.msg.observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
            }
        });
        binding.saveButtonMyProfileScreen.setOnClickListener(v -> {
            mViewModel.isSaveBtnLoading.postValue(true);
            mViewModel.UpdateProfile(binding.userNameETMyProfle.getText().toString(), SharedPreferencesUtils.getInstance(getActivity()).getApiKey(),lat,longg,binding.etLocationMyProfile.getText().toString());
        });

        binding.etLocationMyProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewModel.checkLocationOpened();
                if (!mViewModel.gps_enabled && !mViewModel.network_enabled) {
                    mViewModel.showLocationSettingDiaog(getActivity());
                } else {
                    setLocationPickerIntent();
                }
            }
        });
        binding.ChangeButtonMyProfileScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewModel.isChangBtnLoading.postValue(true);
                mViewModel.ChangePassWord(SharedPreferencesUtils.getInstance(getActivity()).getApiKey(),binding.CurrentPasswordETMyProfile.getText().toString(),
                        binding.newPasswordETMyProfile.getText().toString());
            }
        });

        return root;
    }
    private TextWatcher userNameWatcher=new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            UserName = binding.userNameETMyProfle.getText().toString().trim();
            binding.saveButtonMyProfileScreen.setEnabled(!UserName.isEmpty());
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
    private TextWatcher ChangePasswordWatcher=new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            CurrentPassword = binding.CurrentPasswordETMyProfile.getText().toString().trim();
            NewPassword = binding.newPasswordETMyProfile.getText().toString().trim();
            binding.ChangeButtonMyProfileScreen.setEnabled(!CurrentPassword.isEmpty()&&CurrentPassword.length()>=6&&!NewPassword.isEmpty()&&NewPassword.length()>=6);
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    private void setLocationPickerIntent() {
        locationPickerIntent = new LocationPickerActivity.Builder()
                .withGeolocApiKey("AIzaSyDksR9M0SUkLZIlzLZjVkGuDxVsd7NRTDc")
                .withGooglePlacesApiKey("AIzaSyBAvumGK60xeoV_XOPnzXBIhM-AymfECRM")
                .withDefaultLocaleSearchZone()
                .withGoogleTimeZoneEnabled()
                .withUnnamedRoadHidden()
                .withSearchZone("ar")
                .build(getActivity());
        startActivityForResult(locationPickerIntent, 1);

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && data != null) {
            Log.i("xxx", "OK");
            try {
                if (requestCode == 1) {
                    lat = data.getDoubleExtra(LATITUDE, 0.0);
                    longg = data.getDoubleExtra(LONGITUDE, 0.0);
                    binding.etLocationMyProfile.setText(data.getStringExtra(LocationPickerActivityKt.LOCATION_ADDRESS));
                }
            } catch (Exception ex) {
                Log.i("xxxEX", ex.toString());
            }

        }
        if (resultCode == Activity.RESULT_CANCELED) {
            Log.i("xxx", "CANCELLED");
        }
    }

}