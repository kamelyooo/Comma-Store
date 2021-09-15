package com.comma_store.shopping.ui.Home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.comma_store.shopping.R;
import com.comma_store.shopping.pojo.SubCategory;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SliderAdapter extends RecyclerView.Adapter<SliderAdapter.ViewHolder> {
    List<SubCategory>subCategories;
    ViewPager2 viewPager2;
    public SliderAdapter(List<SubCategory> subCategories, ViewPager2 viewPager2) {
        this.subCategories = subCategories;
        this.viewPager2 = viewPager2;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return  new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.image_slider_row, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_homeFragment_to_get_items_graph);
                Toast.makeText(v.getContext(),   subCategories.get(position).getId()+"", Toast.LENGTH_SHORT).show();
            }
        });
        Picasso.get().load("https://store-comma.com/mttgr/public/storage/"+subCategories.get(position).getImage_home()).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return subCategories.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.imageSlider);
        }
    }
}
