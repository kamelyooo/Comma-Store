package com.comma_store.shopping;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.NavGraph;
import androidx.navigation.fragment.NavHostFragment;

import android.view.View;

public class LogIn_Registration_Activity extends AppCompatActivity {
    NavHostFragment navHostFragment;
    NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in__registration_);

        navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_LogIN_HOst);
        navController = navHostFragment.getNavController();
        NavGraph graph = navController.getNavInflater().inflate(R.navigation.login_graph);
        int fragmentKey = getIntent().getIntExtra("FragmentKey", 0);
        if (fragmentKey == 0) {
            graph.setStartDestination(R.id.registrationFragment);
        } else if (fragmentKey == 1) {
            graph.setStartDestination(R.id.logInFragment);
        }
        navController.setGraph(graph);
    }
}