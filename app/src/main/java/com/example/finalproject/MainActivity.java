package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button toFinder = findViewById(R.id.ToFind);
        Button toFavs = findViewById(R.id.ToFav);

        Intent toFinderPage = new Intent(this, ImageFinder.class);
        Intent toFavsPage = new Intent(this, FavouritePage.class);

        toFinder.setOnClickListener(click -> startActivity(toFinderPage));
        toFavs.setOnClickListener(click -> startActivity(toFavsPage));
    }
}