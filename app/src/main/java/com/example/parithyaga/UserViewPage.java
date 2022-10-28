package com.example.parithyaga;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import DonationManagement.DonationListFragment;
import EventManagement.All_EventsFragment;
import EventManagement.MyEventsFragment;

public class UserViewPage extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    DonationListFragment donationListFragment = new DonationListFragment();
    All_EventsFragment all_eventsFragment = new All_EventsFragment();
    MyEventsFragment myEventsFragment = new MyEventsFragment();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_user_view_page);


        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        getSupportFragmentManager().beginTransaction().replace(R.id.framelayout,donationListFragment).commit();

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
               switch (item.getItemId()){
                   case R.id.view_donations:
                       getSupportFragmentManager().beginTransaction().replace(R.id.framelayout,donationListFragment).commit();
                        return true;
                   case R.id.all_charity_event:
                        getSupportFragmentManager().beginTransaction().replace(R.id.framelayout,all_eventsFragment).commit();
                        return true;
                   case R.id.userCharityEvents:
                        getSupportFragmentManager().beginTransaction().replace(R.id.framelayout,myEventsFragment).commit();
                        return true;
               }
                return false;
            }
        });
    }
}