package com.example.finalproject;

import android.widget.Space;

/*
* This is the class for the listview to use
* */
public class SpaceImage {
    private long _id;
    private String title, date, desc, imgLink, imgHD;

    SpaceImage(String title, String date){
        this.title=title;
        this.date=date;
    }

    SpaceImage(Long _id, String title, String date, String desc, String imgLink){
        this._id=_id;
        this.title=title;
        this.date=date;
        this.desc=desc;
        this.imgLink=imgLink;
    }

    public Long getId() { return _id; }

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }

    public String getDesc() { return desc;}

    public String getImgLink() { return imgLink; }
}
