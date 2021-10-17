package com.comma_store.shopping.Utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesUtils {
    public static String SharedPrefranceName="MyPref";
    public static String DeviceToken="DeviceToken";
    public static String DeviceTokenSentBoolean="Sent";
    public static String IsLoginBoolean="IsLogin";
    public static String LangKey="Lang";
    public static String TmpToken="TmpToken";
    public static String ApiKey="ApiKey";
    public static String CustomerName="CustomerName";
    public static String CustomerEmail="CustomerEmail";
    public static String CustomerPhone="CustomerPhone";
    public static String CustomerLong="CustomerLong";
    public static String CustomerLat="CustomerLat";
    public static String CustomerAddress="CustomerAddress";
    public static String notificationId="notificationId";
    public static String NotificationNavigation="NotificationNavigation";
    private  SharedPreferences mPref;
    private SharedPreferences.Editor mEditor;
    private static  SharedPreferencesUtils instance;
    private SharedPreferencesUtils(Context context) {
        mPref =context.getSharedPreferences(SharedPrefranceName, Context.MODE_PRIVATE);
        mEditor=mPref.edit();
    }
    public static SharedPreferencesUtils getInstance(final Context context){
        if (instance==null)
            instance=new SharedPreferencesUtils(context);

            return instance;
    }

    public void setDeviceToken(String deviceToken){

        mEditor.putString(DeviceToken, deviceToken);
        mEditor.apply();
    }
    public String getDeviceToken(){
         return mPref.getString(DeviceToken, null);
    }

    public void setDeviceTokenSentBoolean (boolean deviceTokenSent){
        mEditor.putBoolean(DeviceTokenSentBoolean, deviceTokenSent);
        mEditor.apply();
    }

    public boolean getDeviceTokenSentBoolean() {
        return mPref.getBoolean(DeviceTokenSentBoolean,false);
    }
    public void setIsLogin(boolean isLogin){
        mEditor.putBoolean(IsLoginBoolean,isLogin);
        mEditor.apply();
    }
    public boolean getIsLogin(){
        return mPref.getBoolean(IsLoginBoolean,false);
    }

    public void setLangKey(int langKey){

        mEditor.putInt(LangKey,langKey);
        mEditor.apply();

    }
    public int getLangKey(){
        return mPref.getInt(LangKey,0);
    }

    public void setTmpToken(String TmpTokenn){

        mEditor.putString(TmpToken,TmpTokenn);
        mEditor.apply();
    }
    public String getTmpToken(){
        return mPref.getString(TmpToken,null);
    }

    public void setApiKey(String apiKey){

        mEditor.putString(ApiKey,apiKey);
        mEditor.apply();
    }




    public String getApiKey(){
        return mPref.getString(ApiKey,null);
    }
    public void setCustomerName(String customerName){
        mEditor.putString(CustomerName,customerName);
        mEditor.apply();
    }
    public String getCustomerName(){
        return mPref.getString(CustomerName,null);
    }
    public void setCustomerPhone(String customerPhone){
        mEditor.putString(CustomerPhone,customerPhone);
        mEditor.apply();
    }
    public String getCustomerPhone(){
        return mPref.getString(CustomerPhone,null);
    }
    public void setCustomerEmail(String customerEmail){
        mEditor.putString(CustomerEmail,customerEmail);
        mEditor.apply();
    }
    public String getCustomerEmail(){
        return mPref.getString(CustomerEmail,null);
    }
    public void setCustomerLong(String customerLong){
        mEditor.putString(CustomerLong,customerLong);
        mEditor.apply();
    }
    public String getCustomerLong(){
        return mPref.getString(CustomerLong,null);
    }
    public void setCustomerLat(String customerLat){
        mEditor.putString(CustomerLat,customerLat);
        mEditor.apply();
    }
    public String getCustomerLat(){
        return mPref.getString(CustomerLat,null);
    }
    public void setCustomerAddress(String customerAddress){
        mEditor.putString(CustomerAddress,customerAddress);
        mEditor.apply();
    }


    public String getCustomerAddress(){
        return mPref.getString(CustomerAddress,null);
    }


    public void setNotificationId(int notificationI){
        mEditor.putInt(notificationId,notificationI);
        mEditor.apply();
    }

    public int getNotificationId(){
        return mPref.getInt(notificationId,-1);
    }

    public void setNotificationNavigation(String notificationNavigation){
        mEditor.putString(NotificationNavigation,notificationNavigation);
        mEditor.apply();
    }


    public String getNotificationNavigation(){
        return mPref.getString(NotificationNavigation,null);
    }

    public void setCustomerInfo(String customerName,
                               String customerEmail,
                               String customerPhone ,
                                String customerLat,
                                String customerLong,
                                String customerAddress,
                                String apiKey
    ){
        mEditor.putString(CustomerName,customerName);
        mEditor.putString(CustomerEmail,customerEmail);
        mEditor.putString(CustomerPhone,customerPhone);
        mEditor.putString(CustomerLat,customerLat);
        mEditor.putString(CustomerLong,customerLong);
        mEditor.putString(CustomerAddress,customerAddress);
        mEditor.putString(ApiKey,apiKey);
        mEditor.apply();
    }
}
