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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class FavouritePage extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{
    private ArrayList<SpaceImage> favouriteImages = new ArrayList<>();
    FavouriteAdapter favouriteAdapter = new FavouriteAdapter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite_page);

        ListView favourites =findViewById(R.id.favouritesList);
        Button debug = findViewById(R.id.debugBtn);

        androidx.appcompat.widget.Toolbar myToolBar = (androidx.appcompat.widget.Toolbar) findViewById(R.id.FavTool);
        setSupportActionBar(myToolBar);
        myToolBar.setBackgroundColor(Color.parseColor("#7733ff"));

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.FavDrawer);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, myToolBar, R.string.openNav, R.string.closeNav);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView=(NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        favourites.setAdapter(favouriteAdapter); // sets adapter
        /*
         * Adds a randomized item to the list of favourites. This button is for debug purpose
         * only and will be changed later
         * @param    click
         * @return   void
         **/
        debug.setOnClickListener(click->{
            // adds new image data to list
            favouriteImages.add(new SpaceImage(
                    Integer.toString(favouriteImages.size()),
                    Double.toString(Math.random())));
            favouriteAdapter.notifyDataSetChanged();
        });

        /*
         * Brings up alert dialog for the user to remove an item from the list or not
         * @param       p
         * @param       b
         * @param       position        The position of the element in the list that was clicked
         * @param       id
         * @return      true
         * @see         alertDialog
         **/
        favourites.setOnItemLongClickListener((p, b, position, id)->{
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

            // sets alert dialog to remove the item from the list
            alertDialogBuilder.setTitle(getApplicationContext().getResources().getString(R.string.delete_this))
                    .setMessage(getApplicationContext().getResources().getString(R.string.title)+favouriteAdapter.getItem(position).getTitle()+
                            "\n"+"Id:"+position)
                    .setPositiveButton(getApplicationContext().getResources().getString(R.string.yes), (click, arg)->{
                        favouriteImages.remove(position);
                        favouriteAdapter.notifyDataSetChanged();
                    })
                    .setNegativeButton(getApplicationContext().getResources().getString(R.string.no), (click, arg)->{

                    });

            alertDialogBuilder.create().show();

            return true;
        });
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
                        .setMessage(getApplicationContext().getResources().getString(R.string.favs_help))
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
                toPage = new Intent(this, Homepage.class);
                toPage.putExtra("logout", 10);
                setResult(10, toPage);
                finish();
                break;

        }
        return false;
    }

    /*
     * Adapter for the listview
     * Extends to the BaseAdapter interface
     */
    class FavouriteAdapter extends BaseAdapter{
        @Override
        public int getCount() {
            return favouriteImages.size();
        }

        @Override
        public SpaceImage getItem(int position) {
            return favouriteImages.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        /*
         * Returns the view for the list element
         * @param       position        The position of the element
         * @param       oldView         The old view for the element
         * @param       viewGroup
         * @return      newView         The new updated view
         * @see         newView
         **/
        @Override
        public View getView(int position, View oldView, ViewGroup viewGroup) {
            View newView = oldView;
            LayoutInflater inflater =getLayoutInflater();
            SpaceImage savedImage = getItem(position);

            // makes inflater
            if (newView==null) newView=inflater.inflate(R.layout.space_image_element, viewGroup, false);

            // sets data
            TextView titleView=newView.findViewById(R.id.imageTitle);
            titleView.setText(savedImage.getTitle());

            TextView dateView=newView.findViewById(R.id.imageDate);
            dateView.setText(savedImage.getDate());
            return newView;
        }
    }
}