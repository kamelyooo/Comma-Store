package com.comma_store.shopping.ui.CodeVerification;

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
import com.comma_store.shopping.databinding.CodeVerificationFragmentBinding;

import static com.comma_store.shopping.ui.CodeVerification.CodeVerificationFragmentArgs.*;

public class CodeVerificationFragment extends Fragment {

    private CodeVerificationViewModel mViewModel;
    CountDownTimer countDownTimer;
    String email;
    String tmpToken;
    CodeVerificationFragmentBinding binding;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(CodeVerificationViewModel.class);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.code_verification_fragment, container, false);
        View root = binding.getRoot();
        if (getArguments() != null){
            if (fromBundle(getArguments()).getEmail() != null&& fromBundle(getArguments()).getTmpToken()!=null){
                email = fromBundle(getArguments()).getEmail();
                mViewModel.TmpToken.setValue(fromBundle(getArguments()).getTmpToken());
            }
        }
        mViewModel.TmpToken.observe(getViewLifecycleOwner(), TMpToken -> tmpToken=TMpToken);
        binding.VerifyCodeBtn.setEnabled(false);
        ContDownTimer();
        binding.SendAgainTVCodeVScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContDownTimer();
                mViewModel.SendAgain(email,getActivity());
            }
        });
        binding.VerifyCodeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.VerifyCodeBtn.setEnabled(false);
                binding.VerifyTV.setText("Please Wait");
                binding.spinKitVerifyBtn.setVisibility(View.VISIBLE);
                mViewModel.validateCode(tmpToken,binding.CodeVerificationETCodeVScreen.getText().toString(),CodeVerificationFragment.this);
            }
        });
        binding.CodeVerificationETCodeVScreen.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                binding.VerifyCodeBtn.setEnabled(!binding.CodeVerificationETCodeVScreen.getText().toString().trim().isEmpty()&&!(binding.CodeVerificationETCodeVScreen.getText().toString().trim().length()<4));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        return root;
    }
    private void ContDownTimer(){
        binding.SendAgainTVCodeVScreen.setEnabled(false);
        countDownTimer = new CountDownTimer(90000, 1000) {

            public void onTick(long millisUntilFinished) {
                binding.SendAgainTVCodeVScreen.setText( millisUntilFinished / 1000+"S" );
                //here you can have your logic to set text to edittext
            }

            public void onFinish() {
                binding.SendAgainTVCodeVScreen.setText("Send Again");
                binding.SendAgainTVCodeVScreen.setEnabled(true);
            }

        };
        countDownTimer.start();
    }
}