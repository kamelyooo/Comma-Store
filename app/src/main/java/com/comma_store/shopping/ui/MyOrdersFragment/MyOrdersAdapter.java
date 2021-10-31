package com.comma_store.shopping.ui.MyOrdersFragment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.comma_store.shopping.R;
import com.comma_store.shopping.pojo.GetOrderModelResponse;

import java.util.Collections;
import java.util.List;

public class MyOrdersAdapter extends RecyclerView.Adapter<MyOrdersAdapter.ViewHolder> {
    List<GetOrderModelResponse>myOrders;
    GetOrderModelResponse myOrder;
    String Egp, Day;
    Context context;
    MyOrdersAdapterOnclick myOrdersAdapterOnclick;

    public MyOrdersAdapter(List<GetOrderModelResponse> myOrders, String egp, String day, Context context, MyOrdersAdapterOnclick myOrdersAdapterOnclick) {
        this.myOrders = myOrders;
        Egp = egp;
        Day = day;
        this.context = context;
        this.myOrdersAdapterOnclick = myOrdersAdapterOnclick;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_my_order_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        myOrder = myOrders.get(position);

        holder.orderNumberTV.setText(myOrder.getCode());
        holder.orderStateTV.setText(myOrder.getStatus_name());
        holder.orderCostTV.setText(myOrder.getCost()+Egp);
        holder.orderDeliveryCostTV.setText(myOrder.getDriver_cost()+Egp);
        holder.orderTotalCostTV.setText(myOrder.getTotal()+Egp);
        holder.orderDurationTV.setText(myOrder.getDuration()+Day);
        holder.orderAddressTV.setText(myOrder.getAddress());
        holder.orderDiscontAmountTV.setText(myOrder.getDiscount()+Egp);
        String[] split = myOrder.getCreated_at().split(" ");
        holder.orderDateTV.setText(split[0]);
        holder.orderTimeTV.setText(split[1]);
    }

    @Override
    public int getItemCount() {
        return myOrders.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView orderNumberTV,
                orderStateTV,
                orderCostTV,orderDiscontTV,
                orderDiscontAmountTV,orderDeliveryCostTV,
                orderTotalCostTV,orderDurationTV,
                orderAddressTV,orderDetailsTV,
                orderDateTV,orderTimeTV;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            orderNumberTV=itemView.findViewById(R.id.orderNumberTV);
            orderStateTV=itemView.findViewById(R.id.orderStateTV);
            orderCostTV=itemView.findViewById(R.id.orderCostTV);
            orderDiscontTV=itemView.findViewById(R.id.orderDiscontTV);
            orderDiscontAmountTV=itemView.findViewById(R.id.orderDiscontAmountTV);
            orderDeliveryCostTV=itemView.findViewById(R.id.orderDeliveryCostTV);
            orderTotalCostTV=itemView.findViewById(R.id.orderTotalCostTV);
            orderDurationTV=itemView.findViewById(R.id.orderDurationTV);
            orderAddressTV=itemView.findViewById(R.id.orderAddressTV);
            orderDetailsTV=itemView.findViewById(R.id.orderDetailsTV);
            orderDateTV=itemView.findViewById(R.id.orderDateTV);
            orderTimeTV=itemView.findViewById(R.id.orderTimeTV);
            orderDetailsTV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    myOrdersAdapterOnclick.onClickListener(getAdapterPosition());
                }
            });
        }
    }
}
