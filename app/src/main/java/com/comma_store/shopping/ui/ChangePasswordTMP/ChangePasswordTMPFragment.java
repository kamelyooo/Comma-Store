package com.comma_store.shopping.ui.ChangePasswordTMP;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.comma_store.shopping.R;
import com.comma_store.shopping.databinding.ChangePasswordTmpFragmentBinding;

public class ChangePasswordTMPFragment extends Fragment {

    private ChangePasswordTMPViewModel mViewModel;
    CountDownTimer countDownTimer;
    String email;
    String TmpToken;
   ChangePasswordTmpFragmentBinding binding;
    String newPass;
    String ConfirmPass;
    String CodeVerify;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ChangePasswordTMPViewModel.class);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.change_password_tmp_fragment, container, false);
        View root = binding.getRoot();

        if (getArguments() != null){

            email = ChangePasswordTMPFragmentArgs.fromBundle(getArguments()).getEmail();
            TmpToken = ChangePasswordTMPFragmentArgs.fromBundle(getArguments()).getTmpToken();
        }

        binding.SubmitCodeBtnChangePassTMPScreen.setEnabled(false);
        binding.ETCodeChangePassTmpScreen.addTextChangedListener(watcher);
        binding.ConfirmpasswordETChangePassTMPScreen.addTextChangedListener(watcher);
        binding.NewpasswordETChangePassTMPScreen.addTextChangedListener(watcher);
        ContDownTimerSend();
        mViewModel.TmpToken.observe(getViewLifecycleOwner(), tmpToken -> TmpToken =tmpToken);

        mViewModel.isLoading.observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isLoading) {
                if (isLoading){
                    binding.SubmitCodeBtnChangePassTMPScreen.setEnabled(false);
                    binding.spinKitSendCodeBtn.setVisibility(View.VISIBLE);
                    binding.SubmitTV.setText("Please Wait");
                }else {
                    binding.SubmitCodeBtnChangePassTMPScreen.setEnabled(true);
                    binding.spinKitSendCodeBtn.setVisibility(View.INVISIBLE);
                    binding.SubmitTV.setText("Submit");
                }
            }
        });
        binding.SendAgainTbChangePassTMPScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContDownTimerSend();
                mViewModel.SendAgain(email,getActivity());
            }
        });

        binding.SubmitCodeBtnChangePassTMPScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                binding.enterNewPassInput.setError(null);
                binding.confirmPassInput.setError(null);
                if (newPass.equals(ConfirmPass)){
                    mViewModel.isLoading.postValue(true);
                    mViewModel.ChangePasswordTmp(TmpToken,binding.NewpasswordETChangePassTMPScreen.getText().toString(),
                            binding.ETCodeChangePassTmpScreen.getText().toString(),ChangePasswordTMPFragment.this);
                }else if (!newPass.equals(ConfirmPass)){
                    binding.enterNewPassInput.setError("The new PassWord And Confirm PassWord Isn't Same");
                    binding.enterNewPassInput.setErrorIconDrawable(null);
                    binding.confirmPassInput.setErrorIconDrawable(null);
                    binding.confirmPassInput.setError("The new PassWord And Confirm PassWord Isn't Same");
                }
            }
        });
        return root;
    }
    private TextWatcher watcher=new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            newPass = binding.NewpasswordETChangePassTMPScreen.getText().toString().trim();

            ConfirmPass = binding.ConfirmpasswordETChangePassTMPScreen.getText().toString().trim();
            CodeVerify = binding.ETCodeChangePassTmpScreen.getText().toString().trim();

            binding.SubmitCodeBtnChangePassTMPScreen.setEnabled(!newPass.isEmpty()&&!ConfirmPass.isEmpty()&& !(CodeVerify.length() <4));
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
    private void ContDownTimerSend(){
        binding.SendAgainTbChangePassTMPScreen.setEnabled(false);
        countDownTimer = new CountDownTimer(90000, 1000) {

            public void onTick(long millisUntilFinished) {
                binding.SendAgainTbChangePassTMPScreen.setText( millisUntilFinished / 1000+"S" );
                //here you can have your logic to set text to edittext
            }

            public void onFinish() {
                binding.SendAgainTbChangePassTMPScreen.setText("Send Again");
                binding.SendAgainTbChangePassTMPScreen.setEnabled(true);
            }

        };
        countDownTimer.start();
    }
}