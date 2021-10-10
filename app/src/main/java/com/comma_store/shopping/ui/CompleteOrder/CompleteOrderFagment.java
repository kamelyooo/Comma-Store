package com.comma_store.shopping.ui.CompleteOrder;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.comma_store.shopping.LogIn_Registration_Activity;
import com.comma_store.shopping.R;
import com.comma_store.shopping.Utils.NetworkUtils;
import com.comma_store.shopping.Utils.SharedPreferencesUtils;
import com.comma_store.shopping.data.ItemClient;
import com.comma_store.shopping.databinding.CompleteOrderFagmentBinding;
import com.comma_store.shopping.pojo.CompleteOrderModel;
import com.comma_store.shopping.pojo.ItemModel;
import com.comma_store.shopping.pojo.Resource;
import com.comma_store.shopping.pojo.SettingResponse;
import com.crowdfire.cfalertdialog.CFAlertDialog;
import com.schibstedspain.leku.LocationPickerActivity;
import com.schibstedspain.leku.LocationPickerActivityKt;

import java.net.Inet4Address;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static android.provider.MediaStore.Video.VideoColumns.LATITUDE;
import static android.provider.MediaStore.Video.VideoColumns.LONGITUDE;

public class CompleteOrderFagment extends Fragment {

    private CompleteOrderFagmentViewModel mViewModel;
    CompleteOrderFagmentBinding binding;
    CompleteOrderModel listsOfItem;
    int SubTotalCost = 0;
    int deliverCost = 0;
    int promoCodeCut = 0;
    String adress = "";
    String promoCode;


    Button tryAgainErrorScreen;

    Intent locationPickerIntent;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(CompleteOrderFagmentViewModel.class);
        loadData();
    }

    private void loadData() {
        //check if network found or not
        if (NetworkUtils.isNetworkConnected(requireContext())) {
            mViewModel.getSettings();
        } else {
            mViewModel.isConnected.postValue(false);
        }
        adress = SharedPreferencesUtils.getInstance(getActivity()).getCustomerAddress();
        mViewModel.spinKitIsShow = true;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.complete_order_fagment, container, false);
        View root = binding.getRoot();
        if (getArguments() != null) {
            listsOfItem = CompleteOrderFagmentArgs.fromBundle(getArguments()).getListsOfItem();
        }
        showSpinKit();
        tryAgainErrorScreen = root.findViewById(R.id.Error_Conection_Retry_Btn);
        mViewModel.isConnected.observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isConnected) {
                if (isConnected) {
                    binding.spinKitCompleteOrderScreen.setVisibility(View.INVISIBLE);
                    binding.CompleteOrderScreen.setVisibility(View.VISIBLE);
                    binding.ErrorScreenCompleteOrderScreen.setVisibility(View.INVISIBLE);
                    mViewModel.spinKitIsShow = false;
                    setDuration();
                    setDeliverCost(adress);

                } else {
                    binding.spinKitCompleteOrderScreen.setVisibility(View.INVISIBLE);
                    binding.CompleteOrderScreen.setVisibility(View.INVISIBLE);
                    binding.ErrorScreenCompleteOrderScreen.setVisibility(View.VISIBLE);
                    mViewModel.spinKitIsShow = false;
                }

            }
        });
        binding.promoCodeEditText.addTextChangedListener(watcher);
        binding.applyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewModel.isPromoCodeLoading.postValue(true);
                mViewModel.checkPromoCode(SharedPreferencesUtils.getInstance(getActivity()).getApiKey(), binding.promoCodeEditText.getText().toString());
            }
        });
        binding.locationTV.setText(adress);
        binding.phoneNumberTV.setText(SharedPreferencesUtils.getInstance(getActivity()).getCustomerPhone());

        if (SubTotalCost == 0) {
            getSubTotalCost(listsOfItem);
        }

        binding.SubTotalTv.setText(SubTotalCost + "");
        binding.promoCodeCutTv.setText("-" + promoCodeCut);
        binding.ChangeLocationBtn.setOnClickListener(new View.OnClickListener() {
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
        mViewModel.isPromoCodeLoading.observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isPromoCodeLoading) {
                if (isPromoCodeLoading) {
                    binding.applyBtn.setVisibility(View.INVISIBLE);
                    binding.spinKitPromoCodeBtn.setVisibility(View.VISIBLE);
                    binding.promoCodeEditText.setEnabled(false);
                } else {
                    binding.applyBtn.setVisibility(View.VISIBLE);
                    binding.spinKitPromoCodeBtn.setVisibility(View.INVISIBLE);
                    binding.promoCodeEditText.setEnabled(true);
                }
            }
        });
        mViewModel.ErrorMsg.observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String msg) {
                if (msg != null) {
                    binding.ErrorMassegePromoCodeTv.setVisibility(View.VISIBLE);
                    binding.ErrorMassegePromoCodeTv.setText(msg);
                }


            }
        });
        mViewModel.promoCodeMin_cost.observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer promoCodeMin_cost) {
                if (promoCodeMin_cost != null) {
                    if (SubTotalCost > promoCodeMin_cost) {
                        promoCodeCut = mViewModel.promoCodeCut;
                        binding.promoCodeCutTv.setText("-" + promoCodeCut);
                        setTotalCost(SubTotalCost, deliverCost, promoCodeCut);

                    } else {
                        mViewModel.ErrorMsg.postValue("* The Cost Of The Order Is Lessthan The Minimum Cost Of PromoCode");
                    }
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

            promoCode = binding.promoCodeEditText.getText().toString().trim();

            if (!promoCode.isEmpty() && !promoCode.equals(mViewModel.promoCodeSubmited)) {
                promoCodeCut = 0;
                mViewModel.promoCodeCut = 0;
                binding.promoCodeCutTv.setText("-" + promoCodeCut);
                setTotalCost(SubTotalCost, deliverCost, promoCodeCut);
                binding.ErrorMassegePromoCodeTv.setVisibility(View.GONE);
                mViewModel.ErrorMsg.postValue(null);
                mViewModel.promoCodeMin_cost.postValue(null);

            }
            binding.applyBtn.setEnabled(!promoCode.isEmpty());
            if (!promoCode.isEmpty()) {
                binding.applyBtn.setTextColor(getActivity().getResources().getColor(R.color.HeaderTextColor));
            } else {
                binding.applyBtn.setTextColor(getActivity().getResources().getColor(R.color.DisAbleColor));
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    public void getSubTotalCost(CompleteOrderModel listsOfItem) {
        Optional<ItemModel> itemModel;
        for (int i = 0; i < listsOfItem.getCartItemList().size(); i++) {
            int finalI = i;
            itemModel = listsOfItem.getItemModels().parallelStream().filter(x -> x.getId() == listsOfItem.getCartItemList().get(finalI).getId()).findFirst();
            SubTotalCost = SubTotalCost + listsOfItem.getCartItemList().get(i).getQuantity() * itemModel.get().getPriceAfter();
        }

    }

    private void showSpinKit() {
        if (mViewModel.spinKitIsShow)
            binding.spinKitCompleteOrderScreen.setVisibility(View.VISIBLE);
        else binding.spinKitCompleteOrderScreen.setVisibility(View.INVISIBLE);
    }

    private void setDeliverCost(String adress) {
        if (adress.toLowerCase().contains("cairo") ||
                adress.toLowerCase().contains("giza")) {
            deliverCost = Integer.parseInt(mViewModel.DataResponse.get(5).getValue());
        } else {
            deliverCost = Integer.parseInt(mViewModel.DataResponse.get(6).getValue());
        }
        binding.deliverCostTv.setText(deliverCost + "");
        setTotalCost(SubTotalCost, deliverCost, promoCodeCut);
    }

    private void setDuration() {

        Integer max = Collections.max(listsOfItem.getItemModels().stream().map(ItemModel::getDuration).collect(Collectors.toList()));
        if (mViewModel.DataResponse.get(4).getValue() != null) {
            getTheDateOfDelivery(max > Integer.parseInt(mViewModel.DataResponse.get(4).getValue()) ? max : Integer.parseInt(mViewModel.DataResponse.get(4).getValue()));
        } else {
            getTheDateOfDelivery(max);

        }


    }

    private void getTheDateOfDelivery(int days) {
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM", Locale.getDefault());
        String formattedDate = df.format(c);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(c);
        calendar.add(Calendar.DAY_OF_YEAR, (days));
        String format = df.format(calendar.getTime());
        binding.DurationTv.setText(formattedDate + "--" + format);
    }

    private void setTotalCost(int subTotalCost, int deliverCost, int promoCodeCut) {
        binding.TotalCostTv.setText((subTotalCost + deliverCost - promoCodeCut) + "");
    }

    private void setLocationPickerIntent() {
        locationPickerIntent = new LocationPickerActivity.Builder()
                .withGeolocApiKey("AIzaSyDksR9M0SUkLZIlzLZjVkGuDxVsd7NRTDc")
                .withGooglePlacesApiKey("AIzaSyBAvumGK60xeoV_XOPnzXBIhM-AymfECRM")
                .withDefaultLocaleSearchZone()
                .withGoogleTimeZoneEnabled()
                .withUnnamedRoadHidden()
                .withSearchZone("ar")
                .build(getActivity());
        startActivityForResult(locationPickerIntent, 2);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && data != null) {
            Log.i("xxx", "OK");
            try {
                if (requestCode == 2) {
                    adress = data.getStringExtra(LocationPickerActivityKt.LOCATION_ADDRESS);
                    setDeliverCost(data.getStringExtra(LocationPickerActivityKt.LOCATION_ADDRESS));

                    binding.locationTV.setText(adress);
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