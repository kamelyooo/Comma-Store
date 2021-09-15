package com.comma_store.shopping.pojo;

import java.util.List;

public class SubCategoryHome{
    int id;
    String title_ar;
    String title_en;
    String image;
    String image_home;
    int active;
    int slider;
    int home;
    int order;
    String created_at;
    String updated_at;
    int category_id;
    int type;
    String title;
    List<ItemModel> itemslimit;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle_ar() {
        return title_ar;
    }

    public void setTitle_ar(String title_ar) {
        this.title_ar = title_ar;
    }

    public String getTitle_en() {
        return title_en;
    }

    public void setTitle_en(String title_en) {
        this.title_en = title_en;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImage_home() {
        return image_home;
    }

    public void setImage_home(String image_home) {
        this.image_home = image_home;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public int getSlider() {
        return slider;
    }

    public void setSlider(int slider) {
        this.slider = slider;
    }

    public int getHome() {
        return home;
    }

    public void setHome(int home) {
        this.home = home;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<ItemModel> getItemslimit() {
        return itemslimit;
    }

    public void setItemslimit(List<ItemModel> itemslimit) {
        this.itemslimit = itemslimit;
    }
}