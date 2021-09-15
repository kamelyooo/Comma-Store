package com.comma_store.shopping.ui.Home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.navigation.Navigation;

import com.comma_store.shopping.R;
import com.comma_store.shopping.pojo.SubCategory;
import com.smarteist.autoimageslider.SliderViewAdapter;
import com.squareup.picasso.Picasso;

import java.util.List;

public class HomeImageSliderAdapter extends
        SliderViewAdapter<HomeImageSliderAdapter.SliderAdapterVH> {
    List<SubCategory> subCategories;
    HomeFragmentDirections.ActionHomeFragmentToGetItemsGraph action;

    public HomeImageSliderAdapter(List<SubCategory> subCategories) {
        this.subCategories = subCategories;
    }

    @Override
    public SliderAdapterVH onCreateViewHolder(ViewGroup parent) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_slider_row, null);
        return new SliderAdapterVH(inflate);
    }

    @Override
    public void onBindViewHolder(SliderAdapterVH viewHolder, final int position) {

        viewHolder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             //   Navigation.findNavController(v).navigate(R.id.action_homeFragment_to_get_items_graph);
                action = HomeFragmentDirections.actionHomeFragmentToGetItemsGraph();
                action.setSubCategoryId(subCategories.get(position).getId());
                Navigation.findNavController(v).navigate(action);
            }
        });
        Picasso.get().load("https://store-comma.com/mttgr/public/storage/"+subCategories.get(position).getImage_home()).into( viewHolder.imageView);

    }

    @Override
    public int getCount() {
        //slider view count could be dynamic size
        return subCategories.size();
    }

    class SliderAdapterVH extends SliderViewAdapter.ViewHolder {

        ImageView imageView;
        public SliderAdapterVH(View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.imageSlider);
        }
    }
}
