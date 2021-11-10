package com.comma_store.shopping.ui.Home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.comma_store.shopping.R;
import com.comma_store.shopping.pojo.GetHomeResponse;
import com.comma_store.shopping.pojo.SubCategory;
import com.comma_store.shopping.pojo.SubCategoryHome;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.List;

public class HomeRecycleParentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_SLIDER = 0;
    private static final int VIEW_TYPE_SUB_CATEGORIES = 1;
    GetHomeResponse getHomeResponse;
    HomeAdapterOnClick homeAdapterOnClick;
    Context context;

    public HomeRecycleParentAdapter(GetHomeResponse getHomeResponse, Context context,HomeAdapterOnClick homeAdapterOnClick) {
        this.getHomeResponse = getHomeResponse;
        this.context = context;
        this.homeAdapterOnClick=homeAdapterOnClick;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == VIEW_TYPE_SLIDER){

            return  new SliderViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.slider_layout_raw, parent, false));
        }else

        return new subCategoriesViewHolder(LayoutInflater.from(parent.getContext()).
                inflate(R.layout.subcategories_list_row, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position)==VIEW_TYPE_SLIDER){

            ((SliderViewHolder)holder).bindData(getHomeResponse.getSliders());
        }else {
            ((subCategoriesViewHolder)holder).bindData(getHomeResponse.getSubcategoryHome());

        }
    }
    @Override
    public int getItemViewType(int position) {
        if(position == 0){
            return VIEW_TYPE_SLIDER;
        }
        return VIEW_TYPE_SUB_CATEGORIES;
    }
    @Override
    public int getItemCount() {
        return 2;
    }

    public class SliderViewHolder extends RecyclerView.ViewHolder {
//        ViewPager2 Sliderrow;
        SliderView sliderView;
        public SliderViewHolder(@NonNull View itemView) {
            super(itemView);
            sliderView=itemView.findViewById(R.id.imageSlider);
//            Sliderrow.setLayoutManager(new LinearLayoutManager(itemView.getContext(),RecyclerView.HORIZONTAL,false));

        }
        public void bindData(List<SubCategory> subCategoriesList){
            //set view pagerAdapter(slider)
       sliderView.setSliderAdapter(new HomeImageSliderAdapter(subCategoriesList,homeAdapterOnClick));
            sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM);
            sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
            sliderView.startAutoCycle();
        }
    }

    public class subCategoriesViewHolder extends RecyclerView.ViewHolder {
        RecyclerView subCategoriesListRow;

        public subCategoriesViewHolder(@NonNull View itemView) {
            super(itemView);
            subCategoriesListRow=itemView.findViewById(R.id.sub_categories_list);
        }
        public void bindData(List<SubCategoryHome> subCategoryHomeList){
            //set subcategorieslist adapter
            subCategoriesListRow.setAdapter(new HomeSubsAdapter(subCategoryHomeList,context,homeAdapterOnClick));

        }
    }
}
