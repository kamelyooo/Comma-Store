package com.comma_store.shopping.ui.SendCodeVerification;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.comma_store.shopping.R;
import com.comma_store.shopping.databinding.SendCodeVerificationFragmentBinding;

public class SendCodeVerificationFragment extends Fragment {

    private SendCodeVerificationViewModel mViewModel;
    SendCodeVerificationFragmentBinding binding;
    String Email;
    int resone;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(SendCodeVerificationViewModel.class);


    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.send_code_verification_fragment, container, false);
        View root = binding.getRoot();
        binding.SendCodeBtn.setEnabled(false);
        if (getArguments()!=null){
            resone = SendCodeVerificationFragmentArgs.fromBundle(getArguments()).getResone();
        }
        if (resone==0){
            binding.HeaderTVSendCodeScreen.setText("Forget PassWord?");
        }else {
            binding.HeaderTVSendCodeScreen.setText("Verify Email");

        }
        mViewModel.IsLoading.observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean IsLoading) {
                if (IsLoading){
                    binding.SendCodeBtn.setEnabled(false);
                    binding.spinKitSendCodeBtn.setVisibility(View.VISIBLE);
                    binding.SendCodeTV.setText("Please Wait");
                }else {
                    binding.SendCodeBtn.setEnabled(true);
                    binding.spinKitSendCodeBtn.setVisibility(View.GONE);
                    binding.SendCodeTV.setText("Send Code");
                }
            }
        });
        binding.SignUpTVSendCodeScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_sendCodeVerificationFragment_to_registrationFragment);
            }
        });
        binding.SendCodeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewModel.IsLoading.postValue(true);
                binding.EmailETSendCodeScreen.setEnabled(false);
                mViewModel.SendCode(binding.EmailETSendCodeScreen.getText().toString(),SendCodeVerificationFragment.this,resone);
            }
        });
        binding.EmailETSendCodeScreen.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.SendCodeBtn.setEnabled(!binding.EmailETSendCodeScreen.getText().toString().trim().isEmpty());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return root;
    }



}