package com.comma_store.shopping.ui.Cart;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.comma_store.shopping.LogIn_Registration_Activity;
import com.comma_store.shopping.Utils.NetworkUtils;
import com.comma_store.shopping.R;
import com.comma_store.shopping.Utils.SharedPreferencesUtils;
import com.comma_store.shopping.databinding.CartFragmentBinding;
import com.comma_store.shopping.pojo.CartItem;
import com.comma_store.shopping.pojo.CompleteOrderModel;
import com.comma_store.shopping.pojo.ItemModel;
import com.crowdfire.cfalertdialog.CFAlertDialog;

import java.util.List;
import java.util.Optional;

public class CartFragment extends Fragment implements cartAdapterInterface{

    CartViewModel mViewModel;

    CartFragmentBinding binding;
    CartItemsAdapter adapter;

    Button tryAgain;
    Intent intent;
    CartFragmentDirections.ActionCartFragmentToCompleteOrderFagment action;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(CartViewModel.class);

    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.cart_fragment, container, false);
        View root = binding.getRoot();
        tryAgain = root.findViewById(R.id.Error_Conection_Retry_Btn);
        if (NetworkUtils.isNetworkConnected(requireContext())) {
            loadData();
        } else {
            mViewModel.screenState.postValue(1);
        }
        mViewModel.screenState.observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integerState) {
                //state 0 cart Screen
                //state 1 error Screen
                //state 2 empty Screen
                //state 3 loading
                //state 4 validating
                if (integerState != null) {
                    if (integerState == 0) {
                        binding.spinKitCartScreen.setVisibility(View.INVISIBLE);
                        binding.cartScreen.setVisibility(View.VISIBLE);
                        binding.ErrorScreenCartScreen.setVisibility(View.INVISIBLE);
                        binding.EmptyCartScreen.setVisibility(View.INVISIBLE);
                        binding.shadow.setVisibility(View.INVISIBLE);
                        binding.CompleteOrderTV.setText("Complete Your Order");
                        binding.spinKitCompleteYourOrderBtn.setVisibility(View.INVISIBLE);
                        binding.CompleteOrderCartScreen.setEnabled(true);
                    } else if (integerState == 1) {
                        binding.spinKitCartScreen.setVisibility(View.INVISIBLE);
                        binding.cartScreen.setVisibility(View.INVISIBLE);
                        binding.ErrorScreenCartScreen.setVisibility(View.VISIBLE);
                        binding.EmptyCartScreen.setVisibility(View.INVISIBLE);
                    } else if (integerState == 2) {
                        binding.spinKitCartScreen.setVisibility(View.INVISIBLE);
                        binding.cartScreen.setVisibility(View.INVISIBLE);
                        binding.ErrorScreenCartScreen.setVisibility(View.INVISIBLE);
                        binding.EmptyCartScreen.setVisibility(View.VISIBLE);
                    } else if (integerState == 3) {
                        binding.spinKitCartScreen.setVisibility(View.VISIBLE);
                        binding.cartScreen.setVisibility(View.INVISIBLE);
                        binding.ErrorScreenCartScreen.setVisibility(View.INVISIBLE);
                        binding.EmptyCartScreen.setVisibility(View.INVISIBLE);
                    } else if (integerState == 4) {
                        binding.CompleteOrderCartScreen.setEnabled(false);
                        binding.shadow.setVisibility(View.VISIBLE);
                        binding.CompleteOrderTV.setText("Please Wait");
                        binding.spinKitCompleteYourOrderBtn.setVisibility(View.VISIBLE);
                        binding.spinKitCartScreen.setVisibility(View.VISIBLE);

                    }
                }


            }
        });
        mViewModel.Navigate.observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer navigete) {
                if (navigete != null) {
                    if (navigete == 0) {
                        action = CartFragmentDirections.actionCartFragmentToCompleteOrderFagment(mViewModel.lists);
                        action.setListsOfItem(mViewModel.lists);
                        Navigation.findNavController(getView()).navigate(action);

                    } else if (navigete == 1) {
                        intent = new Intent(getActivity(), LogIn_Registration_Activity.class);
                        intent.putExtra("FragmentKey", 1);
                        getActivity().startActivity(intent);
                    }
                    mViewModel.Navigate.postValue(null);
                }
            }
        });
        tryAgain.setOnClickListener(v -> {
            mViewModel.screenState.postValue(3);
            loadData();
        });


        binding.shadow.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        binding.CompleteOrderCartScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               mViewModel.screenState.postValue(4);
                mViewModel.getItemCartLocal(true);
            }
        });
        return root;
    }

    private void loadData() {
        mViewModel.getItemCartLocal(false);

        mViewModel.cartItemsLiveData.observe(getViewLifecycleOwner(), new Observer<List<ItemModel>>() {
            @Override
            public void onChanged(List<ItemModel> itemModels) {
                if (itemModels != null) {
                    adapter = new CartItemsAdapter( mViewModel.cartItemsLocalLiveData, itemModels,CartFragment.this);
                    binding.CartRecycleView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    binding.CartRecycleView.setAdapter(adapter);
                    adapter.submitList(mViewModel.cartItemsLocalLiveData);
                    mViewModel.cartItemsLiveData.postValue(null);

                }

            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mViewModel.screenState.postValue(3);
    }

    @Override
    public void onPlusClicked(CartItem cartItem) {
        cartItem.setQuantity(cartItem.getQuantity()+1);
        mViewModel.updateItemCart(cartItem);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onMinusClicked(CartItem cartItem) {
        if (cartItem.getQuantity()>1) {
            cartItem.setQuantity(cartItem.getQuantity() - 1);
            mViewModel.updateItemCart(cartItem);
            adapter.notifyDataSetChanged();
        }else if (cartItem.getQuantity()==1){
            Toast.makeText(getActivity(), "Quantity Cant Be Less Than 1", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRemoveClicked(CartItem cartItem,int position) {
        showDeleteDialog(position,cartItem);
    }

    private  void showDeleteDialog( int position, CartItem cartItem){
        new CFAlertDialog.Builder(getActivity())
                .setDialogStyle(CFAlertDialog.CFAlertStyle.ALERT)
                .setTitle("Do You Want To Delete The Item ?")
                .setTextColor( getActivity().getResources().getColor(R.color.HeaderTextColor))
                .addButton("Delete", getActivity().getResources().getColor(R.color.white), getActivity().getResources().getColor(R.color.Buttons), CFAlertDialog.CFAlertActionStyle.POSITIVE, CFAlertDialog.CFAlertActionAlignment.JUSTIFIED, (dialog, which) -> {
                    mViewModel.DeleteItemCart(cartItem);
                    adapter.localItemsCart.remove(position);
                    adapter.notifyItemRemoved(position);
                    if (adapter.localItemsCart.size()==0){
                        mViewModel.screenState.postValue(2);
                    }
                    dialog.dismiss();
                })
                .addButton("Cancel",  getActivity().getResources().getColor(R.color.Buttons),  getActivity().getResources().getColor(R.color.white), CFAlertDialog.CFAlertActionStyle.DEFAULT, CFAlertDialog.CFAlertActionAlignment.JUSTIFIED, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();

    }
}