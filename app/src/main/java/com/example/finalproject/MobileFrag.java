package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MobileFrag extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_frag);

        Bundle extras = getIntent().getExtras();
        FavouriteFragment dFragment = new FavouriteFragment();

        dFragment.setArguments(extras);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.TabletLayout,dFragment)
                .commit();
    }
}