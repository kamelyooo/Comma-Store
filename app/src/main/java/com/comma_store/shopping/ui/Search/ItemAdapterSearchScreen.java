package com.comma_store.shopping.ui.Search;


import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.navigation.Navigation;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.comma_store.shopping.R;
import com.comma_store.shopping.pojo.ItemModel;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import pl.droidsonroids.gif.GifImageView;

public class ItemAdapterSearchScreen extends PagedListAdapter<ItemModel,ItemAdapterSearchScreen.ItemViewHolder> {
    Context context;
    SearchFragmentDirections.ActionSearchFragmentToItemDetailsFragment3 action;

    public ItemAdapterSearchScreen(Context context) {
        super(DIFF_CALLBACK);
        this.context=context;
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
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_search_item , parent, false));

    }


    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        Picasso.get().load("https://store-comma.com/mttgr/public/storage/"+getItem(position).getImages().get(0)).into(holder.Cm_ImagSearchItem, new Callback() {
            @Override
            public void onSuccess() {
                holder.Spin_Kit_SeachItem.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onError(Exception e) {

            }
        });
        holder.Cm_TitleSearchItem.setText(getItem(position).getTitle());
        holder.Cm_PriceAfterSearchItem.setText(getItem(position).getPriceAfter()+context.getResources().getString(R.string.EGP));
        if (getItem(position).getDiscount()==1){
            holder.Cm_PriceBeforeSearchItem.setText(getItem(position).getPriceBefor()+context.getResources().getString(R.string.EGP));
            holder.Cm_PriceBeforeSearchItem.setVisibility(View.VISIBLE);
            holder.Cm_ViewSearchItem.setVisibility(View.VISIBLE);
            holder.Cm_DescountPecentageSearchItem.setText(getItem(position).getDiscountPrecentage()+context.getResources().getString(R.string.off));
            holder.Cm_DescountPecentageSearchItem.setVisibility(View.VISIBLE);
        }
        holder.Cm_SearchItemLayOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                action = SearchFragmentDirections.actionSearchFragmentToItemDetailsFragment3(getItem(position));
                action.setItemDetails(getItem(position));
                Navigation.findNavController(v).navigate(action);
            }
        });
    }



    public class ItemViewHolder extends RecyclerView.ViewHolder {
        GifImageView Spin_Kit_SeachItem;
        ImageView Cm_ImagSearchItem;
        TextView Cm_TitleSearchItem,Cm_PriceAfterSearchItem
                ,Cm_PriceBeforeSearchItem,Cm_DescountPecentageSearchItem;
        View Cm_ViewSearchItem;
        ConstraintLayout Cm_SearchItemLayOut;
        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
           Spin_Kit_SeachItem=itemView.findViewById(R.id.spin_kit_SerachItem);
            Cm_ImagSearchItem=itemView.findViewById(R.id.Cm_ImageSearchItem);
            Cm_TitleSearchItem=itemView.findViewById(R.id.Cm_TitleSearchItem);
            Cm_PriceAfterSearchItem=itemView.findViewById(R.id.Cm_PriceAfterSearchItem);
            Cm_PriceBeforeSearchItem=itemView.findViewById(R.id.Cm_PriceBeforeSearchItem);
            Cm_DescountPecentageSearchItem=itemView.findViewById(R.id.Cm_DescountPecentageSearchItem);
            Cm_ViewSearchItem=itemView.findViewById(R.id.Cm_ViewSearchItem);
            Cm_SearchItemLayOut=itemView.findViewById(R.id.Cm_SearchItemLayOut);
        }
    }
}
