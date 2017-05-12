package com.example.android.quakereport;

import android.app.Activity;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by EliteBook 8570w on 5/4/2017.
 */

public class EarthquakeAdapter extends ArrayAdapter<EarthquakeItem>{

    public EarthquakeAdapter(Activity context, ArrayList<EarthquakeItem> earthQuake){
        super(context,0,earthQuake);
    }

    public View getView (int position, View convertView, ViewGroup parent){
        View listItemView = convertView;
        if (listItemView == null){
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }
        EarthquakeItem currentItem = getItem(position);

        TextView currentMag = (TextView) listItemView.findViewById(R.id.mag);
        TextView currentCity = (TextView) listItemView.findViewById(R.id.city);
        TextView currentDate = (TextView) listItemView.findViewById(R.id.date);
        TextView currentTime = (TextView) listItemView.findViewById(R.id.time);
        TextView currentDistance = (TextView) listItemView.findViewById(R.id.distance);

        //Format and separate the date and time of Earthquake
        Date dateObj = new Date(currentItem.getDateLong());
        SimpleDateFormat dateFormatter = new SimpleDateFormat("MMM dd, yyyy");
        String displayDate = dateFormatter.format(dateObj);

        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
        String displayTime = timeFormat.format(dateObj);

        //Isolate distance between city and city name
        String place = currentItem.getCity();
        String distance = "", nearestCity = "";
        if (place.contains("of")){
            String placeArray [] = place.split("of");
            distance = placeArray[0] + "of";
            nearestCity = placeArray[1];
        } else{
            distance = "Near the ";
            nearestCity = currentItem.getCity();
        }

        //Change the magnitude from double to one decimal place
        double changeThisMag = currentItem.getMag();
        DecimalFormat format = new DecimalFormat("0.0");
        String displayMag = format.format(changeThisMag);

        //Setting the color on the circle
        GradientDrawable magnitudeCircle = (GradientDrawable) currentMag.getBackground();
        int magColor = getMagnitudeColor (currentItem.getMag());

        //Setting the values to the correct views
        magnitudeCircle.setColor(magColor);
        currentMag.setText(displayMag);
        currentCity.setText(nearestCity);
        currentDate.setText(displayDate);
        currentTime.setText(displayTime);
        currentDistance.setText(distance);
        return listItemView;
    }

    private int getMagnitudeColor(double mag) {
        int magColorResId = 0;
        int floorMag = (int) Math.floor(mag);
        switch (floorMag) {
            case 0:
            case 1:
                magColorResId = R.color.magnitude1;
                break;
            case 2:
                magColorResId = R.color.magnitude2;
                break;
            case 3:
                magColorResId = R.color.magnitude3;
                break;
            case 4:
                magColorResId = R.color.magnitude4;
                break;
            case 5:
                magColorResId = R.color.magnitude5;
                break;
            case 6:
                magColorResId = R.color.magnitude6;
                break;
            case 7:
                magColorResId = R.color.magnitude7;
                break;
            case 8:
                magColorResId = R.color.magnitude8;
                break;
            case 9:
                magColorResId = R.color.magnitude9;
                break;
            default:
                magColorResId = R.color.magnitude10plus;
                break;
        }
        return ContextCompat.getColor(getContext(), magColorResId);
    }
}
