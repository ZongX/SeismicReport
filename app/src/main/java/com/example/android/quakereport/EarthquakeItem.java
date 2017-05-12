package com.example.android.quakereport;

/**
 * Created by EliteBook 8570w on 5/4/2017.
 */

public class EarthquakeItem {
    private double mMag = 0;
    private String mCity = "";
    private long mDate_long = 0;
    private String mUrl = "";


    public EarthquakeItem(double mag, String city, long date, String url){
        mMag = mag;
        mCity = city;
        mDate_long = date;
        mUrl = url;
    }

    public double getMag (){
        return mMag;
    }

    public String getCity (){
        return mCity;
    }

    public long getDateLong () {
        return mDate_long;
    }

    public String getUrl (){
        return mUrl;
    }



}
