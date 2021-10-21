package com.comma_store.shopping.ui.FavoriteItemsScreen;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.comma_store.shopping.R;
import com.comma_store.shopping.pojo.CartItem;
import com.comma_store.shopping.pojo.ItemModel;

import java.util.zip.Inflater;

public class FavoriteItemsAdapter extends ListAdapter<ItemModel,FavoriteItemsAdapter.ViewHolder> {

    public FavoriteItemsAdapter() {
        super(diffCallback);

    }
    private static final DiffUtil.ItemCallback<ItemModel> diffCallback=new DiffUtil.ItemCallback<ItemModel>() {
        @Override
        public boolean areItemsTheSame(@NonNull ItemModel oldItem, @NonNull ItemModel newItem) {
            return oldItem.getId() ==newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull ItemModel oldItem, @NonNull ItemModel newItem) {
            return true;
        }
    };



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_favorite_item_layout,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    }


    public class ViewHolder extends RecyclerView.ViewHolder  {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
