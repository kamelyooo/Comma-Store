package com.comma_store.shopping.data;

import com.comma_store.shopping.pojo.CategoryModel;
import com.comma_store.shopping.pojo.CategoryScreenResposnse;
import com.comma_store.shopping.pojo.CustomerModel;
import com.comma_store.shopping.pojo.GetHomeResponse;
import com.comma_store.shopping.pojo.GetItemResponse;
import com.comma_store.shopping.pojo.GetItemsCartRespons;
import com.comma_store.shopping.pojo.ItemModel;
import com.comma_store.shopping.pojo.RegisterResponse;
import com.comma_store.shopping.pojo.Resource;
import com.comma_store.shopping.pojo.SubCategory;

import java.util.HashMap;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ItemInterface {

    String token = "$2y$10$6iJjs.jP74WKhX8mXCotFOYDlZipEIil8MAvQaM.xwKiOP4WBJPH6";

    @Headers("token:" + token)
    @GET("mttgr/public/api/getItem")
    Observable<Resource<GetItemResponse>> getItem(@Query("lang") String lang, @Query("item_id") int itemId);

    @Headers("token:" + token)
    @GET("mttgr/public/api/getItemsCart")
    Observable<Resource<List<ItemModel>>> getItemsCart(@Query("lang") String lang, @Query("item_ids[]") List<Integer> itemIds);

    @Headers("token:" + token)
    @GET("mttgr/public/api/home")
    Observable<Resource<GetHomeResponse>> gethome(@Query("lang") String lang);

    @Headers("token:" + token)
    @GET("mttgr/public/api/deals")
    Observable<Resource<List<ItemModel>>> getDealsItems(@Query("lang") String lang, @Query("orderBy") String orderBy, @Query("page") int page);

    @Headers("token:" + token)
    @GET("mttgr/public/api/getCategories")
    Observable<Resource<List<CategoryModel>>> getCategories(@Query("lang") String lang);

    @Headers("token:" + token)
    @GET("mttgr/public/api/getSubcategories")
    Observable<Resource<List<SubCategory>>> getSubCategories(@Query("lang") String lang, @Query("category_id") int category_id);

    @Headers("token:" + token)
    @GET("mttgr/public/api/search")
    Observable<Resource<List<ItemModel>>> getSubCategoriesItems(@Query("lang") String lang, @Query("subcategory_id[]") int subcategory_id
            , @Query("orderBy") String orderBy, @Query("page") int page);

    @Headers("token:" + token)
    @GET("mttgr/public/api/search")
    Observable<Resource<List<ItemModel>>> getSearchItems(@Query("lang") String lang, @Query("q") String query
            , @Query("orderBy") String orderBy, @Query("page") int page);


    @Headers("token:" + token)
    @POST("mttgr/public/api/addDviceTokenGuest")
    Single<Resource<String>> addDviceTokenGuest(@Query("device_token") String deviceToken, @Query("device_type") int deviceType);

    @Headers("token:" + token)
    @POST("mttgr/public/api/register")
    Single<RegisterResponse> Register(@Query("name") String name,
                                      @Query("phone") String phone,
                                      @Query("password") String password,
                                      @Query("device_type") int device_type,
                                      @Query("email") String email,
                                      @Query("longitude") double longitude,
                                      @Query("latitude") double latitude,
                                      @Query("address") String address,
                                      @Query("lang") String lang,
                                      @Query("device_token") String device_token);

    @Headers("token:" + token)
    @POST("mttgr/public/api/validateCode")
    Single<Resource<CustomerModel>> validateCode(@Query("tmpToken") String tmpToken, @Query("activation_key") String activation_key,
                                                 @Query("lang") String lang);

    @Headers("token:" + token)
    @POST("mttgr/public/api/forgetPassword")
    Single<Resource<NullPointerException>> forgetPassword(@Query("email") String email,
                                                          @Query("lang") String lang);

    @Headers("token:" + token)
    @GET("mttgr/public/api/getCategoriesScreen")
    Observable<Resource<List<CategoryScreenResposnse>>> getCategoriesScreen(@Query("lang") String lang);


    @Headers("token:" + token)
    @POST("mttgr/public/api/login")
    Single<Resource<CustomerModel>> login(@Query("email") String email
            , @Query("password") String passeWord
            , @Query("device_type") int deviceType
            , @Query("device_token") String deviceToken
            ,@Query("lang") String lang);

    @Headers("token:" + token)
    @POST("mttgr/public/api/changePasswordTmp")
    Single<Resource<NullPointerException>> ChangePasswordTmp(@Query("tmpToken") String tmpToken
            , @Query("password") String passeWord
            , @Query("activation_key") String activation_key
            ,@Query("lang") String lang);
}
