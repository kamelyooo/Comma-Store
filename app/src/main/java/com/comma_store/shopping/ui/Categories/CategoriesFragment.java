package com.comma_store.shopping.ui.Categories;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.comma_store.shopping.Utils.NetworkUtils;
import com.comma_store.shopping.R;
import com.comma_store.shopping.databinding.CategoriesFragmentBinding;
import com.comma_store.shopping.pojo.CategoryModel;
import com.comma_store.shopping.pojo.CategoryScreenResposnse;
import com.comma_store.shopping.pojo.SubCategory;


import java.util.List;

public class CategoriesFragment extends Fragment {
    CategoriesReycycleAdapter categoriesReycycleAdapter;
    SubCategoriesRecycleAdapter subCategoriesRecycleAdapter;
    CategoriesViewModel mViewModel;
    Button tryAgainErrorScreen;
    CategoriesFragmentBinding binding;
    View root;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(CategoriesViewModel.class);
        loadData();
        mViewModel.showSpinKitCategories = true;
    }

    private void loadData() {
        //check if network found or not
        if (NetworkUtils.isNetworkConnected(requireContext())) {
            mViewModel.getCategoryScreen();
        } else {
            mViewModel.isConnected.postValue(false);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.categories_fragment, container, false);
        root = binding.getRoot();
        showCategoriesSpinKit();

        tryAgainErrorScreen = root.findViewById(R.id.Error_Conection_Retry_Btn);

        //observe on the internet connection
        mViewModel.isConnected.observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isConnected) {
                if (isConnected) {
                    binding.spinKitCategoris.setVisibility(View.INVISIBLE);
                    mViewModel.showSpinKitCategories=false;
                    binding.CategorisScreen.setVisibility(View.VISIBLE);
                    binding.CategoriesErrorConnection.setVisibility(View.INVISIBLE);
                    setCategoriesScreenViews();
                } else {
                    binding.spinKitCategoris.setVisibility(View.INVISIBLE);
                    mViewModel.showSpinKitCategories=false;
                    binding.CategorisScreen.setVisibility(View.INVISIBLE);
                    binding.CategoriesErrorConnection.setVisibility(View.VISIBLE);

                }
            }
        });

        tryAgainErrorScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadData();
                binding.spinKitCategoris.setVisibility(View.VISIBLE);
                mViewModel.showSpinKitCategories=true;
                binding.CategorisScreen.setVisibility(View.INVISIBLE);
                binding.CategoriesErrorConnection.setVisibility(View.INVISIBLE);
            }
        });

        binding.etSearchViewCategorties.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_categoriesFragment_to_search_graph);
            }
        });
        return root;
    }

    private void setCategoriesScreenViews() {
        mViewModel.mutableLiveDataCategoryScreen.observe(getViewLifecycleOwner(), categoryScreenResposnses -> {
            categoriesReycycleAdapter=new CategoriesReycycleAdapter(categoryScreenResposnses,
                    CategoriesFragment.this);
            binding.categoriesRecycleCatScreen.setLayoutManager(new LinearLayoutManager(getActivity()));
            binding.categoriesRecycleCatScreen.setAdapter(categoriesReycycleAdapter);

            Glide.with(getActivity()).load("https://store-comma.com/mttgr/public/storage/"+categoryScreenResposnses.get(mViewModel.categorySelected).getImage())
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            binding.spinKitCategoriyImage.setVisibility(View.INVISIBLE);
                            return false;
                        }
                    }) .into(binding.categoryImageCategoriesScreen);
            subCategoriesRecycleAdapter=new SubCategoriesRecycleAdapter(categoryScreenResposnses.get(mViewModel.categorySelected).getSubcategories());
            binding.subsRecycleCatScreen.setLayoutManager(new GridLayoutManager(getActivity(),3));
            binding.subsRecycleCatScreen.setAdapter(subCategoriesRecycleAdapter);




        });
    }

    private void showCategoriesSpinKit() {
        if (mViewModel.showSpinKitCategories)
            binding.spinKitCategoris.setVisibility(View.VISIBLE);
        else binding.spinKitCategoris.setVisibility(View.INVISIBLE);

    }

}