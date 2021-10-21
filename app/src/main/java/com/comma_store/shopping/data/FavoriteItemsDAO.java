package com.comma_store.shopping.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.comma_store.shopping.pojo.FavoriteItem;

import java.util.List;

import io.reactivex.Single;

@Dao
public interface FavoriteItemsDAO {

@Query("SELECT count (*)FROM favoriteitems WHERE Id=:Id")
 Single<Integer> ItemCount(int Id);

@Insert
 Single<Long> insetFavoriteItem(FavoriteItem FavoriteItem);

@Delete
 Single<Integer>DeleteFavoriteItem(FavoriteItem FavoriteItem);

 @Query("SELECT * FROM favoriteitems")
Single<List<FavoriteItem>> FavoriteItems();

 @Query("SELECT * FROM favoriteitems")
 LiveData<List<FavoriteItem>> FavoriteItems2();
}


