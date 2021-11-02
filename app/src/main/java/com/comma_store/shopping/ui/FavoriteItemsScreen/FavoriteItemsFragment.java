package com.comma_store.shopping.ui.FavoriteItemsScreen;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.comma_store.shopping.R;
import com.comma_store.shopping.Utils.NetworkUtils;
import com.comma_store.shopping.data.CartDataBase;
import com.comma_store.shopping.databinding.FavoriteItemsFragmentBinding;
import com.comma_store.shopping.pojo.CartItem;
import com.comma_store.shopping.pojo.FavoriteItem;
import com.comma_store.shopping.pojo.ItemModel;
import com.crowdfire.cfalertdialog.CFAlertDialog;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class FavoriteItemsFragment extends Fragment implements FavoriteItemAdapterIterface {

    private FavoriteItemsViewModel mViewModel;
    private FavoriteItemsFragmentBinding binding;
    List<Integer> ids;
    Button tryAgain;
    FavoriteItemsAdapter adapter;
    List<FavoriteItem> favoriteItemslist;
    CompositeDisposable disposable = new CompositeDisposable();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(FavoriteItemsViewModel.class);


    }

    private void loadData(List<Integer> ids) {
        //check if network found or not
        if (NetworkUtils.isNetworkConnected(requireContext())) {
            mViewModel.getItems(ids);
        } else {
            mViewModel.ScreenState.postValue(3);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.favorite_items_fragment, container, false);
        View root = binding.getRoot();
//        mViewModel.getFavoriteItems(getActivity());
        binding.PopUpBtnFavoriteItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigateUp();
            }
        });
//        mViewModel.getCartItems();
        mViewModel.ScreenState.observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer StateInt) {
                // 0==Empty
                // 1 ==loading
                //2== Recycle
                //3== error
                if (StateInt != null) {
                    if (StateInt == 0) {
                        binding.FavoriteItemsScreen.setVisibility(View.INVISIBLE);
                        binding.ErrorScreenFavoriteItems.setVisibility(View.INVISIBLE);
                        binding.spinKitFavoriteScreen.setVisibility(View.INVISIBLE);
                        binding.EmptyFavoriteScreen.setVisibility(View.VISIBLE);
                    } else if (StateInt == 1) {
                        binding.FavoriteItemsScreen.setVisibility(View.INVISIBLE);
                        binding.ErrorScreenFavoriteItems.setVisibility(View.INVISIBLE);
                        binding.spinKitFavoriteScreen.setVisibility(View.VISIBLE);
                        binding.EmptyFavoriteScreen.setVisibility(View.INVISIBLE);
                    } else if (StateInt == 2) {
                        binding.FavoriteItemsScreen.setVisibility(View.VISIBLE);
                        binding.ErrorScreenFavoriteItems.setVisibility(View.INVISIBLE);
                        binding.spinKitFavoriteScreen.setVisibility(View.INVISIBLE);
                        binding.EmptyFavoriteScreen.setVisibility(View.INVISIBLE);
                    } else if (StateInt == 3) {
                        binding.FavoriteItemsScreen.setVisibility(View.INVISIBLE);
                        binding.ErrorScreenFavoriteItems.setVisibility(View.VISIBLE);
                        binding.spinKitFavoriteScreen.setVisibility(View.INVISIBLE);
                        binding.EmptyFavoriteScreen.setVisibility(View.INVISIBLE);
                    }
                }
            }
        });

       disposable.add(CartDataBase.getInstance(getActivity()).favoriteItemsDAO().FavoriteItems().subscribeOn(Schedulers.io()).subscribe(x->{
           if (x != null) {
               if (x.size() != 0) {
                   ids = x.parallelStream().map(FavoriteItem::getId).collect(Collectors.toList());
                   loadData(ids);
                   favoriteItemslist = x;
               } else {
                   mViewModel.ScreenState.postValue(0);
               }
           }
       }));
//        CartDataBase.getInstance(getActivity()).favoriteItemsDAO().FavoriteItems2().observe(getViewLifecycleOwner(), new Observer<List<FavoriteItem>>() {
//            @Override
//            public void onChanged(List<FavoriteItem> favoriteItems) {
//                if (favoriteItems != null) {
//                    if (favoriteItems.size() != 0) {
//                        ids = favoriteItems.parallelStream().map(FavoriteItem::getId).collect(Collectors.toList());
//                        loadData(ids);
//                        favoriteItemslist = favoriteItems;
//                    } else {
//                        mViewModel.ScreenState.postValue(0);
//                    }
//                }
//            }
//        });
        mViewModel.listItemsMutableLiveData.observe(getViewLifecycleOwner(), new Observer<List<ItemModel>>() {
            @Override
            public void onChanged(List<ItemModel> itemModels) {
                adapter = new FavoriteItemsAdapter(getActivity(), itemModels, FavoriteItemsFragment.this);
                binding.FavoriteItemsRecycleView.setLayoutManager(new LinearLayoutManager(getActivity()));
                binding.FavoriteItemsRecycleView.setAdapter(adapter);
                adapter.submitList(itemModels);
            }
        });
        tryAgain = root.findViewById(R.id.Error_Conection_Retry_Btn);
        tryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewModel.ScreenState.postValue(1);
                loadData(ids);
            }
        });

        return root;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mViewModel.FavoriteItemsMutableLiveData.postValue(null);
        mViewModel.ScreenState.postValue(1);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding.unbind();
        binding = null;
        disposable.dispose();
        disposable.clear();
    }

    @Override
    public void onAddToCartBtn(ItemModel itemModel) {
       disposable.add( CartDataBase.getInstance(getActivity()).itemDAO().insert(new CartItem(itemModel.getId(), 1, itemModel.getColors().get(0)))
                .subscribeOn(Schedulers.io()).subscribe());
    }

    @Override
    public void RemoveToCartBtn(ItemModel itemModel, int position) {
        showDialog(position, itemModel);
    }

    private void showDialog(int position, ItemModel itemModel) {
        new CFAlertDialog.Builder(getActivity())
                .setDialogStyle(CFAlertDialog.CFAlertStyle.ALERT)
                .setTitle("Do You Want To Delete The Item ?")
                .setTextColor(getActivity().getResources().getColor(R.color.HeaderTextColor))
                .addButton("Delete", getActivity().getResources().getColor(R.color.white), getActivity().getResources().getColor(R.color.Buttons), CFAlertDialog.CFAlertActionStyle.POSITIVE, CFAlertDialog.CFAlertActionAlignment.JUSTIFIED, (dialog, which) -> {
                    disposable.add(CartDataBase.getInstance(getActivity()).favoriteItemsDAO().DeleteFavoriteItem(new FavoriteItem(itemModel.getId())).subscribeOn(Schedulers.io()).subscribe());
                    adapter.itemModels.remove(position);
                    adapter.notifyItemRemoved(position);
                    if (adapter.getCurrentList().size()==0)
                        mViewModel.ScreenState.postValue(0);
                    dialog.dismiss();
                })
                .addButton("Cancel", getActivity().getResources().getColor(R.color.Buttons), getActivity().getResources().getColor(R.color.white), CFAlertDialog.CFAlertActionStyle.DEFAULT, CFAlertDialog.CFAlertActionAlignment.JUSTIFIED, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();

    }
}