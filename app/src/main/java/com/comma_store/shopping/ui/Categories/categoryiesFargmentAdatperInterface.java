package com.comma_store.shopping.ui.Categories;

import com.comma_store.shopping.pojo.CategoryScreenResposnse;
import com.comma_store.shopping.pojo.SubCategory;

import java.util.List;

public interface categoryiesFargmentAdatperInterface {
    void onCategoryClicked(CategoryScreenResposnse subCategories,int position);
    void onSubCategoryClicked(int subCategoryId);
}
