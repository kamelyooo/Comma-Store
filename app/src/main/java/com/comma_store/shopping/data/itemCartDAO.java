package com.comma_store.shopping.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.comma_store.shopping.pojo.CartItem;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;


@Dao
public interface itemCartDAO {
    @Query("SELECT * FROM cart")
    Single<List<CartItem>> GetItemsCart();
    @Query("SELECT * FROM cart")
    LiveData<List<CartItem>> GetItemsCart2();
    @Delete
    Single<Integer> delete (CartItem item);
    @Update
    Single<Integer> update( CartItem item);
    @Insert
    Single<Long> insert(CartItem CartItem);
    @Query("SELECT * FROM cart WHERE item_id =:item_Id")
    Single<CartItem> getitembyId( int item_Id);

    @Query("SELECT * FROM cart WHERE item_id =:item_Id")
    LiveData<CartItem> getItemById2( int item_Id);
    @Query("SELECT SUM (quantity) as total FROM cart")
    Single<Integer> TotalItemInCart ();

    @Query("SELECT count (*)FROM cart WHERE item_id=:Id")
    Single<Integer> ItemCount(int Id);
    @Query("DELETE FROM cart")
   Single<Void> deleteAll ();
}
