package com.comma_store.shopping.pojo;

import java.util.List;

public class GetHomeResponse {
    List<SubCategory> sliders;
    List<SubCategoryHome>subcategoryHome;

    public List<SubCategory> getSliders() {
        return sliders;
    }

    public void setSliders(List<SubCategory> sliders) {
        this.sliders = sliders;
    }

    public List<SubCategoryHome> getSubcategoryHome() {
        return subcategoryHome;
    }

    public void setSubcategoryHome(List<SubCategoryHome> subcategoryHome) {
        this.subcategoryHome = subcategoryHome;
    }
}
