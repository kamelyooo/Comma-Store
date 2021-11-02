package com.comma_store.shopping.ui.FavoriteItemsScreen;

import com.comma_store.shopping.pojo.ItemModel;

public interface FavoriteItemAdapterIterface {
    void onAddToCartBtn(ItemModel itemModel);
    void RemoveToCartBtn(ItemModel itemModel,int position);
}
