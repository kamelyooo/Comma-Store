package com.comma_store.shopping.ui.FavoriteItemsScreen;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

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
import com.comma_store.shopping.pojo.FavoriteItem;
import com.comma_store.shopping.pojo.ItemModel;

import java.util.List;
import java.util.stream.Collectors;

public class FavoriteItemsFragment extends Fragment {

    private FavoriteItemsViewModel mViewModel;
    private FavoriteItemsFragmentBinding binding;
    List<Integer> ids;
    Button tryAgain;
    FavoriteItemsAdapter adapter;
    List<FavoriteItem>favoriteItemslist;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(FavoriteItemsViewModel.class);


    }

    private void loadData(List<Integer>ids) {
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
        mViewModel.getCartItems(getActivity());
        mViewModel.ScreenState.observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer StateInt) {
                // 0==Empty
                // 1 ==loading
                //2== Recycle
                //3== error
                if (StateInt!=null) {
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

        CartDataBase.getInstance(getActivity()).favoriteItemsDAO().FavoriteItems2().observe(getViewLifecycleOwner(), new Observer<List<FavoriteItem>>() {
            @Override
            public void onChanged(List<FavoriteItem> favoriteItems) {
                if (favoriteItems!=null){
                    if (favoriteItems.size() != 0) {
                        ids = favoriteItems.parallelStream().map(FavoriteItem::getId).collect(Collectors.toList());
                        loadData(ids);
                       favoriteItemslist=favoriteItems;
                    }else {
                        mViewModel.ScreenState.postValue(0);
                    }
                }
            }
        });
//        mViewModel.FavoriteItemsMutableLiveData.observe(getViewLifecycleOwner(), new Observer<List<FavoriteItem>>() {
//            @Override
//            public void onChanged(List<FavoriteItem> favoriteItems) {
//              if (favoriteItems!=null){
//                  if (favoriteItems.size() != 0) {
//                      ids = favoriteItems.parallelStream().map(FavoriteItem::getId).collect(Collectors.toList());
//                      loadData(ids);
//
//                  }else {
//                      mViewModel.ScreenState.postValue(0);
//                  }
//              }
//            }
//        });

        mViewModel.listItemsMutableLiveData.observe(getViewLifecycleOwner(), new Observer<List<ItemModel>>() {
            @Override
            public void onChanged(List<ItemModel> itemModels) {
                adapter = new FavoriteItemsAdapter(getActivity(),mViewModel.cartItemsId,itemModels,favoriteItemslist);
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
        binding=null;
    }
}