package com.comma_store.shopping.ui.Home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.navigation.Navigation;

import com.comma_store.shopping.R;
import com.comma_store.shopping.pojo.SubCategory;
import com.github.ybq.android.spinkit.SpinKitView;
import com.smarteist.autoimageslider.SliderViewAdapter;
import com.squareup.picasso.Picasso;

import java.util.List;

import io.reactivex.internal.operators.flowable.FlowableThrottleFirstTimed;

public class HomeImageSliderAdapter extends
        SliderViewAdapter<HomeImageSliderAdapter.SliderAdapterVH> {
    List<SubCategory> subCategories;
    HomeAdapterOnClick homeAdapterOnClick;
    public HomeImageSliderAdapter(List<SubCategory> subCategories,HomeAdapterOnClick homeAdapterOnClick) {
        this.subCategories = subCategories;
        this.homeAdapterOnClick=homeAdapterOnClick;
    }

    @Override
    public SliderAdapterVH onCreateViewHolder(ViewGroup parent) {
        return new SliderAdapterVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.image_slider_row, parent,false));
    }

    @Override
    public void onBindViewHolder(SliderAdapterVH viewHolder, final int position) {

        viewHolder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                homeAdapterOnClick.ClickToSubCategoryItems(subCategories.get(position).getId());
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
        SpinKitView spinKitViewSlider;
        public SliderAdapterVH(View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.imageSlider);
            spinKitViewSlider=itemView.findViewById(R.id.spin_kitSliderHOme);
        }
    }
}
