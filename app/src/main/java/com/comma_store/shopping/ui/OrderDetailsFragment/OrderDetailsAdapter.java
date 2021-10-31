package com.comma_store.shopping.ui.OrderDetailsFragment;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.comma_store.shopping.pojo.GetOrderModelResponse;
import com.comma_store.shopping.pojo.OrderDetailsModel;
import com.comma_store.shopping.ui.CompleteOrder.CompleteOrderAdapter;
import com.github.ybq.android.spinkit.SpinKitView;

import java.util.List;

public class OrderDetailsAdapter extends RecyclerView.Adapter<OrderDetailsAdapter.ViewHolder> {
    List<OrderDetailsModel> orderDetails;
    Context context;

    public OrderDetailsAdapter(List<OrderDetailsModel> orderDetails, Context context) {
        this.orderDetails = orderDetails;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_orders_item,parent,false));

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        OrderDetailsModel orderDetailsModel = orderDetails.get(position);
        Glide.with(context).load("https://store-comma.com/mttgr/public/storage/"+orderDetailsModel.getItem().getImages().get(0))
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

        holder.Cm_TitleOrderItem.setText(orderDetailsModel.getItem().getTitle());
        holder.QuantityTVOrderItem.setText(orderDetailsModel.getQuantity()+"");
        holder.PriceAfterOrderItemTV.setText(orderDetailsModel.getItem().getPriceAfter()+context.getResources().getString(R.string.EGP));
        if (orderDetailsModel.getItem().getDiscount()==1){
            holder.Cm_PriceBeforeOrderItem.setText(orderDetailsModel.getItem().getPriceBefor()+context.getResources().getString(R.string.EGP));
            holder.Cm_PriceBeforeOrderItem.setVisibility(View.VISIBLE);
            holder.Cm_ViewOrderItem.setVisibility(View.VISIBLE);
            holder.Cm_DescountPecentageOrderItem.setText(orderDetailsModel.getItem().getDiscountPrecentage()+context.getResources().getString(R.string.off));
            holder.Cm_DescountPecentageOrderItem.setVisibility(View.VISIBLE);
        }

        holder.ColorOrderItemTv.setText(orderDetailsModel.getColor());
        holder.TotalCostOrderItemTv.setText(orderDetailsModel.getCost()+"");


    }

    @Override
    public int getItemCount() {
        return orderDetails.size();
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
