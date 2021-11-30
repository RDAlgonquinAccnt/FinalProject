package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

public class Homepage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);


        ImageView toFinder = findViewById(R.id.imgfinderbtn);
        ImageView toFavs = findViewById(R.id.favbtn);

        Intent toFinderPage = new Intent(this, ImageFinder.class);
        Intent toFavsPage = new Intent(this, FavouritePage.class);

        toFinder.setOnClickListener(click -> startActivity(toFinderPage));
        toFavs.setOnClickListener(click -> startActivity(toFavsPage));
    }
}