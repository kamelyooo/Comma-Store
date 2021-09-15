package com.comma_store.shopping.ui.Profile;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.comma_store.shopping.LogIn_Registration_Activity;
import com.comma_store.shopping.R;
import com.comma_store.shopping.Utils.SharedPreferencesUtils;
import com.comma_store.shopping.databinding.ProfileFragmentBinding;

public class ProfileFragment extends Fragment {

    private ProfileViewModel mViewModel;
    ProfileFragmentBinding binding;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);


    }

    @Override
    public void onStart() {
        super.onStart();
        mViewModel.IsLogIn.setValue(SharedPreferencesUtils.getInstance(getActivity()).getIsLogin());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.profile_fragment, container, false);
        View root = binding.getRoot();


        mViewModel.IsLogIn.observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isLogin) {
                if (isLogin){
                    binding.ClientNameProfScreen.setText(SharedPreferencesUtils.getInstance(getActivity()).getCustomerName());
                    binding.LoginLayOut.setVisibility(View.VISIBLE);
                    binding.NotLoginLayOut.setVisibility(View.GONE);
                    binding.LogOutBtn.setVisibility(View.VISIBLE);

                }else {
                    binding.LoginLayOut.setVisibility(View.GONE);
                    binding.NotLoginLayOut.setVisibility(View.VISIBLE);
                    binding.LogOutBtn.setVisibility(View.GONE);
                }
            }
        });
        binding.LangBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SharedPreferencesUtils.getInstance(getActivity()).getLangKey()==0){
                    SharedPreferencesUtils.getInstance(getActivity()).setLangKey(1);
                }else if (SharedPreferencesUtils.getInstance(getActivity()).getLangKey()==1){
                    SharedPreferencesUtils.getInstance(getActivity()).setLangKey(0);
                }
               Restart();
            }
        });
        binding.LogOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferencesUtils preferencesUtils = SharedPreferencesUtils.getInstance(getActivity());
                preferencesUtils.setTmpToken(null);
                preferencesUtils.setIsLogin(false);
                preferencesUtils.setCustomerLong(null);
                preferencesUtils.setCustomerLat(null);
                preferencesUtils.setCustomerPhone(null);
                preferencesUtils.setCustomerAddress(null);
                preferencesUtils.setCustomerEmail(null);
                preferencesUtils.setCustomerName(null);
                preferencesUtils.setApiKey(null);
                Restart();
            }
        });
       binding.LogInButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent=new Intent(getActivity(), LogIn_Registration_Activity.class);
               intent.putExtra("FragmentKey",1);
               startActivity(intent);
           }
       });
        binding.RegistrationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), LogIn_Registration_Activity.class);
                intent.putExtra("FragmentKey",0);
                startActivity(intent);
            }
        });
        return root;
    }
    private void Restart (){
        Intent intent = getActivity().getIntent();
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_NO_ANIMATION);
        getActivity().overridePendingTransition(0, 0);
        getActivity().finish();

        getActivity().overridePendingTransition(0, 0);
        startActivity(intent);
    }
}