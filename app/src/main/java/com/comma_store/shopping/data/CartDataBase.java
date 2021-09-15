package com.comma_store.shopping.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.comma_store.shopping.pojo.CartItem;

@Database(entities = {CartItem.class},version = 1)
public abstract class CartDataBase extends RoomDatabase {

    public abstract itemCartDAO itemDAO();
    public static CartDataBase ourInstance;

    public static CartDataBase getInstance(Context context) {

        if (ourInstance == null) {

            ourInstance = Room.databaseBuilder(context,
                    CartDataBase.class, "commacart.db")
                    .createFromAsset("databases/commacart.db")
                    .build();
        }

        return ourInstance;

    }

}
