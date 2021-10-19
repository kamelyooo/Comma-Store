package com.comma_store.shopping.pojo;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
@Entity(tableName = "favoriteitems")
public class FavoriteItem {
    @PrimaryKey
    int Id;

    public FavoriteItem(int id) {
        Id = id;
    }

    public FavoriteItem() {
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }
}
