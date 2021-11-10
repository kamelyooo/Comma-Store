package com.comma_store.shopping.ui.ItemDetails;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.comma_store.shopping.Utils.AnimationUtils;
import com.comma_store.shopping.R;
import com.comma_store.shopping.data.CartDataBase;
import com.comma_store.shopping.databinding.ItemDetailsFragmentBinding;
import com.comma_store.shopping.pojo.CartItem;
import com.comma_store.shopping.pojo.ItemModel;
import com.google.android.material.button.MaterialButton;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class ItemDetailsFragment extends Fragment {

    private ItemDetailsViewModel mViewModel;
    ItemDetailsFragmentBinding binding;
    ItemDetailsSliderAdapter adapter;
    CartItem cartItem;
    ItemModel itemDetail;
    int Quantity = 1;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ItemDetailsViewModel.class);
        if (getArguments() != null) {
            if (ItemDetailsFragmentArgs.fromBundle(getArguments()).getItemDetails() != null) {
                itemDetail = ItemDetailsFragmentArgs.fromBundle(getArguments()).getItemDetails();
            }
        }

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.item_details_fragment, container, false);
        View root = binding.getRoot();
        setview(itemDetail);
        handleCartItem(itemDetail);
        binding.popUpItemDetails.setOnClickListener(v -> Navigation.findNavController(v).navigateUp());
        return root;

    }

    public void handleCartItem(ItemModel itemDetails) {
        mViewModel.getItemFromDataBase(itemDetails.getId(),getActivity());
        mViewModel.cartItem.observe(getViewLifecycleOwner(), new Observer<CartItem>() {
            @Override
            public void onChanged(CartItem item) {
              if (item!=null) {
                  cartItem = item;
//                  binding.AddToCartButton.setVisibility(View.INVISIBLE);
//                  binding.PlusMinusLayout.setVisibility(View.VISIBLE);
                  AnimationUtils.slideDown(binding.AddToCartButton);
                  AnimationUtils.slideUp(binding.PlusMinusLayout);
                  Quantity = item.getQuantity();
                  binding.QuantityTV.setText(Quantity + "");
              }else {
                  binding.AddToCartButton.setVisibility(View.VISIBLE);
                  binding.PlusMinusLayout.setVisibility(View.INVISIBLE);
              }
            }
        });
        binding.AddToCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Quantity = 1;
                binding.QuantityTV.setText(Quantity + "");
                AnimationUtils.slideDown(binding.AddToCartButton);
                AnimationUtils.slideUp(binding.PlusMinusLayout);
                binding.MinusButton.setEnabled(true);
                binding.AddToCartButton.setEnabled(false);
                cartItem = new CartItem();
                cartItem.setItem_id(itemDetails.getId());
                cartItem.setQuantity(Quantity);
                if (!itemDetails.getColors().get(0).equals("")) {
                    cartItem.setColor(binding.colorSpinner.getSelectedItem().toString());
                } else cartItem.setColor(null);
                mViewModel.insertItemCart(getActivity(), cartItem);
            }
        });
        binding.PlusButton.setOnClickListener(v -> {
            Quantity++;
            binding.QuantityTV.setText(Quantity + "");
            cartItem.setQuantity(Quantity);
            mViewModel.updateItemCart(getActivity(), cartItem);
        });
        binding.MinusButton.setOnClickListener(v -> {
            if (Quantity == 1) {
                binding.MinusButton.setEnabled(false);
                AnimationUtils.slideDown(binding.PlusMinusLayout);
                AnimationUtils.slideUp(binding.AddToCartButton);
                binding.AddToCartButton.setEnabled(true);
                mViewModel.DeleteItemCart(getActivity(), cartItem);
                mViewModel.cartItem.postValue(null);
            } else if (Quantity > 1) {
                Quantity--;
                binding.QuantityTV.setText(Quantity + "");
                cartItem.setQuantity(Quantity);
                mViewModel.updateItemCart(getActivity(), cartItem);
            }
        });
    }

    public void setview(ItemModel itemDetails) {
        if (itemDetails.getImages() != null && itemDetails != null) {
            adapter = new ItemDetailsSliderAdapter(itemDetails.getImages());
            binding.viewPagerItemDetails.setAdapter(adapter);
        }
        //set the title of the item
        binding.TitleItemDetailsTV.setText(itemDetails.getTitle());
        //set the priceAfter of the item
        binding.PriceAfterItemDetailsTV.setText(itemDetails.getPriceAfter() + getActivity().getResources().getString(R.string.EGP));
        if (itemDetails.getDiscount() == 1) {
            binding.PriceBeforeItemDetailsTV.setText(itemDetails.getPriceBefor() + getActivity().getResources().getString(R.string.EGP));
            binding.DesctountPrecentageItemDetailsTV.setText(itemDetails.getDiscountPrecentage() + getActivity().getResources().getString(R.string.off));
        } else binding.DescountLayOutItemDetails.setVisibility(View.GONE);

        if (itemDetails.getDuration() != 0) {
            Date c = Calendar.getInstance().getTime();
            SimpleDateFormat df = new SimpleDateFormat("dd-MMM", Locale.getDefault());
            String formattedDate = df.format(c);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(c);
            calendar.add(Calendar.DAY_OF_YEAR, itemDetails.getDuration());
            String format = df.format(calendar.getTime());
            binding.DurationDaysItemDetialsTV.setText(formattedDate + "--" + format);
        } else binding.DurationLayOutItemDetailsTV.setVisibility(View.GONE);

        //set the colors in the spinner color
        if (itemDetails.getColors().get(0).equals("") || itemDetails.getColors().isEmpty() || itemDetails.getColors().size() == 0) {
            binding.ItemColorItemDetailsTV.setVisibility(View.GONE);

        } else {
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, itemDetails.getColors());
            dataAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
            binding.colorSpinner.setAdapter(dataAdapter);
        }

        binding.DescriptionItemDetialsTV.setText(itemDetails.getDiscerption());

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mViewModel.cartItem.postValue(null);
//        binding.unbind();

    }
}