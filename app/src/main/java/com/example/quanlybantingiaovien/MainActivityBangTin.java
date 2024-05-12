package com.example.quanlybantingiaovien;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.quanlybantingiaovien.databinding.ActivityMainBinding;

public class MainActivityBangTin extends AppCompatActivity  {

    private ActivityMainBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_new, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        //ẩn ActionBar
        getSupportActionBar().hide();

//        Toolbar toolbar=findViewById(R.id.toolbar);
//        setSupportActionBar(null);


    }
//    public void goToChiTietBaiGiang(thongtinbaigiangModel ttbg){
//        FragmentTransaction fragmentTransaction= getSupportFragmentManager().beginTransaction();
//        chitietbaigiangFragment chitietbaigiangFragment= new chitietbaigiangFragment();
//        Bundle bundle=new Bundle();
//        bundle.putSerializable("key_chitietbaigiang", (Serializable) ttbg);
//        chitietbaigiangFragment.setArguments(bundle);
//        fragmentTransaction.replace(R.id.container,new chitietbaigiangFragment());
//        fragmentTransaction.commit();
//    }


}