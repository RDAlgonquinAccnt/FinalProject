package com.example.finalproject;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class FavouritePage extends AppCompatActivity {
    private ArrayList<SpaceImage> favouriteImages = new ArrayList<>();
    FavouriteAdapter favouriteAdapter = new FavouriteAdapter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite_page);

        ListView favourites =findViewById(R.id.favouritesList);
        Button debug = findViewById(R.id.debugBtn);

        favourites.setAdapter(favouriteAdapter); // sets adapter
        debug.setOnClickListener(click->{
            // adds new image data to list
            favouriteImages.add(new SpaceImage(
                    Integer.toString(favouriteImages.size()),
                    Double.toString(Math.random())));
            favouriteAdapter.notifyDataSetChanged();
        });

        favourites.setOnItemLongClickListener((p, b, position, id)->{
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

            // sets alert dialog to remove the item from the list
            alertDialogBuilder.setTitle("Do you want to delete this?")
                    .setMessage("Title:"+favouriteAdapter.getItem(position).getTitle()+
                            "\n"+"Id:"+position)
                    .setPositiveButton("yes", (click, arg)->{
                        favouriteImages.remove(position);
                        favouriteAdapter.notifyDataSetChanged();
                    })
                    .setNegativeButton("No", (click, arg)->{

                    });

            alertDialogBuilder.create().show();

            return true;
        });
    }

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