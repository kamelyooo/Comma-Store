package com.comma_store.shopping.ui.CompleteOrder;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.comma_store.shopping.R;
import com.comma_store.shopping.pojo.CartItem;
import com.comma_store.shopping.pojo.ItemModel;
import com.github.ybq.android.spinkit.SpinKitView;

import java.util.List;
import java.util.Optional;

public class CompleteOrderAdapter extends RecyclerView.Adapter<CompleteOrderAdapter.ViewHolder> {
    List<CartItem> cartItemList;
    List<ItemModel> itemModelsList;
    Context mCtx;

    public CompleteOrderAdapter(List<CartItem> cartItemList, List<ItemModel> itemModelsList, Context mCtx) {
        this.cartItemList = cartItemList;
        this.itemModelsList = itemModelsList;
        this.mCtx = mCtx;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_orders_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Optional<ItemModel> itemModel = itemModelsList.stream().filter(x -> x.getId() == cartItemList.get(position).getItem_id()).findFirst();
        if (itemModel.isPresent()) {
            Glide.with(mCtx).load("https://store-comma.com/mttgr/public/storage/"+itemModel.get().getImages().get(0))
                    .addListener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            holder.spin_kit_OrderItem.setVisibility(View.INVISIBLE);
                            return false;
                        }
                    }).into(holder.imageViewOrderItem);

            holder.Cm_TitleOrderItem.setText(itemModel.get().getTitle());
            holder.QuantityTVOrderItem.setText(cartItemList.get(position).getQuantity()+"");
            holder.PriceAfterOrderItemTV.setText(itemModel.get().getPriceAfter()+mCtx.getResources().getString(R.string.EGP));
            if (itemModel.get().getDiscount()==1){
                holder.Cm_PriceBeforeOrderItem.setText(itemModel.get().getPriceBefor()+mCtx.getResources().getString(R.string.EGP));
                holder.Cm_PriceBeforeOrderItem.setVisibility(View.VISIBLE);
                holder.Cm_ViewOrderItem.setVisibility(View.VISIBLE);
                holder.Cm_DescountPecentageOrderItem.setText(itemModel.get().getDiscountPrecentage()+mCtx.getResources().getString(R.string.off));
                holder.Cm_DescountPecentageOrderItem.setVisibility(View.VISIBLE);
            }

            if (cartItemList.get(position).getColor()!=null){
                holder.ColorOrderItemTv.setText(cartItemList.get(position).getColor());
                holder.ColorOrderItemTv.setVisibility(View.VISIBLE);
                holder.textViewColotText.setVisibility(View.VISIBLE);
            }
            holder.TotalCostOrderItemTv.setText(itemModel.get().getPriceAfter()*cartItemList.get(position).getQuantity()+"");

        }
    }

    @Override
    public int getItemCount() {
        return  cartItemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewOrderItem;
        TextView Cm_TitleOrderItem,
                QuantityTVOrderItem,
                PriceAfterOrderItemTV,
                Cm_PriceBeforeOrderItem,
                Cm_DescountPecentageOrderItem,
                ColorOrderItemTv,
                textViewColotText,
                TotalCostOrderItemTv;
        View Cm_ViewOrderItem;
        SpinKitView spin_kit_OrderItem;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewOrderItem=itemView.findViewById(R.id.imageViewOrderItem);
            Cm_TitleOrderItem=itemView.findViewById(R.id.Cm_TitleOrderItem);
            QuantityTVOrderItem=itemView.findViewById(R.id.QuantityTVOrderItem);
            PriceAfterOrderItemTV=itemView.findViewById(R.id.PriceAfterOrderItemTV);
            Cm_PriceBeforeOrderItem=itemView.findViewById(R.id.Cm_PriceBeforeOrderItem);
            Cm_DescountPecentageOrderItem=itemView.findViewById(R.id.Cm_DescountPecentageOrderItem);
            ColorOrderItemTv=itemView.findViewById(R.id.ColorOrderItemTv);
            textViewColotText=itemView.findViewById(R.id.textViewColotText);
            TotalCostOrderItemTv=itemView.findViewById(R.id.TotalCostOrderItemTv);
            Cm_ViewOrderItem=itemView.findViewById(R.id.Cm_ViewOrderItem);
            spin_kit_OrderItem=itemView.findViewById(R.id.spin_kit_OrderItem);

        }
    }
}
