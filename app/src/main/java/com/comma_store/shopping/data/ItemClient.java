package com.comma_store.shopping.data;

import retrofit2.Retrofit;

import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ItemClient {

    private static final String url = "https://store-comma.com/";
    private static ItemClient INSTANCE;
    private ItemInterface itemInterface;
    Retrofit retrofit;

    public ItemClient() {
        retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    public static ItemClient getINSTANCE() {
        if (INSTANCE == null) {
            INSTANCE = new ItemClient();
        }
        return INSTANCE;
    }

    public ItemInterface getItemInterface() {
        return retrofit.create(ItemInterface.class);
    }

}
