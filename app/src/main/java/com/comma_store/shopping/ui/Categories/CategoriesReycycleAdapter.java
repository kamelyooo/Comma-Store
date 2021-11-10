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
import com.comma_store.shopping.pojo.CategoryScreenResposnse;

import java.util.List;

public class CategoriesReycycleAdapter extends RecyclerView.Adapter<CategoriesReycycleAdapter.ViewHolder> {
    List<CategoryScreenResposnse>categories;

    categoryiesFargmentAdatperInterface categoryiesFargmentAdatperInterface;
    int categorySelected;
    public CategoriesReycycleAdapter(List<CategoryScreenResposnse> categories,categoryiesFargmentAdatperInterface categoryiesFargmentAdatperInterface,int categorySelected) {
        this.categories = categories;
        this.categoryiesFargmentAdatperInterface=categoryiesFargmentAdatperInterface;
        this.categorySelected=categorySelected;
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
        if (categorySelected==position){
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
            radioButtonCategory.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    categoryiesFargmentAdatperInterface.onCategoryClicked(categories.get(getAdapterPosition()),getAdapterPosition());
                }
            });
        }
    }
}
