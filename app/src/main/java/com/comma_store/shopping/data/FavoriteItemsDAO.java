package com.comma_store.shopping.data;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.comma_store.shopping.pojo.FavoriteItem;

import io.reactivex.Single;

@Dao
public interface FavoriteItemsDAO {

@Query("SELECT count (*)FROM favoriteitems WHERE Id=:Id")
 Single<Integer> ItemCount(int Id);

@Insert
 Single<Long> insetFavoriteItem(FavoriteItem FavoriteItem);

@Delete
 Single<Integer>DeleteFavoriteItem(FavoriteItem FavoriteItem);
}
