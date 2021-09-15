package com.comma_store.shopping.ui.Deals;

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

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.comma_store.shopping.Utils.NetworkUtils;
import com.comma_store.shopping.R;
import com.comma_store.shopping.databinding.DealsFragmentBinding;
import com.comma_store.shopping.pojo.ItemModel;
import com.crowdfire.cfalertdialog.CFAlertDialog;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class DealsFragment extends Fragment {

    DealsViewModel mViewModel;
    DealsFragmentBinding binding;
    Button tryAgainErrorScreen;
    FlexboxLayoutManager layoutManager;
    ItemAdapter adapter;
    View root;
    int sortBySelected = 0;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(DealsViewModel.class);
        loadData();
        mViewModel.setShowSpinKit(true);
    }

    private void loadData() {
        //check if network found or not
        if (NetworkUtils.isNetworkConnected(requireContext())) {

            mViewModel.getItemsPagedDeals(this);
        } else {
            mViewModel.isConnected.postValue(false);

        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.deals_fragment, container, false);
        root = binding.getRoot();
        tryAgainErrorScreen = root.findViewById(R.id.Error_Conection_Retry_Btn);
        //show spinKite according to showSpinkite in view modle
        showSpinKite();
        //onclick on sort by to open the dialog of sorting
        binding.sortByDeals.setOnClickListener(v -> {
            showSortByDialog();
        });
        //make adapter for recycleView Deals
        adapter = new ItemAdapter(getActivity());
        layoutManager = new FlexboxLayoutManager(getActivity());
        layoutManager.setJustifyContent(JustifyContent.SPACE_EVENLY);
        binding.RecycleDeals.setLayoutManager(new GridLayoutManager(getActivity(),2));
        binding.RecycleDeals.setAdapter(adapter);
        binding.DealsScreen.setVisibility(View.VISIBLE);
        //on click listenter for search edit text
        binding.etSearchViewDeals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_dealsFragment_to_search_graph);
//                binding.RecycleDeals.smoothScrollToPosition(0);
            }
        });


        mViewModel.isConnected.observe(getViewLifecycleOwner(), internetConnected -> {
            if (internetConnected) {
                //set the spinner inVisible
                //  binding.spinKitDeals.setVisibility(View.INVISIBLE);
                //get item for adapter for recycle
                binding.DealsScreen.setVisibility(View.VISIBLE);
                binding.spinKitDeals.setVisibility(View.INVISIBLE);
                mViewModel.setShowSpinKit(false);
            } else if (!internetConnected) {
                mViewModel.setShowSpinKit(false);
                binding.spinKitDeals.setVisibility(View.INVISIBLE);
                binding.dealErrorConnection.setVisibility(View.VISIBLE);
                binding.DealsScreen.setVisibility(View.INVISIBLE);
            }
        });
        mViewModel.iitempagelist.observe(getViewLifecycleOwner(), new Observer<PagedList<ItemModel>>() {
            @Override
            public void onChanged(PagedList<ItemModel> itemModels) {
                          adapter.submitList(itemModels);

            }
        });
//        mViewModel.itemPagedList.observe(getViewLifecycleOwner(), (Observer<PagedList<ItemModel>>) itemModels -> {
//            adapter.submitList(itemModels);
//        });
        //tryAgain Button in Deals Fragment
        tryAgainErrorScreen.setOnClickListener(v -> {
            loadData();
            mViewModel.setShowSpinKit(true);
            binding.spinKitDeals.setVisibility(View.VISIBLE);
            binding.dealErrorConnection.setVisibility(View.INVISIBLE);
        });

        return root;
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

    private void showSpinKite() {
        if (mViewModel.showSpinKit)
            binding.spinKitDeals.setVisibility(View.VISIBLE);
        else binding.spinKitDeals.setVisibility(View.INVISIBLE);
    }

}