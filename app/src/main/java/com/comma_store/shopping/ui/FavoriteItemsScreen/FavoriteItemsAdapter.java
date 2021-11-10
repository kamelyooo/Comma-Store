package com.comma_store.shopping.ui.FavoriteItemsScreen;

import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.QuickContactBadge;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.exifinterface.media.ExifInterface;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.AndroidViewModel;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.comma_store.shopping.R;
import com.comma_store.shopping.Utils.AnimationUtils;
import com.comma_store.shopping.data.CartDataBase;
import com.comma_store.shopping.pojo.CartItem;
import com.comma_store.shopping.pojo.FavoriteItem;
import com.comma_store.shopping.pojo.ItemModel;
import com.crowdfire.cfalertdialog.CFAlertDialog;
import com.github.ybq.android.spinkit.SpinKitView;

import java.util.List;
import java.util.Optional;
import java.util.zip.Inflater;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class FavoriteItemsAdapter extends ListAdapter<ItemModel, FavoriteItemsAdapter.ViewHolder> {
    Context context;
    List<ItemModel> itemModels;
    FavoriteItemAdapterIterface favoriteItemAdapterIterface;
    CompositeDisposable disposable = new CompositeDisposable();

    public FavoriteItemsAdapter(Context context, List<ItemModel> itemModels, FavoriteItemAdapterIterface favoriteItemAdapterIterface) {
        super(diffCallback);
        this.context = context;

        this.itemModels = itemModels;
        this.favoriteItemAdapterIterface = favoriteItemAdapterIterface;
    }

    private static final DiffUtil.ItemCallback<ItemModel> diffCallback = new DiffUtil.ItemCallback<ItemModel>() {
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
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_favorite_item_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(context).load("https://store-comma.com/mttgr/public/storage/" + getItem(position).getImages().get(0)).addListener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                holder.cm_spin_kit_favoriteItem.setVisibility(View.INVISIBLE);
                return false;
            }
        }).into(holder.cm_Image_favoriteItem);
        holder.cm_title_favoriteItem.setText(getItem(position).getTitle());
        holder.cm_PriceAfter_favorite_item.setText(getItem(position).getPriceAfter() + context.getResources().getString(R.string.EGP));
        if (getItem(position).getDiscount() == 1) {
            holder.cm_DescountLayout_favoriteItem.setVisibility(View.VISIBLE);
            holder.cm_PriceBefore_favorite_item.setText(getItem(position).getPriceBefor() + context.getResources().getString(R.string.EGP));
            holder.cm_DescountPercentage_favorite_Item.setText(getItem(position).getDiscountPrecentage() + context.getResources().getString(R.string.off));
        }
        disposable.add(CartDataBase.getInstance(context).itemDAO().ItemCount(getItem(position).getId()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(x -> {
                    if (x == 0) {
                        holder.cm_AddedToCartButton.setVisibility(View.INVISIBLE);
                        holder.cm_AddToCartButton.setVisibility(View.VISIBLE);
                    } else{
                        holder.cm_AddedToCartButton.setVisibility(View.VISIBLE);
                        holder.cm_AddToCartButton.setVisibility(View.INVISIBLE);
                    }
                }));
    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        disposable.clear();
        disposable.dispose();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView cm_Image_favoriteItem;
        SpinKitView cm_spin_kit_favoriteItem;
        TextView cm_title_favoriteItem,
                cm_PriceAfter_favorite_item,
                cm_PriceBefore_favorite_item,
                cm_DescountPercentage_favorite_Item;
        ConstraintLayout cm_DescountLayout_favoriteItem,
                cm_AddedToCartButton, cm_AddToCartButton,
                cm_Delete_favoriteItem;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cm_Image_favoriteItem = itemView.findViewById(R.id.cm_Image_favoriteItem);
            cm_spin_kit_favoriteItem = itemView.findViewById(R.id.cm_spin_kit_favoriteItem);
            cm_title_favoriteItem = itemView.findViewById(R.id.cm_title_favoriteItem);
            cm_PriceAfter_favorite_item = itemView.findViewById(R.id.cm_PriceAfter_favorite_item);
            cm_PriceBefore_favorite_item = itemView.findViewById(R.id.cm_PriceBefore_favorite_item);
            cm_DescountPercentage_favorite_Item = itemView.findViewById(R.id.cm_DescountPercentage_favorite_Item);
            cm_DescountLayout_favoriteItem = itemView.findViewById(R.id.cm_DescountLayout_favoriteItem);
            cm_AddedToCartButton = itemView.findViewById(R.id.cm_AddedToCartButton);
            cm_AddToCartButton = itemView.findViewById(R.id.cm_AddToCartButton);
            cm_Delete_favoriteItem = itemView.findViewById(R.id.cm_Delete_favoriteItem);

            cm_AddToCartButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    favoriteItemAdapterIterface.onAddToCartBtn(getItem(getAdapterPosition()));
                    AnimationUtils.slideDown(cm_AddToCartButton);
                    AnimationUtils.slideUp(cm_AddedToCartButton);
                }
            });
            cm_Delete_favoriteItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    favoriteItemAdapterIterface.RemoveToCartBtn(getItem(getAdapterPosition()), getAdapterPosition());
                }
            });
        }
    }

}
