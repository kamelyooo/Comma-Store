package com.comma_store.shopping.pojo;

import android.os.Parcelable;

import java.io.Serializable;
import java.util.List;

public class ItemModel implements Serializable {

        int id;
        String code;
        String title_ar;
        String title_en;
        String discerption_ar;
        String discerption_en;
        String provider;
        int discount;
        int discountPrecentage;
        int priceBefor;
        int priceAfter;
        List<String> images;
        int count;
        List<String> colors_ar;
        int duration;
        int active;
        int subcategory_id;
        String created_at;
        String updated_at;
        List<String> colors_en;
        String title;
        String discerption;
        List<String> colors;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
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

        public String getDiscerption_ar() {
            return discerption_ar;
        }

        public void setDiscerption_ar(String discerption_ar) {
            this.discerption_ar = discerption_ar;
        }

        public String getDiscerption_en() {
            return discerption_en;
        }

        public void setDiscerption_en(String discerption_en) {
            this.discerption_en = discerption_en;
        }

        public String getProvider() {
            return provider;
        }

        public void setProvider(String provider) {
            this.provider = provider;
        }

        public int getDiscount() {
            return discount;
        }

        public void setDiscount(int discount) {
            this.discount = discount;
        }

        public int getDiscountPrecentage() {
            return discountPrecentage;
        }

        public void setDiscountPrecentage(int discountPrecentage) {
            this.discountPrecentage = discountPrecentage;
        }

        public int getPriceBefor() {
            return priceBefor;
        }

        public void setPriceBefor(int priceBefor) {
            this.priceBefor = priceBefor;
        }

        public int getPriceAfter() {
            return priceAfter;
        }

        public void setPriceAfter(int priceAfter) {
            this.priceAfter = priceAfter;
        }

        public List<String> getImages() {
            return images;
        }

        public void setImages(List<String> images) {
            this.images = images;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public List<String> getColors_ar() {
            return colors_ar;
        }

        public void setColors_ar(List<String> colors_ar) {
            this.colors_ar = colors_ar;
        }

        public int getDuration() {
            return duration;
        }

        public void setDuration(int duration) {
            this.duration = duration;
        }

        public int getActive() {
            return active;
        }

        public void setActive(int active) {
            this.active = active;
        }

        public int getSubcategory_id() {
            return subcategory_id;
        }

        public void setSubcategory_id(int subcategory_id) {
            this.subcategory_id = subcategory_id;
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

        public List<String> getColors_en() {
            return colors_en;
        }

        public void setColors_en(List<String> colors_en) {
            this.colors_en = colors_en;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDiscerption() {
            return discerption;
        }

        public void setDiscerption(String discerption) {
            this.discerption = discerption;
        }

        public List<String> getColors() {
            return colors;
        }

        public void setColors(List<String> colors) {
            this.colors = colors;
        }
    }

