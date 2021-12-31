package com.comma_store.shopping.ui.Deals;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.navigation.Navigation;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.comma_store.shopping.R;
import com.comma_store.shopping.Utils.AnimationUtils;
import com.comma_store.shopping.data.CartDataBase;
import com.comma_store.shopping.pojo.ItemModel;
import com.comma_store.shopping.ui.SubCategoryItems.SubCategoryItemsFragmentDirections;
import com.github.ybq.android.spinkit.SpinKitView;



import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class ItemAdapter extends PagedListAdapter<ItemModel,ItemAdapter.ItemViewHolder> {
    private Context mCtx;
    int lastPostion=-1;
    itemAdapterDeals_SubItems itemAdapterDeals_subItems;
    CompositeDisposable disposable=new CompositeDisposable();
    public ItemAdapter(Context mCtx, itemAdapterDeals_SubItems itemAdapterDeals_subItems) {
        super(DIFF_CALLBACK);
        this.mCtx = mCtx;
        this.itemAdapterDeals_subItems=itemAdapterDeals_subItems;

    }
    private static DiffUtil.ItemCallback<ItemModel> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<ItemModel>() {

                @Override
                public boolean areItemsTheSame(@NonNull ItemModel oldItem, @NonNull ItemModel newItem) {
                    return oldItem.getId() == newItem.getId();
                }

                @Override
                public boolean areContentsTheSame(@NonNull ItemModel oldItem, @NonNull ItemModel newItem) {
                    return true;
                }
            };

    @NonNull
    @Override
    public ItemAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ItemViewHolder(LayoutInflater.from(mCtx).inflate(R.layout.custom_item_layout_deals_screen , parent, false));
    }


    @Override
    public void onBindViewHolder(@NonNull ItemAdapter.ItemViewHolder holder, int position) {
        if (holder.getAdapterPosition()>lastPostion) {
            Animation animation = android.view.animation.AnimationUtils.loadAnimation(mCtx, R.anim.scale);
            ((ItemViewHolder) holder).itemView.startAnimation(animation);
        }
        holder.title.setText(getItem(position).getTitle());
        holder.PriceAfter.setText(getItem(position).getPriceAfter()+"");
        holder.PriceBefore.setText(getItem(position).getPriceBefor()+"");
        holder.Precentage.setText(getItem(position).getDiscountPrecentage()+"");
        Glide.with(mCtx).load("https://store-comma.com/mttgr/public/storage/"+getItem(position).getImages().get(0))
                .into(holder.itemImage);
        disposable.add(CartDataBase.getInstance(mCtx).favoriteItemsDAO().ItemCount(getItem(position).getId()).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(x->{
                    if (x==0){
                        holder.unFavoriteImage.setVisibility(View.INVISIBLE);
                        holder.favoriteImage.setVisibility(View.VISIBLE);
                    }else {
                        holder.unFavoriteImage.setVisibility(View.VISIBLE);
                        holder.favoriteImage.setVisibility(View.INVISIBLE);
                    }
                }));
    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        disposable.clear();
        disposable.dispose();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
       ImageView itemImage,favoriteImage,unFavoriteImage;
       TextView title, PriceAfter,PriceBefore
               ,Precentage;
       SpinKitView spinKitView;
        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            itemImage=itemView.findViewById(R.id.cm_Item_Image_Deals_Screen);
            favoriteImage=itemView.findViewById(R.id.favorite_Deals_Screen);
            unFavoriteImage=itemView.findViewById(R.id.Unfavorite_Deals_Screen);
            title=itemView.findViewById(R.id.Custom_item_Title_Deals_Screen);
            PriceAfter =itemView.findViewById(R.id.Custom_Item_PriceAfter_Deals_Screen);
            PriceBefore=itemView.findViewById(R.id.Custom_Item_PriceBefor_Deals_Screen);
            Precentage=itemView.findViewById(R.id.Custom_Item_Precentage_Deals_Screen);
            spinKitView=itemView.findViewById(R.id.spin_kit_Deals_item);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemAdapterDeals_subItems.OnItemClick(getItem(getAdapterPosition()));

                }
            });
            favoriteImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemAdapterDeals_subItems.OnFavoriteClicked(getItem(getAdapterPosition()));
                    AnimationUtils.slideDown(favoriteImage);
                    AnimationUtils.slideUp(unFavoriteImage);
                }
            });
            unFavoriteImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemAdapterDeals_subItems.onUnFavoriteClicked(getItem(getAdapterPosition()));
                    AnimationUtils.slideDown(unFavoriteImage);
                    AnimationUtils.slideUp(favoriteImage);
                }
            });



        }
    }
}
