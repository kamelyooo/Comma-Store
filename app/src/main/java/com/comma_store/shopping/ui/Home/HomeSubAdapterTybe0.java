package com.comma_store.shopping.ui.Home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.comma_store.shopping.R;
import com.comma_store.shopping.pojo.ItemModel;
import com.squareup.picasso.Picasso;

import java.util.List;

public class    HomeSubAdapterTybe0 extends RecyclerView.Adapter<HomeSubAdapterTybe0.ViewHolder> {
    List<ItemModel>itemModelList;
    Context context;

    public HomeSubAdapterTybe0(List<ItemModel> itemModelList, Context context) {
        this.itemModelList = itemModelList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_item_layout, parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Picasso.get().load("https://store-comma.com/mttgr/public/storage/"+itemModelList.get(position).getImages().get(0))
                .into(holder.imageViewItem);
        holder.Cm_Item_Title.setText(itemModelList.get(position).getTitle());
        holder.Cm_Item_priceAfter.setText(itemModelList.get(position).getPriceAfter()+context.getResources().getString(R.string.EGP));
        holder.CustomItemLayoutType0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomeFragmentDirections.ActionHomeFragmentToItemDetailsFragment2 action=HomeFragmentDirections.actionHomeFragmentToItemDetailsFragment2(itemModelList.get(position));
                action.setItemDetails(itemModelList.get(position));
                Navigation.findNavController(v).navigate(action);
            }
        });
        if (itemModelList.get(position).getDiscount()==1){

            holder.Cm_Item_PriceBefor.setVisibility(View.VISIBLE);
            holder.Cm_Item_PriceBefor.setText(itemModelList.get(position).getPriceBefor()+context.getResources().getString(R.string.EGP));
            holder.Cm_DiscountPrecentage.setVisibility(View.VISIBLE);
            holder.Cm_DiscountPrecentage.setText(itemModelList.get(position).getDiscountPrecentage()+context.getResources().getString(R.string.off));
            holder.Cm_Item_View.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return itemModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewItem;
        TextView Cm_Item_Title,Cm_Item_priceAfter,Cm_Item_PriceBefor,Cm_DiscountPrecentage
                ,Cm_Item_Off,Cm_Item_EGP;
        View Cm_Item_View;
        ConstraintLayout CustomItemLayoutType0;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewItem=itemView.findViewById(R.id.cm_Item_Image);
            Cm_Item_Title=itemView.findViewById(R.id.Custom_item_Title);
            Cm_Item_priceAfter=itemView.findViewById(R.id.Custom_Item_PriceAfter);
            Cm_Item_PriceBefor=itemView.findViewById(R.id.Custom_Item_PriceBefor);
            Cm_DiscountPrecentage=itemView.findViewById(R.id.Custom_Item_Precentage);
            Cm_Item_View=itemView.findViewById(R.id.Custom_Item_View);
            CustomItemLayoutType0=itemView.findViewById(R.id.CustomItemLayoutType0);
        }
    }
}
