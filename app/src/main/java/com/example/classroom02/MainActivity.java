package com.example.classroom02;


import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.classroom02.Fragment.ArchivedClassesFragment;
import com.example.classroom02.Fragment.HomeFragment;
import com.example.classroom02.Fragment.NotificationFragment;
import com.example.classroom02.Fragment.PasswordUpdateFragment;
import com.example.classroom02.Fragment.SettingsFragment;
import com.example.classroom02.Fragment.UserInfoFragment;
import com.example.classroom02.Fragment.UserUpdateFragment;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawerLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar); //Ignore red line errors
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_views);
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav,
                R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
            navigationView.setCheckedItem(R.id.home);
        }
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.home) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
        } else if (item.getItemId() == R.id.notifications) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new NotificationFragment()).commit();
        } else if (item.getItemId() == R.id.ArchivedClasses) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ArchivedClassesFragment()).commit();
        } else if (item.getItemId() == R.id.Settings) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new SettingsFragment()).commit();
        } else if (item.getItemId() == R.id.Users) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new UserInfoFragment()).commit();
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
    public void onButtonClickUpdate(View view) {
        // Thực hiện phương thức của bạn ở đây
        replaceFragmentUpdate();
    }
    public void replaceFragmentUpdate() {
        UserUpdateFragment usersUpdateFragment = new UserUpdateFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, usersUpdateFragment);
        ((FragmentTransaction) fragmentTransaction).addToBackStack(null); // Thêm Fragment vào back stack để quay trở lại
        fragmentTransaction.commit();
    }

    public void onButtonClickChange(View view) {
        // Thực hiện phương thức của bạn ở đây
        replaceFragmentChange();
    }
    public void replaceFragmentChange() {
        PasswordUpdateFragment passwordUpdateFragment = new PasswordUpdateFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, passwordUpdateFragment);
        ((FragmentTransaction) fragmentTransaction).addToBackStack(null); // Thêm Fragment vào back stack để quay trở lại
        fragmentTransaction.commit();
    }
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}