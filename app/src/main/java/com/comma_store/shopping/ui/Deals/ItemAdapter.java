package com.comma_store.shopping.ui.Deals;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.navigation.Navigation;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.comma_store.shopping.R;
import com.comma_store.shopping.pojo.ItemModel;
import com.comma_store.shopping.ui.SubCategoryItems.SubCategoryItemsFragmentDirections;
import com.github.ybq.android.spinkit.SpinKitView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class ItemAdapter extends PagedListAdapter<ItemModel,ItemAdapter.ItemViewHolder> {
    private Context mCtx;
    public ItemAdapter(Context mCtx) {
        super(DIFF_CALLBACK);
        this.mCtx = mCtx;
    }
    private static DiffUtil.ItemCallback<ItemModel> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<ItemModel>() {

                @Override
                public boolean areItemsTheSame(@NonNull ItemModel oldItem, @NonNull ItemModel newItem) {
                    return oldItem.getId() == newItem.getId();
                }

                @Override
                public boolean areContentsTheSame(@NonNull ItemModel oldItem, @NonNull ItemModel newItem) {
                    return true;
                }
            };

    @NonNull
    @Override
    public ItemAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ItemViewHolder(LayoutInflater.from(mCtx).inflate(R.layout.custom_item_layout_deals_screen , parent, false));
    }


    @Override
    public void onBindViewHolder(@NonNull ItemAdapter.ItemViewHolder holder, int position) {
        holder.title.setText(getItem(position).getTitle());
        holder.PriceAfter.setText(getItem(position).getPriceAfter()+"");
        holder.PriceBefore.setText(getItem(position).getPriceBefor()+"");
        holder.Precentage.setText(getItem(position).getDiscountPrecentage()+"");

        Glide.with(mCtx).load("https://store-comma.com/mttgr/public/storage/"+getItem(position).getImages().get(0))
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        holder.spinKitView.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        holder.spinKitView.setVisibility(View.GONE);
                        return false;
                    }
                }).into(holder.itemImage);
        holder.favoriteImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mCtx, "loved"+getItem(position).getTitle(), Toast.LENGTH_SHORT).show();
            }
        });
        holder.DealsItemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ( Navigation.findNavController(v).getCurrentDestination().getId()==R.id.dealsFragment){
                    DealsFragmentDirections.ActionDealsFragmentToItemDetailsFragment2 action=DealsFragmentDirections.actionDealsFragmentToItemDetailsFragment2(getItem(position));
                    action.setItemDetails(getItem(position));
                    Navigation.findNavController(v).navigate(action);
                }else{
                    SubCategoryItemsFragmentDirections.ActionSubCategoryItemsToItemDetailsFragment action=SubCategoryItemsFragmentDirections.actionSubCategoryItemsToItemDetailsFragment(getItem(position));
                    action.setItemDetails(getItem(position));
                    Navigation.findNavController(v).navigate(action);
                }

            }
        });
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
       ImageView itemImage,favoriteImage;
       TextView title, PriceAfter,PriceBefore
               ,Precentage;
       SpinKitView spinKitView;
       ConstraintLayout DealsItemLayout;
        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            itemImage=itemView.findViewById(R.id.cm_Item_Image_Deals_Screen);
            favoriteImage=itemView.findViewById(R.id.favorite_Deals_Screen);
            title=itemView.findViewById(R.id.Custom_item_Title_Deals_Screen);
            PriceAfter =itemView.findViewById(R.id.Custom_Item_PriceAfter_Deals_Screen);
            PriceBefore=itemView.findViewById(R.id.Custom_Item_PriceBefor_Deals_Screen);
            Precentage=itemView.findViewById(R.id.Custom_Item_Precentage_Deals_Screen);
            spinKitView=itemView.findViewById(R.id.spin_kit_Deals_item);
            DealsItemLayout=itemView.findViewById(R.id.DealsItemLayOut);
        }
    }
}
