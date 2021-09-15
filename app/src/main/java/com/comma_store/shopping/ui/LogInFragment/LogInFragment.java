package com.comma_store.shopping.ui.LogInFragment;

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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.comma_store.shopping.R;
import com.comma_store.shopping.Utils.SharedPreferencesUtils;
import com.comma_store.shopping.databinding.LogInFragmentBinding;

public class LogInFragment extends Fragment {

    private LogInViewModel mViewModel;
    String password;
    String Email;
    LogInFragmentBinding binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(LogInViewModel.class);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,

                             @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.log_in_fragment, container, false);
        View root = binding.getRoot();
        binding.signinButtonLogInScreen.setEnabled(false);
        binding.EmailETLogINScreen.addTextChangedListener(watcher);
        binding.passwordETLogINScreen.addTextChangedListener(watcher);
        binding.signUpTVLogInScreen.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_logInFragment_to_registrationFragment));
        binding.ForgetPasswordTV.setOnClickListener(v -> {
            LogInFragmentDirections.ActionLogInFragmentToSendCodeVerificationFragment action=
                    LogInFragmentDirections.actionLogInFragmentToSendCodeVerificationFragment();
            action.setResone(0);
            Navigation.findNavController(v).navigate(action);
        });
        mViewModel.IsLoading.observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isLoading) {
                if (isLoading){
                    binding.spinKitSendCodeBtn.setVisibility(View.VISIBLE);
                    binding.signInTV.setText("Please Wait");
                    binding.signinButtonLogInScreen.setEnabled(false);
                }else {
                    binding.spinKitSendCodeBtn.setVisibility(View.INVISIBLE);
                    binding.signInTV.setText("Sign In");
                    binding.signinButtonLogInScreen.setEnabled(true);
                }
            }
        });
        binding.signinButtonLogInScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             mViewModel.IsLoading.postValue(true);
                mViewModel.login(LogInFragment.this,binding.EmailETLogINScreen.getText().toString(),
                        binding.passwordETLogINScreen.getText().toString(), SharedPreferencesUtils.getInstance(getActivity()).getDeviceToken());
                binding.passwordInputLogInScreen.setError(null);
                binding.textInputEmail.setError(null);

            }
        });
        return root;
    }

    private TextWatcher watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            password = binding.passwordETLogINScreen.getText().toString().trim();
            Email = binding.EmailETLogINScreen.getText().toString().trim();
            binding.signinButtonLogInScreen.setEnabled(!password.isEmpty()&& !Email.isEmpty());

        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };


}