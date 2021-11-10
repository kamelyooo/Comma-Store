package com.comma_store.shopping.ui.Home;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.helper.widget.Flow;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.comma_store.shopping.R;
import com.comma_store.shopping.pojo.ItemModel;
import com.comma_store.shopping.pojo.SubCategoryHome;
import com.github.ybq.android.spinkit.SpinKitView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

import pl.droidsonroids.gif.GifImageView;

public class HomeSubsAdapter  extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
   Context context;
    List<SubCategoryHome>subCategoryHomeList;

    HomeAdapterOnClick homeAdapterOnClick;
    public HomeSubsAdapter(List<SubCategoryHome> subCategoryHomeList,Context context, HomeAdapterOnClick homeAdapterOnClick) {
        this.subCategoryHomeList = subCategoryHomeList;
        this.context=context;
        this.homeAdapterOnClick=homeAdapterOnClick;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (subCategoryHomeList.get(parent.getChildCount()).getType()==0)
        return  new Type0ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.sub_category_home_type0, parent, false));
        else if (subCategoryHomeList.get(parent.getChildCount()).getType()==1||subCategoryHomeList.get(parent.getChildCount()).getType()==2)
            return  new Type1ViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.sub_category_home_type1, parent, false));
        return  new Type2ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.sub_category_home_type2, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (subCategoryHomeList.get(position).getType()==0){
            ((Type0ViewHolder)holder).SubHomeType0TitleTv.setText(subCategoryHomeList.get(position).getTitle());
            ((Type0ViewHolder)holder).bindData(subCategoryHomeList.get(position).getItemslimit());
            ((Type0ViewHolder)holder).ShopNow_sub_home_type0.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    homeAdapterOnClick.ClickToSubCategoryItems(subCategoryHomeList.get(position).getId());

                }
            });
            }else if (subCategoryHomeList.get(position).getType()==1||subCategoryHomeList.get(position).getType()==2) {

            ((Type1ViewHolder)holder).Title_sub_home_type1.setText(subCategoryHomeList.get(position).getTitle());
            ((Type1ViewHolder)holder).bindData(subCategoryHomeList.get(position).getItemslimit(),getItemViewType(position));
            ((Type1ViewHolder)holder).sHopNow_sub_home_type1.setOnClickListener(v -> {
                homeAdapterOnClick.ClickToSubCategoryItems(subCategoryHomeList.get(position).getId());

            });
            Picasso.get().load("https://store-comma.com/mttgr/public/storage/"+subCategoryHomeList.get(position).getImage_home())
                    .into(  ((Type1ViewHolder)holder).Image_sub_Home_Type1);
        }else {
//            Picasso.get().load("https://store-comma.com/mttgr/public/storage/" + subCategoryHomeList.get(position).getImage_home())
//                    .into(((Type2ViewHolder) holder).image_sub_home_type2, new Callback() {
//                        @Override
//                        public void onSuccess() {
//                            ((Type2ViewHolder) holder).spinSubHOmeType2.setVisibility(View.INVISIBLE);
//                        }
//
//                        @Override
//                        public void onError(Exception e) {
//
//                        }
//                    });
            Glide.with(context).load("http://store-comma.com/mttgr/public/storage/" + subCategoryHomeList.get(position).getImage_home())
                    .into(((Type2ViewHolder) holder).image_sub_home_type2);
            ((Type2ViewHolder)holder).image_sub_home_type2.setOnClickListener(v -> {
                homeAdapterOnClick.ClickToSubCategoryItems(subCategoryHomeList.get(position).getId());
            });
        }

    }

//    @Override
//    public int getItemViewType(int position) {
//        return subCategoryHomeList.get(position).getType();
//    }

    @Override
    public int getItemCount() {
        return subCategoryHomeList.size();
    }

    public class Type0ViewHolder extends RecyclerView.ViewHolder {
        TextView SubHomeType0TitleTv;
        RecyclerView subHomeRecycleType0;
        Flow ShopNow_sub_home_type0;
        public Type0ViewHolder(@NonNull View itemView) {
            super(itemView);
            SubHomeType0TitleTv=itemView.findViewById(R.id.SubHomeType0TitleTv);
            subHomeRecycleType0=itemView.findViewById(R.id.subHomeRecycleType0);
            ShopNow_sub_home_type0=itemView.findViewById(R.id.ShopNow_sub_home_type0);
        }
        public void bindData(List<ItemModel>itemslimit){
            subHomeRecycleType0.setAdapter(new HomeSubAdapterTybe0(itemslimit,context,homeAdapterOnClick));
            subHomeRecycleType0.setLayoutManager(new LinearLayoutManager(itemView.getContext(),LinearLayoutManager.HORIZONTAL,false));

        }
    }

    public class Type1ViewHolder extends RecyclerView.ViewHolder {
        ImageView Image_sub_Home_Type1;
        Flow sHopNow_sub_home_type1;
        TextView Title_sub_home_type1;
        RecyclerView recycler_sub_home_type1;

        public Type1ViewHolder(@NonNull View itemView) {
            super(itemView);
            Title_sub_home_type1=itemView.findViewById(R.id.Title_sub_home_type1);
            recycler_sub_home_type1=itemView.findViewById(R.id.recycle_sub_home_type1);
            sHopNow_sub_home_type1=itemView.findViewById(R.id.ShopNow_sub_home_type1);
            Image_sub_Home_Type1=itemView.findViewById(R.id.image_sub_home_type1);

        }

        public void bindData(List<ItemModel>itemslimit,int type){
            recycler_sub_home_type1.setLayoutManager(new GridLayoutManager(itemView.getContext(), 2));
            recycler_sub_home_type1.setAdapter(new HomeSubAdapterType1(itemslimit,type,context,homeAdapterOnClick));

        }
    }
    public class Type2ViewHolder extends RecyclerView.ViewHolder {
        SpinKitView spinSubHOmeType2;
        ImageView image_sub_home_type2;
        public Type2ViewHolder(@NonNull View itemView) {
            super(itemView);
            spinSubHOmeType2=itemView.findViewById(R.id.spin_sub_category_home_type2);
            image_sub_home_type2=itemView.findViewById(R.id.image_sub_home_type2);
        }
    }
}
