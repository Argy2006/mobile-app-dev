package com.example.myapplication;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private TextView tvMainContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Bind Views
        drawerLayout = findViewById(R.id.drawerlayout);
        navigationView = findViewById(R.id.navView);
        toolbar = findViewById(R.id.toolbar);
        tvMainContent = findViewById(R.id.tvMainContent);

        // Set Toolbar as ActionBar
        setSupportActionBar(toolbar);

        // Configure ActionBarDrawerToggle (hamburger icon animation & state sync)
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                R.string.app_name,
                R.string.app_name
        );
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Listen for navigation item selections
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_home);

        // Handle System Back Button Press (Closes drawer if open)
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (drawerLayout != null && drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    setEnabled(false);
                    getOnBackPressedDispatcher().onBackPressed();
                }
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            tvMainContent.setText("You are on the Home Screen");
            Toast.makeText(this, "Home Selected", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_profile) {
            tvMainContent.setText("You are on the Profile Screen");
            Toast.makeText(this, "Profile Selected", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_settings) {
            tvMainContent.setText("You are on the Settings Screen");
            Toast.makeText(this, "Settings Selected", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_about) {
            tvMainContent.setText("You are on the About Screen");
            Toast.makeText(this, "About Selected", Toast.LENGTH_SHORT).show();
        }

        // Close drawer smoothly after selection
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}
