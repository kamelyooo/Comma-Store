package com.comma_store.shopping.ui.Home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
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
import com.comma_store.shopping.ui.Deals.ItemAdapter;


import java.util.List;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class HomeSubAdapterTybe0 extends RecyclerView.Adapter<HomeSubAdapterTybe0.ViewHolder> {
    List<ItemModel> itemModelList;
    Context context;
    int lastPostion = -1;

    CompositeDisposable disposable = new CompositeDisposable();
    HomeAdapterOnClick homeAdapterOnClick;

    public HomeSubAdapterTybe0(List<ItemModel> itemModelList, Context context, HomeAdapterOnClick homeAdapterOnClick) {
        this.itemModelList = itemModelList;
        this.context = context;
        this.homeAdapterOnClick = homeAdapterOnClick;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_item_layout, parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (holder.getAdapterPosition() > lastPostion) {
            Animation animation = android.view.animation.AnimationUtils.loadAnimation(context, R.anim.scale);
            ((ViewHolder) holder).itemView.startAnimation(animation);
        }
        Glide.with(context).load("https://store-comma.com/mttgr/public/storage/" + itemModelList.get(position).getImages().get(0))
                .into(holder.imageViewItem);
        holder.Cm_Item_Title.setText(itemModelList.get(position).getTitle());
        holder.Cm_Item_priceAfter.setText(itemModelList.get(position).getPriceAfter() + context.getResources().getString(R.string.EGP));

        disposable.add(CartDataBase.getInstance(context).favoriteItemsDAO().ItemCount(itemModelList.get(position).getId()).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(x -> {
                    if (x == 0) {
                        holder.Cm_FavoriteImageYes.setVisibility(View.INVISIBLE);
                        holder.Cm_FavoriteImageNo.setVisibility(View.VISIBLE);
                    } else {
                        holder.Cm_FavoriteImageYes.setVisibility(View.VISIBLE);
                        holder.Cm_FavoriteImageNo.setVisibility(View.INVISIBLE);
                    }
                }));

        if (itemModelList.get(position).getDiscount() == 1) {

            holder.Cm_Item_PriceBefor.setVisibility(View.VISIBLE);
            holder.Cm_Item_PriceBefor.setText(itemModelList.get(position).getPriceBefor() + context.getResources().getString(R.string.EGP));
            holder.Cm_DiscountPrecentage.setVisibility(View.VISIBLE);
            holder.Cm_DiscountPrecentage.setText(itemModelList.get(position).getDiscountPrecentage() + context.getResources().getString(R.string.off));
            holder.Cm_Item_View.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return itemModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewItem, Cm_FavoriteImageNo, Cm_FavoriteImageYes;
        TextView Cm_Item_Title, Cm_Item_priceAfter, Cm_Item_PriceBefor, Cm_DiscountPrecentage;
        View Cm_Item_View;
        ConstraintLayout CustomItemLayoutType0;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewItem = itemView.findViewById(R.id.cm_Item_Image);
            Cm_Item_Title = itemView.findViewById(R.id.Custom_item_Title);
            Cm_Item_priceAfter = itemView.findViewById(R.id.Custom_Item_PriceAfter);
            Cm_Item_PriceBefor = itemView.findViewById(R.id.Custom_Item_PriceBefor);
            Cm_DiscountPrecentage = itemView.findViewById(R.id.Custom_Item_Precentage);
            Cm_Item_View = itemView.findViewById(R.id.Custom_Item_View);
            Cm_FavoriteImageNo = itemView.findViewById(R.id.Cm_FavoriteImageNo);
            Cm_FavoriteImageYes = itemView.findViewById(R.id.Cm_FavoriteImageYes);
            CustomItemLayoutType0 = itemView.findViewById(R.id.CustomItemLayoutType0);
            CustomItemLayoutType0.setOnClickListener(v -> {
                homeAdapterOnClick.ClickToItemDetails(itemModelList.get(getAdapterPosition()));
            });


            Cm_FavoriteImageNo.setOnClickListener(v -> {
                homeAdapterOnClick.ClickOnFavorite(itemModelList.get(getAdapterPosition()).getId());
                AnimationUtils.slideDown(Cm_FavoriteImageNo);
                AnimationUtils.slideUp(Cm_FavoriteImageYes);
            });
            Cm_FavoriteImageYes.setOnClickListener(v -> {
                homeAdapterOnClick.ClickOnUnFavorite(itemModelList.get(getAdapterPosition()).getId());
                AnimationUtils.slideDown(Cm_FavoriteImageYes);
                AnimationUtils.slideUp(Cm_FavoriteImageNo);
            });
        }

    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        disposable.clear();
        disposable.dispose();
    }
}
