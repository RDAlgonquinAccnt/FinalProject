package com.example.finalproject;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FavouriteFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FavouriteFragment extends Fragment {
    private static final String ARG_PARAM1 = "isTablet";
    private static final String ARG_PARAM2 = "Title";
    private static final String ARG_PARAM3 = "Date";
    private static final String ARG_PARAM4 = "Url";
    private static final String ARG_PARAM5 = "Desc";

    private Boolean mParam1;
    private String mParam2;
    private String mParam3;
    private String mParam4;
    private String mParam5;
    private Bitmap spacePicture=null;

    public FavouriteFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static FavouriteFragment newInstance(Boolean param1, String param2, String param3, String param4, String param5) {
        FavouriteFragment fragment = new FavouriteFragment();
        Bundle args = new Bundle();
        args.putBoolean(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        args.putString(ARG_PARAM3, param3);
        args.putString(ARG_PARAM4, param4);
        args.putString(ARG_PARAM4, param5);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getBoolean(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            mParam3 = getArguments().getString(ARG_PARAM3);
            mParam4 = getArguments().getString(ARG_PARAM4);
            mParam5 = getArguments().getString(ARG_PARAM5);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View newView =  inflater.inflate(R.layout.fragment_favourite, container, false);
        TextView titleFrag = newView.findViewById(R.id.FragTitle);
        TextView dateFrag = newView.findViewById(R.id.FragDate);
        TextView descFrag = newView.findViewById(R.id.FragDesc);
        ImageView imgFrag = newView.findViewById(R.id.SpaceImage);
        Button toWebpageBtn = newView.findViewById(R.id.ToImageBtn);

        // downloads image from link
        if (!fileExistance(mParam2 + ".png")) {
            try {
                downloadIcon();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        FileInputStream fis = null;
        try {    fis = getActivity().openFileInput(mParam2 + ".png");   }
        catch (FileNotFoundException e) {    e.printStackTrace();  }
        spacePicture = BitmapFactory.decodeStream(fis);

        titleFrag.setText(mParam2);
        dateFrag.setText(mParam3);
        descFrag.setText(mParam5);
        imgFrag.setImageBitmap(spacePicture);

        descFrag.setMovementMethod(new ScrollingMovementMethod());

        toWebpageBtn.setOnClickListener(click->{
            Intent toWebpage = new Intent(Intent.ACTION_VIEW);
            toWebpage.setData(Uri.parse(mParam4));
            startActivity(toWebpage);
        });
        return newView;
    }

    /*
     * Downloads the icon
     * @param    null
     * @return   void
     * @see      null
     **/
    private void downloadIcon() throws InterruptedException {
        try {
            Bitmap image = null;
            // URL connection
            URL imageUrl = new URL(mParam4);
            HttpURLConnection imageConnection = (HttpURLConnection) imageUrl.openConnection();
            imageConnection.connect();
            int responseCode = imageConnection.getResponseCode();
            if (responseCode == 200) {
                image = BitmapFactory.decodeStream(imageConnection.getInputStream());
            }
            // Saves the picture
            FileOutputStream outputStream = getActivity().openFileOutput(mParam2 + ".png", Context.MODE_PRIVATE);
            image.compress(Bitmap.CompressFormat.PNG, 80, outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
        }
    }

    /*
     * Checks if files exists
     * @param    fname      The file name to check
     * @return   boolean    Returns if the file exists or not
     * @see      null
     **/
    public boolean fileExistance(String fname){
        File file = getActivity().getBaseContext().getFileStreamPath(fname);
        return file.exists();
    }
}