package com.example.finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

public class Homepage extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==10) {
            Log.e("value", Integer.toString(resultCode));
            if (resultCode==10) finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);


        ImageView toFinder = findViewById(R.id.imgfinderbtn);
        ImageView toFavs = findViewById(R.id.favbtn);

        Intent toFinderPage = new Intent(this, ImageFinder.class);
        Intent toFavsPage = new Intent(this, FavouritePage.class);

        toFinder.setOnClickListener(click -> startActivityForResult(toFinderPage, 10));
        toFavs.setOnClickListener(click -> startActivityForResult(toFavsPage, 10));

        //------------------- TOOLBAR -------------------\\
        androidx.appcompat.widget.Toolbar myToolBar = (androidx.appcompat.widget.Toolbar) findViewById(R.id.HomepageTool);
        setSupportActionBar(myToolBar);
        myToolBar.setBackgroundColor(Color.parseColor("#7733ff"));
        //------------------- NAVBAR -------------------\\
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.HomepageDrawer);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, myToolBar, R.string.openNav, R.string.closeNav);
        drawer.addDrawerListener(toggle);
        drawer.bringChildToFront(findViewById(R.id.nav_view));
        toggle.syncState();
        NavigationView navigationView=(NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_space_bar, menu);
        return true;
    }

    /*
     * Method is responsible for the toolbar selection
     * Pressing help icon brings up a alert dialog
     * @param       item        the item that was clicked on the toolbar
     * @return      true
     * @see         alert dialogue
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.helpItem:
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

                // sets alert dialog to display help
                alertDialogBuilder.setTitle(getApplicationContext().getResources().getString(R.string.help_title))
                        .setMessage(getApplicationContext().getResources().getString(R.string.homepage_help))
                        .setPositiveButton(getApplicationContext().getResources().getString(R.string.okay), (click, arg)->{
                        });

                alertDialogBuilder.create().show();
                break;
        }
        return true;
    }

    /*
     * Method is responsible for the navbar selection
     * Pressing the different elements to go to a differnet page
     * @param       item        the item that was clicked on the navbar
     * @return      true
     * @see         change page
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Intent toPage;
        Log.e("Nav", "here");
        switch (item.getItemId()) {
            case R.id.toHomepageNav:
                Log.e("Home Nav", "here");
                toPage = new Intent(this, Homepage.class);
                startActivity(toPage);
                break;
            case R.id.toFinderNav:
                Log.e("Finder Nav", "here");
                toPage = new Intent(this, ImageFinder.class);
                startActivity(toPage);
                break;
            case R.id.toFavNav:
                Log.e("Fav Nav", "here");
                toPage = new Intent(this, FavouritePage.class);
                startActivity(toPage);
                break;
            case R.id.signoutNav:
                finish();
                break;

        }
        return false;
    }
}