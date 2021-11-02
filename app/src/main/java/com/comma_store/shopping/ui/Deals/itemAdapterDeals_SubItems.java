package com.comma_store.shopping.ui.Deals;

import com.comma_store.shopping.pojo.ItemModel;

public interface itemAdapterDeals_SubItems {
    void OnItemClick(ItemModel itemModel);
    void OnFavoriteClicked(ItemModel itemModel);
    void onUnFavoriteClicked(ItemModel itemModel);
}
