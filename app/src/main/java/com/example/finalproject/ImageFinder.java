package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ImageFinder extends AppCompatActivity {
    String apiUrl = "https://api.nasa.gov/planetary/apod?api_key=DgPLcIlnmN0Cwrzcg3e9NraFaYLIDI68Ysc6Zh3d&date=2020-02-01";
    private String dayValue, monthValue, yearValue;
    private String title, description, dateString, spaceUrl;
    private int progression=0;
    private String savedLink="NIL";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_finder);

        Button fetchImage = findViewById(R.id.FetchImage);
        ImageView spaceView = findViewById(R.id.SpaceImage);

        fetchImage.setOnClickListener(click -> {
            // get edit fielf values and set it equal
            EditText dayField = findViewById(R.id.DayValue);
            EditText monthField = findViewById(R.id.MonthValue);
            EditText yearField = findViewById(R.id.YearValue);
            yearValue = yearField.getText().toString();
            monthValue = monthField.getText().toString();
            dayValue = dayField.getText().toString();

            // adds a zero is there is only single digit input
            if (dayValue.length()==1) dayValue="0"+dayValue;
            if (monthValue.length()==1) monthValue="0"+monthValue;

            // concats the string
            String dateUrl = "https://api.nasa.gov/planetary/apod?api_key=DgPLcIlnmN0Cwrzcg3e9NraFaYLIDI68Ysc6Zh3d&date="+yearValue+"-"+monthValue+"-"+dayValue;

            // call api
            QueryImageAPI imageAPI = new QueryImageAPI();
            imageAPI.execute(dateUrl);
        });

        spaceView.setOnClickListener(click ->{
            // make snackbar
            savedLink=spaceUrl;
            Log.e("saved",title);
            Snackbar snackbar = Snackbar.make(spaceView, "Saved: "+title, Snackbar.LENGTH_LONG);
            snackbar.show();
            snackbar.setAction("Undo", clickSnack ->{
                // "deletes" the recently saved data.\
                savedLink="NIL";
                Log.e("deleted",title);
            });
        });


    }


    private class QueryImageAPI extends AsyncTask<String, Integer, String> {
        private Bitmap spacePicture=null;

        @Override
        protected String doInBackground(String... strings) {

            try {
                URL url = new URL(strings[0]);

                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                InputStream response = urlConnection.getInputStream();

                BufferedReader reader = new BufferedReader(new InputStreamReader(response, "UTF-8"), 8);
                StringBuilder sb = new StringBuilder();
                String line;

                // Makes a string
                while ((line = reader.readLine()) != null) sb.append(line + "\n");
                String result = sb.toString();

                // Makes JSON file
                JSONObject imageJson = new JSONObject(result);

                // gets each string
                title = imageJson.getString("title");
                publishProgress(25);
                description = imageJson.getString("explanation");
                publishProgress(25);
                dateString = imageJson.getString("date");
                publishProgress(25);
                spaceUrl = imageJson.getString("url");
                // downloads image from link
                if (!fileExistance(title + ".png")) downloadIcon();
                FileInputStream fis = null;
                try {    fis = openFileInput(title + ".png");   }
                catch (FileNotFoundException e) {    e.printStackTrace();  }
                spacePicture = BitmapFactory.decodeStream(fis);
                publishProgress(25);

                Log.e("url", url.toString());
            }  catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        public void onProgressUpdate(Integer... args) {
            ProgressBar apiFetchProgress = findViewById(R.id.apiDownload);
            apiFetchProgress.setVisibility(View.VISIBLE);
            progression+=args[0]; // adds to progress
            apiFetchProgress.setProgress(progression);
            if (progression>=100) {
                // resets the progression bar when hitting 100%
                progression=0;
                apiFetchProgress.setVisibility(View.INVISIBLE);
            }
        }

        //Type3
        public void onPostExecute(String fromDoInBackground) {
            // sets the fields with the api values
            TextView titleLabel = findViewById(R.id.TitleImage);
            TextView descriptionLabel = findViewById(R.id.DescriptionImage);
            ImageView spaceImage = findViewById(R.id.SpaceImage);

            titleLabel.setText(title);
            descriptionLabel.setText(description);
            descriptionLabel.setMovementMethod(new ScrollingMovementMethod());
            spaceImage.setImageBitmap(spacePicture);
        }

        private void downloadIcon() throws InterruptedException {
            try {
                Bitmap image=null;
                // URL connection
                URL imageUrl = new URL(spaceUrl);
                HttpURLConnection imageConnection = (HttpURLConnection) imageUrl.openConnection();
                imageConnection.connect();
                int responseCode = imageConnection.getResponseCode();
                if (responseCode == 200) {
                    image = BitmapFactory.decodeStream(imageConnection.getInputStream());
                }
                // Saves the picture
                FileOutputStream outputStream = openFileOutput( title + ".png", Context.MODE_PRIVATE);
                image.compress(Bitmap.CompressFormat.PNG, 80, outputStream);
                outputStream.flush();
                outputStream.close();
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
            }
        }

        public boolean fileExistance(String fname){
            File file = getBaseContext().getFileStreamPath(fname);
            return file.exists();
        }
    }

}