package com.comma_store.shopping.ui.RegistrationFragment;

import androidx.appcompat.app.AlertDialog;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.comma_store.shopping.R;
import com.comma_store.shopping.Utils.SharedPreferencesUtils;
import com.comma_store.shopping.data.ItemClient;
import com.comma_store.shopping.databinding.RegistrationFragmentBinding;
import com.comma_store.shopping.pojo.RegisterResponse;
import com.crowdfire.cfalertdialog.CFAlertDialog;
import com.schibstedspain.leku.LocationPickerActivity;
import com.schibstedspain.leku.LocationPickerActivityKt;
import com.schibstedspain.leku.tracker.LocationPickerTracker;
import com.schibstedspain.leku.tracker.TrackEvents;


import org.jetbrains.annotations.NotNull;

import java.util.Locale;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static android.provider.MediaStore.Video.VideoColumns.LATITUDE;
import static android.provider.MediaStore.Video.VideoColumns.LONGITUDE;

public class RegistrationFragment extends Fragment {

    private RegistrationViewModel mViewModel;
    RegistrationFragmentBinding binding;
    Intent locationPickerIntent;

    String userName;
    String Email;
    String password;
    String Location;
    String PhoneNumbe;
    double latitude;
    double longitude;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(RegistrationViewModel.class);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.registration_fragment, container, false);
        View root = binding.getRoot();
        String deviceToken = SharedPreferencesUtils.getInstance(getActivity()).getDeviceToken();
        binding.userNameET.addTextChangedListener(watcher);
        binding.PhoneNumberET.addTextChangedListener(watcher);
        binding.passwordET.addTextChangedListener(watcher);
        binding.EmailET.addTextChangedListener(watcher);
        binding.etLocation.addTextChangedListener(watcher);
        binding.signUpButtonRegisterScreen.setEnabled(false);
        binding.etLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewModel.checkLocationOpened(getActivity());
                if (!mViewModel.gps_enabled && !mViewModel.network_enabled) {
                    mViewModel.showLocationSettingDiaog(getActivity());
                } else {
                    setLocationPickerIntent();
                }
            }
        });
        binding.LogInTVRegisterScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_registrationFragment_to_logInFragment);
            }
        });
        binding.signUpButtonRegisterScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.textInputPassword.setError(null);
                binding.textInputPhoneNumber.setError(null);
                binding.textInputEmail.setError(null);
                binding.ErrorMassegeTVRegisterScreen.setVisibility(View.GONE);


                if (
                        !binding.EmailET.getText().toString().contains("@")
                         || !binding.EmailET.getText().toString().contains(".com")
                        || binding.PhoneNumberET.getText().toString().length() < 11
                        || binding.PhoneNumberET.getText().toString().length() > 11
                       || !binding.PhoneNumberET.getText().toString().trim().startsWith("0") ||
                        !binding.PhoneNumberET.getText().toString().trim().startsWith("1", 1) ||
                        binding.passwordET.getText().toString().length() < 6
                ) {
                    if (!binding.EmailET.getText().toString().contains("@") || !binding.EmailET.getText().toString().contains(".com")) {
                        binding.textInputEmail.setError("It does not LookLike an Email It Must Contain '@' and '.com' ");
                    }
                    if (
                            !binding.PhoneNumberET.getText().toString().trim().startsWith("0") ||
                                    binding.PhoneNumberET.getText().toString().length() < 11 ||
                                    binding.PhoneNumberET.getText().toString().length() > 11 ||
                                    !binding.PhoneNumberET.getText().toString().trim().startsWith("1", 1)
                    ) {
                        binding.textInputPhoneNumber.setError("The Phone Number Must Start With '011'/'012'/'010'/'015' and 11 Digits");
                    }
                    if (binding.passwordET.getText().toString().length() < 6) {
                        binding.textInputPassword.setError("The Password Must Be More Than 6 Digits");
                    }
                } else {
                    binding.signUpButtonRegisterScreen.setEnabled(false);
                    binding.signUpTV.setText("Please Wait");
                    binding.spinKitSignUpBtn.setVisibility(View.VISIBLE);
                    mViewModel.Registration(
                            binding.userNameET.getText().toString(),
                            binding.PhoneNumberET.getText().toString(),
                            binding.passwordET.getText().toString(),
                            binding.EmailET.getText().toString(),
                            longitude,
                            latitude,
                            binding.etLocation.getText().toString()
                            ,deviceToken,v,RegistrationFragment.this
                    );

                }
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
            userName = binding.userNameET.getText().toString().trim();
            Email = binding.EmailET.getText().toString().trim();
            password = binding.passwordET.getText().toString().trim();
            Location = binding.etLocation.getText().toString().trim();
            PhoneNumbe = binding.PhoneNumberET.getText().toString().trim();
            binding.signUpButtonRegisterScreen.setEnabled(!userName.isEmpty()
                    && !Email.isEmpty() && !password.isEmpty() && !Location.isEmpty() && !PhoneNumbe.isEmpty());

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
                    latitude = data.getDoubleExtra(LATITUDE, 0.0);
                    longitude = data.getDoubleExtra(LONGITUDE, 0.0);
                    binding.etLocation.setText(data.getStringExtra(LocationPickerActivityKt.LOCATION_ADDRESS));

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