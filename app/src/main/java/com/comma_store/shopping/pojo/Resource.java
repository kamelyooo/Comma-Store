package com.comma_store.shopping.pojo;

import com.google.gson.annotations.SerializedName;

import java.util.Map;

public class Resource<T> {
    int status;
    Map<String,String> message;
    @SerializedName("first_message")
    String firstMessage;
    boolean isMore;
    String tmpToken;
    int activation_key;
    T data;

    public String getTmpToken() {
        return tmpToken;
    }

    public void setTmpToken(String tmpToken) {
        this.tmpToken = tmpToken;
    }

    public int getActivation_key() {
        return activation_key;
    }

    public void setActivation_key(int activation_key) {
        this.activation_key = activation_key;
    }


    public boolean isMore() {
        return isMore;
    }

    public void setMore(boolean more) {
        isMore = more;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Map<String, String> getMessage() {
        return message;
    }

    public void setMessage(Map<String,String> message) {
        this.message = message;
    }

    public String getFirstMessage() {
        return firstMessage;
    }

    public void setFirstMessage(String firstMessage) {
        this.firstMessage = firstMessage;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
