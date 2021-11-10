package com.comma_store.shopping.ui.Home;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.comma_store.shopping.R;
import com.comma_store.shopping.pojo.ItemModel;
import com.squareup.picasso.Picasso;

import java.util.List;

public class HomeSubAdapterType1 extends RecyclerView.Adapter<HomeSubAdapterType1.ViewHolder> {
    List<ItemModel> itemModelList;
    int type;
    Context context;
    HomeAdapterOnClick homeAdapterOnClick;
    public HomeSubAdapterType1(List<ItemModel> itemModelList, int type, Context context, HomeAdapterOnClick homeAdapterOnClick) {
        this.itemModelList = itemModelList;
        this.type = type;
        this.context = context;
        this.homeAdapterOnClick=homeAdapterOnClick;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new HomeSubAdapterType1.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_item_layout_type1, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Glide.with(context).load("https://store-comma.com/mttgr/public/storage/"+itemModelList.get(position).getImages().get(0))
               .into(holder.imageViewItem_type1);

        holder.Cm_Item_Title_type1.setText(itemModelList.get(position).getTitle());
        holder.Cm_Item_priceAfter_type1.setText(itemModelList.get(position).getPriceAfter()+"");

        if (type==1){
            holder.cm_layout_item_Type1.setBackground(context.getResources().getDrawable(R.drawable.custom_item_shape_type1));
        }else if (type==2){
            holder.cm_layout_item_Type1.setBackground(context.getResources().getDrawable(R.drawable.custom_item_shape_type2));

        }
        if (itemModelList.get(position).getDiscount()==1){

            holder.Cm_Item_PriceBefor_type1.setVisibility(View.VISIBLE);
            holder.Cm_Item_PriceBefor_type1.setText(itemModelList.get(position).getPriceBefor()+"");

            holder.Cm_DiscountPrecentage_type1.setVisibility(View.VISIBLE);
            holder.Cm_DiscountPrecentage_type1.setText(itemModelList.get(position).getDiscountPrecentage()+"");
            holder.Cm_Item_View_type1.setVisibility(View.VISIBLE);
            holder.Cm_Item_EGP_type1.setVisibility(View.VISIBLE);
            holder.Cm_Item_Off_type1.setVisibility(View.VISIBLE);

        }
    }

    @Override
    public int getItemCount() {

        return Math.min(itemModelList.size(), 10);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewItem_type1;
        TextView Cm_Item_Title_type1,Cm_Item_priceAfter_type1,Cm_Item_PriceBefor_type1,Cm_DiscountPrecentage_type1
                ,Cm_Item_Off_type1,Cm_Item_EGP_type1;
        View Cm_Item_View_type1;
        ConstraintLayout cm_layout_item_Type1;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewItem_type1=itemView.findViewById(R.id.cm_Item_Image_type1);
            Cm_Item_Title_type1=itemView.findViewById(R.id.Custom_item_Title_type1);
            Cm_Item_priceAfter_type1=itemView.findViewById(R.id.Custom_Item_PriceAfter_type1);
            Cm_Item_PriceBefor_type1=itemView.findViewById(R.id.Custom_Item_PriceBefor_type1);
            Cm_DiscountPrecentage_type1=itemView.findViewById(R.id.Custom_Item_Precentage_type1);
            Cm_Item_EGP_type1=itemView.findViewById(R.id.Custom_Item_EGP_type1);
            Cm_Item_Off_type1=itemView.findViewById(R.id.Custom_Item_Off_type1);
            Cm_Item_View_type1=itemView.findViewById(R.id.Custom_Item_View_type1);
            cm_layout_item_Type1=itemView.findViewById(R.id.cm_layout_item_Type1);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    homeAdapterOnClick.ClickToItemDetails(itemModelList.get(getAdapterPosition()));
                }
            });
        }
    }
}
