package com.comma_store.shopping.ui.ItemDetails;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.comma_store.shopping.R;
import com.comma_store.shopping.ui.Home.HomeSubAdapterTybe0;
import com.github.islamkhsh.CardSliderAdapter;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ItemDetailsSliderAdapter extends CardSliderAdapter<ItemDetailsSliderAdapter.ViewHolder> {
    List<String>urls;

    public ItemDetailsSliderAdapter(List<String> urls) {
        this.urls = urls;
    }

    @Override
    public void bindVH(@NotNull ViewHolder viewHolder, int position) {
        Picasso.get().load("https://store-comma.com/mttgr/public/storage/"+urls.get(position)).into(viewHolder.sliderImage);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return  new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.image_slider_row, parent, false));
    }

    @Override
    public int getItemCount() {
        return urls.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView sliderImage;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            sliderImage=itemView.findViewById(R.id.imageSlider);
        }
    }
}
