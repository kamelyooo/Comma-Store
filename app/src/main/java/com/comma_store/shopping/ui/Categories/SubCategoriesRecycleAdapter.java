package com.comma_store.shopping.ui.Categories;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.comma_store.shopping.R;
import com.comma_store.shopping.pojo.SubCategory;
import com.github.ybq.android.spinkit.SpinKitView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SubCategoriesRecycleAdapter extends RecyclerView.Adapter<SubCategoriesRecycleAdapter.ViewHolder> {
    List<SubCategory>subCategories;
    CategoriesFragmentDirections.ActionCategoriesFragmentToGetItemsGraph action;

    public SubCategoriesRecycleAdapter(List<SubCategory> subCategories) {
        this.subCategories = subCategories;
    }

    @NonNull
    @Override
    public SubCategoriesRecycleAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_item_sub_cotegory_categories_screen, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SubCategoriesRecycleAdapter.ViewHolder holder, int position) {
        holder.subCategoryTitle.setText(subCategories.get(position).getTitle());

        Glide.with(holder.itemView).load("https://store-comma.com/mttgr/public/storage/"+subCategories.get(position).getImage())
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        holder.subSpinKit.setVisibility(View.INVISIBLE);

                        return false;
                    }
                }).into(holder.subCategoryImage);

        holder.Cm_layOut_SubCategory_CategoryScteen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                action = CategoriesFragmentDirections.actionCategoriesFragmentToGetItemsGraph();
                action.setSubCategoryId(subCategories.get(position).getId());
                Navigation.findNavController(v).navigate(action);
            }
        });
    }

    @Override
    public int getItemCount() {
        return subCategories.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView subCategoryTitle;
        ImageView subCategoryImage;
        SpinKitView subSpinKit;
        ConstraintLayout Cm_layOut_SubCategory_CategoryScteen;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            subCategoryTitle=itemView.findViewById(R.id.Cm_Title_SubCategory_CategoryScteen);
            subCategoryImage=itemView.findViewById(R.id.Cm_image_SubCategory_CategoryScteen);
            subSpinKit=itemView.findViewById(R.id.cm_spin_kit_SubCategoris);
            Cm_layOut_SubCategory_CategoryScteen=itemView.findViewById(R.id.Cm_layOut_SubCategory_CategoryScteen);
        }
    }
}
