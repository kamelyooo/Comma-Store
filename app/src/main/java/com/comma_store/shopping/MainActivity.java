package com.comma_store.shopping;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.comma_store.shopping.Utils.NavigationUtils;
import com.comma_store.shopping.Utils.SharedPreferencesUtils;
import com.comma_store.shopping.data.CartDataBase;
import com.comma_store.shopping.databinding.ActivityMainBinding;
import com.comma_store.shopping.pojo.CartItem;
import com.google.android.material.badge.BadgeDrawable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    NavHostFragment navHostFragment;
    NavController navController;
    ActivityMainBinding binding;
    BadgeDrawable badgeDrawable;

    int lang;
    Locale locale;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lang = SharedPreferencesUtils.getInstance(this).getLangKey();
        if (lang == 0) {
            locale = new Locale("en");
        } else if(lang == 1) {

            locale = new Locale("ar");
        }
        if (locale != null)
            Locale.setDefault(locale);
        Resources resources = getResources();
        Configuration config = resources.getConfiguration();
        config.setLocale(locale);
        resources.updateConfiguration(config, resources.getDisplayMetrics());
        
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        binding.setLifecycleOwner(this);
        binding.navView.setItemIconTintList(null);
        binding.navView.setItemRippleColor(null);
        navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        navController = navHostFragment.getNavController();
        List<Integer>graphIds= Arrays.asList(
                R.navigation.home_graph,
                R.navigation.categories_graph,
                R.navigation.deals_graph,
                R.navigation.cart_graph,
                R.navigation.profile_graph
        );

        NavigationUtils.INSTANCE.setupWithNavController(graphIds,getSupportFragmentManager(),R.id.nav_host_fragment
                ,new Intent(),binding.navView);

        if(getIntent().hasExtra("navigation")){
            int id = getIntent().getIntExtra("id",0);
            SharedPreferencesUtils.getInstance(this).setNotificationId(id);
            switch (getIntent().getStringExtra("navigation")){
                case "order":
                    SharedPreferencesUtils.getInstance(this).setNotificationNavigation("order");
                    break;
                case "promoCode":
                    SharedPreferencesUtils.getInstance(this).setNotificationNavigation("promoCode");
                    break;
                case "offers_sub":
                    SharedPreferencesUtils.getInstance(this).setNotificationNavigation("offers_sub");
                    break;
            }
        }

        CartDataBase.getInstance(this).itemDAO().GetItemsCart2().observe(this, new Observer<List<CartItem>>() {
            @Override
            public void onChanged(List<CartItem> cartItems) {
                badecounter(R.id.cart_graph, cartItems.size(), cartItems.size() > 0);
            }
        });


    }
    @Override
    public boolean onSupportNavigateUp() {
        return navController.navigateUp() || super.onSupportNavigateUp();
    }

    private void badecounter(int id, int number, boolean visibilty) {
        badgeDrawable = binding.navView.getOrCreateBadge(id);
        badgeDrawable.setBackgroundColor(getResources().getColor(R.color.Buttons));
        badgeDrawable.setBadgeTextColor(Color.WHITE);
        badgeDrawable.setMaxCharacterCount(3);
        badgeDrawable.setNumber(number);
        badgeDrawable.setBadgeGravity(BadgeDrawable.TOP_END);
        badgeDrawable.setVisible(visibilty);

    }
}