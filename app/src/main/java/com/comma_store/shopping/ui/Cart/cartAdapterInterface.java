package com.comma_store.shopping.ui.Cart;

import com.comma_store.shopping.pojo.CartItem;

public interface cartAdapterInterface {
    void onPlusClicked(CartItem cartItem);
    void onMinusClicked(CartItem cartItem);
    void onRemoveClicked(CartItem cartItem,int position);
}
