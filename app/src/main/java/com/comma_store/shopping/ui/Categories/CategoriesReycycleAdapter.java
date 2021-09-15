package com.comma_store.shopping.ui.Categories;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.comma_store.shopping.R;
import com.comma_store.shopping.pojo.CategoryModel;
import com.comma_store.shopping.pojo.CategoryScreenResposnse;
import com.comma_store.shopping.ui.Home.HomeRecycleParentAdapter;

import java.util.List;

public class CategoriesReycycleAdapter extends RecyclerView.Adapter<CategoriesReycycleAdapter.ViewHolder> {
    List<CategoryScreenResposnse>categories;
    CategoriesFragment categoriesFragment;

    public CategoriesReycycleAdapter(List<CategoryScreenResposnse> categories, CategoriesFragment categoriesFragment) {
        this.categories = categories;
        this.categoriesFragment = categoriesFragment;
    }

    @NonNull
    @Override
    public CategoriesReycycleAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return  new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_item_cotegory_categories_screen, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CategoriesReycycleAdapter.ViewHolder holder, int position) {
        holder.radioButtonCategory.setText(categories.get(position).getTitle());
        holder.radioButtonCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (categoriesFragment.mViewModel.categorySelected!=position){
                    Glide.with(categoriesFragment.getActivity())
                            .load("https://store-comma.com/mttgr/public/storage/"+categories.get(position).getImage())
                            .listener(new RequestListener<Drawable>() {
                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                    categoriesFragment.binding.spinKitCategoriyImage.setVisibility(View.INVISIBLE);
                                    return false;
                                }
                            }) .into(categoriesFragment.binding.categoryImageCategoriesScreen);
                    categoriesFragment.subCategoriesRecycleAdapter.subCategories=categories.get(position).getSubcategories();
                    categoriesFragment.subCategoriesRecycleAdapter.notifyDataSetChanged();
                }

                categoriesFragment.mViewModel.categorySelected=position;

                notifyDataSetChanged();

            }
        });
        if (categoriesFragment.mViewModel.categorySelected==position){
            holder.radioButtonCategory.setChecked(true);
        }else holder.radioButtonCategory.setChecked(false);

    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RadioButton radioButtonCategory;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            radioButtonCategory=itemView.findViewById(R.id.radioButton_category_CatScreen);
        }
    }
}
