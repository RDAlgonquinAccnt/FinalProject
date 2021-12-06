package com.example.finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
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

public class ImageFinder extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{
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

        //------------------- TOOLBAR -------------------\\
        androidx.appcompat.widget.Toolbar myToolBar = (androidx.appcompat.widget.Toolbar) findViewById(R.id.FinderTool);
        setSupportActionBar(myToolBar);
        myToolBar.setBackgroundColor(Color.parseColor("#7733ff"));
        //------------------- NAVBAR -------------------\\
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.FinderDrawer);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, myToolBar, R.string.openNav, R.string.closeNav);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView=(NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        /*
        * Retrieves image data from the API
        * The text fields for day and month must be either single or double digit
        * and the field for the year must be four digits
        * @param    click   The lambda variable of the click
        * @return   void
        * @see      image data
        * */
        fetchImage.setOnClickListener(click -> {
            // get edit field values and set it equal
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

        /*
         * saves image data on click
         * @param    click   The lambda variable of the click
         * @return   void
         * @see      snackbar
        **/
        spaceView.setOnClickListener(click ->{
            // make snackbar
            long curId = addData();
            viewData();
            Log.e("saved",title);
            Snackbar snackbar = Snackbar.make(spaceView, getApplicationContext().getResources().getString(R.string.saved)+title, Snackbar.LENGTH_LONG);
            snackbar.show();
            snackbar.setAction("Undo", clickSnack ->{
                // "deletes" the recently saved data.\
                deleteData(curId);
                viewData();
                Log.e("deleted",title);
            });
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
                        .setMessage(getApplicationContext().getResources().getString(R.string.finder_help))
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
     * class for obtaining the API data
     * extends to the AsyncTask interface
     */
    private class QueryImageAPI extends AsyncTask<String, Integer, String> {
        private Bitmap spacePicture=null;

        /*
         * Fetches the data in the background
         * The strings[0] must contain a valid api
         * @param    strings   URL for fetching the API call
         * @return   null
         * @see      null
         **/
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

        /*
         * Updates the progress bar
         * @param    args   The progress that the progress bar is suppose to go up
         * @return   void
         * @see      Progress Bar updates
         **/
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

        /*
         * Changes the data and image on the page
         * @param    fromDoInBackground
         * @return   void
         * @see      Updated screen
         **/
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

        /*
         * Downloads the icon
         * @param    null
         * @return   void
         * @see      null
         **/
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

        /*
         * Checks if files exists
         * @param    fname      The file name to check
         * @return   boolean    Returns if the file exists or not
         * @see      null
         **/
        public boolean fileExistance(String fname){
            File file = getBaseContext().getFileStreamPath(fname);
            return file.exists();
        }
    }

    private long addData (){
        SpaceOpener dbOpener = new SpaceOpener(this);
        SQLiteDatabase db = dbOpener.getWritableDatabase();

        ContentValues newRow = new ContentValues();

        //inputs current data into contentvalues object
        newRow.put(SpaceOpener.COL_TITLE, title);
        newRow.put(SpaceOpener.COL_DATE, dateString);
        newRow.put(SpaceOpener.COL_DESCRIPTION, description);
        newRow.put(SpaceOpener.COL_URL, spaceUrl);

        long newId = db.insert(SpaceOpener.TABLE_NAME, null, newRow);
        return newId;
    }

    private long deleteData (long curId){
        SpaceOpener dbOpener = new SpaceOpener(this);
        SQLiteDatabase db = dbOpener.getWritableDatabase();

        return db.delete(SpaceOpener.TABLE_NAME, SpaceOpener.COL_ID+"= ?", new String[] {Long.toString(curId)});
    }

    private void viewData (){
        SpaceOpener dbOpener = new SpaceOpener(this);
        SQLiteDatabase db = dbOpener.getWritableDatabase();

        String [] columns = {SpaceOpener.COL_ID, SpaceOpener.COL_TITLE, SpaceOpener.COL_DATE,
                SpaceOpener.COL_URL, SpaceOpener.COL_DESCRIPTION};
        // query didn't work, so just used rawquery
        Cursor c = db.rawQuery("select * from " + SpaceOpener.TABLE_NAME,null);

        //gets column indexes
        int idColIndex = c.getColumnIndex(SpaceOpener.COL_ID);
        int titleColIndex = c.getColumnIndex(SpaceOpener.COL_TITLE);

        while (c.moveToNext()){
            long id = c.getLong(idColIndex);
            String curTitle = c.getString(titleColIndex);

            Log.e(Long.toString(id), curTitle);

        }
    }
}