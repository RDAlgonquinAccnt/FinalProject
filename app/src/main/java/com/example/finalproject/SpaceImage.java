package com.example.finalproject;

import android.widget.Space;

/*
* This is the class for the listview to use
* */
public class SpaceImage {

    private String title, date;

    SpaceImage(String title, String date){
        this.title=title;
        this.date=date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDate() {
        return date;
    }
}
