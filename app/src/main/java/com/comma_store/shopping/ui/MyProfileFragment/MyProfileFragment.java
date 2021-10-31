package com.comma_store.shopping.ui.MyProfileFragment;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.comma_store.shopping.R;
import com.comma_store.shopping.Utils.SharedPreferencesUtils;
import com.comma_store.shopping.databinding.MyProfileFragmentBinding;

public class MyProfileFragment extends Fragment {

    private MyProfileViewModel mViewModel;
    View root;
    MyProfileFragmentBinding binding;
    String NewPassword;
    String CurrentPassword;
    String UserName;

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

        mViewModel.isChangBtnLoading.observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isChangBtnLoading) {
                if (isChangBtnLoading){
                    binding.ChangeButtonMyProfileScreen.setEnabled(false);
                    binding.ChangeTV.setText("Please Wait");
                    binding.spinKitChangeBtn.setVisibility(View.VISIBLE);
                }else {
                    binding.ChangeButtonMyProfileScreen.setEnabled(true);
                    binding.ChangeTV.setText("Change");
                    binding.spinKitChangeBtn.setVisibility(View.INVISIBLE);
                }
            }
        });

        mViewModel.isSaveBtnLoading.observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isSaveBtnLoading) {
                if (isSaveBtnLoading){
                    binding.saveButtonMyProfileScreen.setEnabled(false);
                    binding.saveTV.setText("Please Wait");
                    binding.spinKitSaveUpBtn.setVisibility(View.VISIBLE);
                }else {
                    binding.saveButtonMyProfileScreen.setEnabled(true);
                    binding.saveTV.setText("Save");
                    binding.spinKitSaveUpBtn.setVisibility(View.INVISIBLE);
                }
            }
        });
        binding.saveButtonMyProfileScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewModel.isSaveBtnLoading.postValue(true);
                mViewModel.UpdateProfile(binding.userNameETMyProfle.getText().toString(), SharedPreferencesUtils.getInstance(getActivity()).getApiKey(),0.32166546,1.65464,"matarya");
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

}