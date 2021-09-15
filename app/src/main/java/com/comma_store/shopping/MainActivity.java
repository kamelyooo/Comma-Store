package com.comma_store.shopping;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.comma_store.shopping.Utils.NavigationUtils;
import com.comma_store.shopping.Utils.SharedPreferencesUtils;
import com.comma_store.shopping.data.CartDataBase;
import com.comma_store.shopping.databinding.ActivityMainBinding;
import com.comma_store.shopping.pojo.CartItem;
import com.comma_store.shopping.ui.Home.HomeFragmentDirections;
import com.comma_store.shopping.ui.Home.HomeViewModel;
import com.google.android.material.badge.BadgeDrawable;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    NavHostFragment navHostFragment;
    NavController navController;
    AppBarConfiguration appBarConfiguration;
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
            switch (getIntent().getStringExtra("navigation")){
                case "order":
                    Toast.makeText(this, "order"+id, Toast.LENGTH_SHORT).show();
                    break;
                case "promoCode":
                    Toast.makeText(this, "promoCode"+id, Toast.LENGTH_SHORT).show();
                    break;
                case "offers_sub":
//                    Toast.makeText(this, "offers_sub"+id, Toast.LENGTH_SHORT).show();
                    break;
            }
        }

//        appBarConfiguration = new AppBarConfiguration.Builder(
//                R.id.homeFragment, R.id.cartFragment, R.id.dealsFragment, R.id.cartFragment, R.id.profileFragment, R.id.categoriesFragment)
//                .build();

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
        badgeDrawable.setVisible(visibilty);
    }
}