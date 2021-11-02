package com.comma_store.shopping.ui.SubCategoryItems;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.comma_store.shopping.Utils.NetworkUtils;
import com.comma_store.shopping.R;
import com.comma_store.shopping.data.CartDataBase;
import com.comma_store.shopping.databinding.SubCategoryItemsFragmentBinding;
import com.comma_store.shopping.pojo.FavoriteItem;
import com.comma_store.shopping.pojo.ItemModel;
import com.comma_store.shopping.ui.Deals.ItemAdapter;
import com.comma_store.shopping.ui.Deals.itemAdapterDeals_SubItems;
import com.crowdfire.cfalertdialog.CFAlertDialog;

import io.reactivex.schedulers.Schedulers;

public class SubCategoryItemsFragment extends Fragment implements itemAdapterDeals_SubItems {

     SubCategoryItemsViewModel mViewModel;
    SubCategoryItemsFragmentBinding binding;
    View root;

    ItemAdapter adapter;
    int sortBySelected=0;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(SubCategoryItemsViewModel.class);
      loadData();
    }

    private void loadData() {
        //check if network found or not
        if (NetworkUtils.isNetworkConnected(requireContext())) {
            if (getArguments()!=null){
                mViewModel.getSubCategoryItems(this,SubCategoryItemsFragmentArgs.fromBundle(getArguments()).getSubCategoryId());
            }
        } else {
            mViewModel.setConnected(false);

        }
    }
Button TryAgain;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,R.layout.sub_category_items_fragment, container, false);
        root = binding.getRoot();
        showSpinKit();
        TryAgain=root.findViewById(R.id.Error_Conection_Retry_Btn);
        showSpinKit();
        adapter = new ItemAdapter(getActivity(),this);



        binding.recycleSubCategoryItems.setLayoutManager(new GridLayoutManager(getActivity(),2));
        binding.recycleSubCategoryItems.setAdapter(adapter);
        mViewModel.getIsConnected().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isConnected) {
                if (isConnected){
                    binding.spinKitSubCategoryItems.setVisibility(View.INVISIBLE);
                    mViewModel.showSpinKit=false;
                    binding.SubCategoryItemsScreen.setVisibility(View.VISIBLE);
                }else if (!isConnected){
                    binding.SubCategoryItemsScreen.setVisibility(View.INVISIBLE);
                    binding.SubCategoryItemsErrorConnection.setVisibility(View.VISIBLE);
                    binding.spinKitSubCategoryItems.setVisibility(View.INVISIBLE);
                    mViewModel.showSpinKit=false;
                }
            }
        });
        mViewModel.itemPagedList.observe(getViewLifecycleOwner(), (Observer<PagedList<ItemModel>>) itemModels -> {
            adapter.submitList(itemModels);
        });
        binding.popUpSubCategoryItems.setOnClickListener(v -> Navigation.findNavController(v).navigateUp());
        binding.etSearchViewSubCategoryItems.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_SubCategoryItems_to_search_graph));
        binding.sortBySubCategoryItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                  showSortByDialog();
            }
        });
        TryAgain.setOnClickListener(v -> {
            binding.SubCategoryItemsErrorConnection.setVisibility(View.INVISIBLE);
            binding.spinKitSubCategoryItems.setVisibility(View.VISIBLE);
            mViewModel.showSpinKit=true;
            loadData();
        });
        return root;
    }

    private void showSpinKit() {
        if (mViewModel.showSpinKit)
            binding.spinKitSubCategoryItems.setVisibility(View.VISIBLE);
        else binding.spinKitSubCategoryItems.setVisibility(View.INVISIBLE);
    }


    private void showSortByDialog() {
        CFAlertDialog.Builder builder = new CFAlertDialog.Builder(getActivity());
        builder.setDialogStyle(CFAlertDialog.CFAlertStyle.ALERT);
        builder.setTitle(R.string.Select_Sort_Option);
        builder.setTextGravity(Gravity.RELATIVE_HORIZONTAL_GRAVITY_MASK);
        builder.setSingleChoiceItems(getActivity().getResources().getStringArray(R.array.DialgoArray), sortBySelected, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int index) {
                sortBySelected = index;
                switch (index) {
                    case 0:
                        mViewModel.sort("idDesc");
                        dialogInterface.dismiss();
                        break;
                    case 1:
                        mViewModel.sort("idAsc");
                        dialogInterface.dismiss();

                        break;
                    case 2:

                        mViewModel.sort("priceDesc");
                        dialogInterface.dismiss();

                        break;
                    case 3:
                        mViewModel.sort("priceAsc");
                        dialogInterface.dismiss();
                        break;
                }
            }
        });
        builder.setIcon(R.drawable.ic_notification).setCornerRadius(20).setTextGravity(0);
        builder.show();
    }


    @Override
    public void OnItemClick(ItemModel itemModel) {
        SubCategoryItemsFragmentDirections.ActionSubCategoryItemsToItemDetailsFragment action=SubCategoryItemsFragmentDirections.actionSubCategoryItemsToItemDetailsFragment(itemModel);
                    action.setItemDetails(itemModel);
                    Navigation.findNavController(getView()).navigate(action);
    }

    @Override
    public void OnFavoriteClicked(ItemModel itemModel) {
        Toast.makeText(getActivity(), "Love In SubFragment"+itemModel.getDiscerption(), Toast.LENGTH_SHORT).show();
        CartDataBase.getInstance(getActivity()).favoriteItemsDAO().insetFavoriteItem(new FavoriteItem(itemModel.getId()))
                .subscribeOn(Schedulers.io()).subscribe();

    }

    @Override
    public void onUnFavoriteClicked(ItemModel itemModel) {
        Toast.makeText(getActivity(), "unLove In SubFragment"+itemModel.getDiscerption(), Toast.LENGTH_SHORT).show();
        CartDataBase.getInstance(getActivity()).favoriteItemsDAO().DeleteFavoriteItem(new FavoriteItem(itemModel.getId()))
                .subscribeOn(Schedulers.io()).subscribe();
    }
}