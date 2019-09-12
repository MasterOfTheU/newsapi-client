package com.newsapiclient.client;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.newsapiclient.R;
import com.newsapiclient.auth.LoginActivity;
import com.newsapiclient.client.ui.news.NewsFragment;
import com.newsapiclient.client.ui.profile.ProfileFragment;
import com.newsapiclient.client.ui.sources.SourcesFragment;
import com.newsapiclient.client.ui.weather.WeatherFragment;


public class HolderActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "HolderActivity";

    private AppBarConfiguration mAppBarConfiguration;

    private DrawerLayout mDrawer;

    private FirebaseUser currentUser;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_holder);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDrawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Animation of drawer toggle icon
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawer, toolbar,
                R.string.nav_drawer_open, R.string.nav_drawer_close);
        mDrawer.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null) {
            loadDefaultFragment();
            navigationView.setCheckedItem(R.id.nav_news);
        }

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_news, R.id.nav_sources, R.id.nav_weather,
                R.id.nav_profile, R.id.nav_signout)
                .setDrawerLayout(mDrawer)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.fragment_container);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

    }

    private void loadDefaultFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new NewsFragment())
                .commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.nav_news:
                Log.d(TAG, "onNavigationItemSelected: open nav_news fragment");
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new NewsFragment())
                        .commit();
                break;
            case R.id.nav_sources:
                Log.d(TAG, "onNavigationItemSelected: open nav_sources fragment");
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new SourcesFragment())
                        .commit();
                break;
            case R.id.nav_weather:
                Log.d(TAG, "onNavigationItemSelected: open nav_weather fragment");
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new WeatherFragment())
                        .commit();
                break;
            case R.id.nav_profile:
                Log.d(TAG, "onNavigationItemSelected: open nav_profile fragment");
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new ProfileFragment())
                        .commit();
                break;
            case R.id.nav_signout:
                Log.d(TAG, "onNavigationItemSelected: sign out ");
                displayToast("Signing out");
                signOut();
                break;
        }
        mDrawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * Makes sure that the navigation is closed before app is killed.
     */
    @Override
    public void onBackPressed() {
        if (mDrawer.isDrawerOpen(GravityCompat.START)) {
            mDrawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void signOut() {
        if (currentUser != null) {
            Log.d(TAG, "signOut: current user is signed out ( " + currentUser.getEmail() + " )");
            mAuth.signOut();
            Intent loginIntent = new Intent(HolderActivity.this, LoginActivity.class);
            startActivity(loginIntent);
            finish();
        }
    }

    private void displayToast(String message) {
        Toast.makeText(getApplicationContext(), message,
                Toast.LENGTH_SHORT).show();
    }

}
