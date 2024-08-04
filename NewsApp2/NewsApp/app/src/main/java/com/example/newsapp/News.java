package com.example.newsapp;

import java.util.ArrayList;

public class News {
    private final String mTitle;
    private final String mSection;
    private final String mDateAndTime;
    private final String mUrl;
    private final ArrayList<String> mAuthor;

    public News(String title, String section, String dateAndTime, String url, ArrayList<String> author) {
        mTitle = title;
        mSection = section;
        mDateAndTime = dateAndTime;
        mUrl = url;
        mAuthor = author;
    }
    public String getTitle(){
        return mTitle;
    }
    public String getSection(){
        return mSection;
    }
    public String getDateAndTime(){return mDateAndTime; }
    public ArrayList<String> getAuthorArrayList(){ return mAuthor; }

    public String getUrl() {return mUrl;}
}
