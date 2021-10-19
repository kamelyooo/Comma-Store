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
import com.comma_store.shopping.Utils.AnimationUtils;
import com.comma_store.shopping.data.CartDataBase;
import com.comma_store.shopping.pojo.FavoriteItem;
import com.comma_store.shopping.pojo.ItemModel;
import com.squareup.picasso.Picasso;

import java.util.List;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

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
//        CartDataBase.getInstance(context).favoriteItemsDAO().
        Picasso.get().load("https://store-comma.com/mttgr/public/storage/"+itemModelList.get(position).getImages().get(0))
                .into(holder.imageViewItem);
        holder.Cm_Item_Title.setText(itemModelList.get(position).getTitle());
        holder.Cm_Item_priceAfter.setText(itemModelList.get(position).getPriceAfter()+context.getResources().getString(R.string.EGP));
        holder.CustomItemLayoutType0.setOnClickListener(v -> {
            HomeFragmentDirections.ActionHomeFragmentToItemDetailsFragment2 action=HomeFragmentDirections.actionHomeFragmentToItemDetailsFragment2(itemModelList.get(position));
            action.setItemDetails(itemModelList.get(position));
            Navigation.findNavController(v).navigate(action);
        });
        CartDataBase.getInstance(context).favoriteItemsDAO().ItemCount(itemModelList.get(position).getId()).subscribeOn(Schedulers.io())
               .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Integer>() {
                    @Override
                    public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {

                    }

                    @Override
                    public void onSuccess(@io.reactivex.annotations.NonNull Integer integer) {
                        if (integer==0){
                            holder.Cm_FavoriteImageYes.setVisibility(View.INVISIBLE);
                            holder.Cm_FavoriteImageNo.setVisibility(View.VISIBLE);
                        }else {
                            holder.Cm_FavoriteImageYes.setVisibility(View.VISIBLE);
                            holder.Cm_FavoriteImageNo.setVisibility(View.INVISIBLE);
                        }
                    }

                    @Override
                    public void onError(@io.reactivex.annotations.NonNull Throwable e) {

                    }
                });
        holder.Cm_FavoriteImageNo.setOnClickListener(v -> {
            CartDataBase.getInstance(context).favoriteItemsDAO().insetFavoriteItem(new FavoriteItem(itemModelList.get(position).getId()))
                    .subscribeOn(Schedulers.io()).subscribe();
            AnimationUtils.slideDown(holder.Cm_FavoriteImageNo);
            AnimationUtils.slideUp(holder.Cm_FavoriteImageYes);
        });
        holder.Cm_FavoriteImageYes.setOnClickListener(v -> {
            CartDataBase.getInstance(context).favoriteItemsDAO().DeleteFavoriteItem(new FavoriteItem(itemModelList.get(position).getId()))
                    .subscribeOn(Schedulers.io()).subscribe();
            AnimationUtils.slideDown(holder.Cm_FavoriteImageYes);
            AnimationUtils.slideUp(holder.Cm_FavoriteImageNo);
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
        ImageView imageViewItem,Cm_FavoriteImageNo,Cm_FavoriteImageYes;
        TextView Cm_Item_Title,Cm_Item_priceAfter,Cm_Item_PriceBefor,Cm_DiscountPrecentage;
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
            Cm_FavoriteImageNo=itemView.findViewById(R.id.Cm_FavoriteImageNo);
            Cm_FavoriteImageYes=itemView.findViewById(R.id.Cm_FavoriteImageYes);
            CustomItemLayoutType0=itemView.findViewById(R.id.CustomItemLayoutType0);

        }

    }
}
