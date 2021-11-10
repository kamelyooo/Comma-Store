package com.comma_store.shopping.ui.Home;

import com.comma_store.shopping.pojo.ItemModel;

public interface HomeAdapterOnClick {
    void ClickToSubCategoryItems(int CategoryId);
    void ClickToItemDetails(ItemModel itemModel);
    void ClickOnFavorite(int itemId);
    void ClickOnUnFavorite(int itemId);
}
