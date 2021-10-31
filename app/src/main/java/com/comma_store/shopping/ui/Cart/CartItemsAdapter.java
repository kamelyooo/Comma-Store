package com.comma_store.shopping.ui.Cart;

import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.comma_store.shopping.R;
import com.comma_store.shopping.pojo.CartItem;
import com.comma_store.shopping.pojo.ItemModel;
import com.crowdfire.cfalertdialog.CFAlertDialog;
import com.github.ybq.android.spinkit.SpinKitView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Optional;

import pl.droidsonroids.gif.GifImageView;

public class CartItemsAdapter extends ListAdapter<CartItem,CartItemsAdapter.ViewHolder> {
    List<ItemModel>ItemsCart;
    List<CartItem>localItemsCart;

    int quantity;

    CartFragment cartFragment;
    public CartItemsAdapter(
            CartFragment cartFragment,List<CartItem>localItemsCart ,List<ItemModel> itemsCart) {
        super(diffCallback);
        this.localItemsCart=localItemsCart;
        ItemsCart = itemsCart;


        this.cartFragment=cartFragment;
    }

    private static final DiffUtil.ItemCallback<CartItem> diffCallback=new DiffUtil.ItemCallback<CartItem>() {
        @Override
        public boolean areItemsTheSame(@NonNull CartItem oldItem, @NonNull CartItem newItem) {
            return oldItem.getQuantity() ==newItem.getQuantity();
        }

        @Override
        public boolean areContentsTheSame(@NonNull CartItem oldItem, @NonNull CartItem newItem) {
            return oldItem.getQuantity() == newItem.getQuantity() &&
                    oldItem.getColor().equals(newItem.getColor());
        }
    };
    @NonNull
    @Override
    public CartItemsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_cart_item , parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CartItemsAdapter.ViewHolder holder, int position) {
        CartItem localItem = getItem(position);
        Optional<ItemModel> itemModelFound = ItemsCart.stream().filter(x -> x.getId() == localItem.getItem_id()).findFirst();
        if (itemModelFound.isPresent()) {
            if (itemModelFound.get().getCount()==0){
                holder.Error_Layout_Item_Cart.setVisibility(View.VISIBLE);
                holder.Error_Layout_Item_Cart.setBackground(cartFragment.getActivity().getDrawable(R.drawable.warning_not_found_cart_item_shap));
                holder.warningImage_CartItem.setImageResource(R.drawable.ic_warning_not_found_cart_item_shap);
                holder.warningMassage_CartItem.setText("This Item is Out Of Stoke You Have To Remove It To Complete Your Order");

            }else if (localItem.getQuantity()>itemModelFound.get().getCount()){

                holder.Error_Layout_Item_Cart.setVisibility(View.VISIBLE);
                holder.Error_Layout_Item_Cart.setBackground(cartFragment.getActivity().getDrawable(R.drawable.warning_not_enough_cart_item_shap));
                holder.warningImage_CartItem.setImageResource(R.drawable.ic_warning_not_enough_cart_item_shap);
                holder.warningMassage_CartItem.setText("there is no enough amount of item ..the avalible amount is ("+itemModelFound.get().getCount()+")");

            }else  holder.Error_Layout_Item_Cart.setVisibility(View.GONE);
            holder.closeWarning_itemCart.setOnClickListener(v -> holder.Error_Layout_Item_Cart.setVisibility(View.GONE));
            Picasso.get().load("https://store-comma.com/mttgr/public/storage/"+itemModelFound.get().getImages().get(0))
                    .into(holder.cm_Image_cartItem, new Callback() {
                        @Override
                        public void onSuccess() {
                            holder.cm_spin_kit_CartItem.setVisibility(View.INVISIBLE);
                        }

                        @Override
                        public void onError(Exception e) {

                        }
                    });
            holder.cm_title_cartItem.setText(itemModelFound.get().getTitle());
            holder.cm_PriceAfter_Cart_item.setText(itemModelFound.get().getPriceAfter()+cartFragment.getActivity().getResources().getString(R.string.EGP));
            if (itemModelFound.get().getDiscount()==1){
                holder.cm_PriceBefor_Cart_item.setText(itemModelFound.get().getPriceBefor()+cartFragment.getActivity().getResources().getString(R.string.EGP));
                holder.cm_DescountPercentage_cart_Item.setText(itemModelFound.get().getDiscountPrecentage()+cartFragment.getActivity().getResources().getString(R.string.off));
                holder.cm_DescountLayout_CartItem.setVisibility(View.VISIBLE);
            }else  holder.cm_DescountLayout_CartItem.setVisibility(View.INVISIBLE);

            holder.cm_QuantityTV_CartItem.setText(localItem.getQuantity()+"");
            holder.cm_PlusButton_CartItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    quantity = localItem.getQuantity();
                    quantity++;
                    localItem.setQuantity(quantity);
                    cartFragment.mViewModel.updateItemCart(cartFragment.getActivity(),localItem);
                    notifyDataSetChanged();
                }
            });
            holder.cm_MinusButton_CartItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    quantity = localItem.getQuantity();
                    if (quantity>1) {
                        quantity--;
                        localItem.setQuantity(quantity);
                        cartFragment.mViewModel.updateItemCart(cartFragment.getActivity(),localItem);
                        notifyDataSetChanged();

                    }else if (quantity==1){
                        Toast.makeText(cartFragment.getActivity(), "Quantity Cant Be Less Than 1", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            holder.cm_Delete_cartItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//
                     showDialog(cartFragment.getActivity(),position,localItem);
                }
            });
            holder.cm_total_CartItem.setText(localItem.getQuantity()*itemModelFound.get().getPriceAfter()+"");
        }
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        ConstraintLayout Error_Layout_Item_Cart,cm_DescountLayout_CartItem,
        cm_Delete_cartItem;
        ImageView warningImage_CartItem,closeWarning_itemCart,
                cm_Image_cartItem;
        TextView warningMassage_CartItem,cm_title_cartItem
                ,cm_PriceAfter_Cart_item,cm_DescountPercentage_cart_Item,
                cm_PriceBefor_Cart_item,
                cm_QuantityTV_CartItem,cm_total_CartItem;
        Button cm_MinusButton_CartItem,cm_PlusButton_CartItem;
        SpinKitView cm_spin_kit_CartItem;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            Error_Layout_Item_Cart=itemView.findViewById(R.id.Error_Layout_Item_Cart);
            cm_DescountLayout_CartItem=itemView.findViewById(R.id.cm_DescountLayout_CartItem);
            cm_Delete_cartItem=itemView.findViewById(R.id.cm_Delete_cartItem);
            warningImage_CartItem=itemView.findViewById(R.id.warningImage_CartItem);
            closeWarning_itemCart=itemView.findViewById(R.id.closeWarning_itemCart);
            cm_Image_cartItem=itemView.findViewById(R.id.cm_Image_cartItem);
            warningMassage_CartItem=itemView.findViewById(R.id.warningMassage_CartItem);
            cm_title_cartItem=itemView.findViewById(R.id.cm_title_cartItem);
            cm_PriceAfter_Cart_item=itemView.findViewById(R.id.cm_PriceAfter_Cart_item);
            cm_DescountPercentage_cart_Item=itemView.findViewById(R.id.cm_DescountPercentage_cart_Item);
            cm_PriceBefor_Cart_item=itemView.findViewById(R.id.cm_PriceBefor_Cart_item);
            cm_QuantityTV_CartItem=itemView.findViewById(R.id.cm_QuantityTV_CartItem);
            cm_total_CartItem=itemView.findViewById(R.id.cm_total_CartItem);
            cm_MinusButton_CartItem=itemView.findViewById(R.id.cm_MinusButton_CartItem);
            cm_PlusButton_CartItem=itemView.findViewById(R.id.cm_PlusButton_CartItem);
            cm_spin_kit_CartItem=itemView.findViewById(R.id.cm_spin_kit_CartItem);
        }
    }
    private  void showDialog(FragmentActivity activity,int position,CartItem cartItem){
        new CFAlertDialog.Builder(activity)
                .setDialogStyle(CFAlertDialog.CFAlertStyle.ALERT)
                .setTitle("Do You Want To Delete The Item ?")
            .setTextColor( activity.getResources().getColor(R.color.HeaderTextColor))
                .addButton("Delete", activity.getResources().getColor(R.color.white), activity.getResources().getColor(R.color.Buttons), CFAlertDialog.CFAlertActionStyle.POSITIVE, CFAlertDialog.CFAlertActionAlignment.JUSTIFIED, (dialog, which) -> {
                    localItemsCart.remove(position);
                    cartFragment.mViewModel.DeleteItemCart(activity,cartItem);
                    notifyItemRemoved(position);
                    if (localItemsCart.isEmpty()){
                       cartFragment.binding.EmptyCartScreen.setVisibility(View.VISIBLE);
                       cartFragment.binding.cartScreen.setVisibility(View.INVISIBLE);
                    }
                    dialog.dismiss();
                })
            .addButton("Cancel",  activity.getResources().getColor(R.color.Buttons),  activity.getResources().getColor(R.color.white), CFAlertDialog.CFAlertActionStyle.DEFAULT, CFAlertDialog.CFAlertActionAlignment.JUSTIFIED, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            })
            .show();

    }
}
