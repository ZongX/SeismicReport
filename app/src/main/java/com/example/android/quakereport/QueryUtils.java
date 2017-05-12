package com.example.android.quakereport;

import android.support.annotation.NonNull;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

public final class QueryUtils {
    private QueryUtils() {
    }

    public static final String LOG_TAG = QueryUtils.class.getSimpleName();

    private static URL createURL (String stringURL){
        URL url = null;
        try{
            url = new URL(stringURL);
        }
        catch (MalformedURLException badURL){
            Log.e(LOG_TAG, "Bad URL");
        }
        return url;
    }

    public static String makeHTTPRequest (URL url) throws IOException {
        String json_response = "";

        if (url == null){
            return json_response;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        try{
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200){
                inputStream = urlConnection.getInputStream();
                json_response = readFromStream(inputStream);
            }
            else {
                Log.e(LOG_TAG, "Error with getting response: " + urlConnection.getResponseCode());
            }
        } catch (IOException exception){
            Log.e(LOG_TAG, "Problem retrieving earthquake JSON results");
        } finally {
            if (urlConnection != null){
                urlConnection.disconnect();
            }
            if (inputStream != null){
                inputStream.close();
            }
        }
        return json_response;
    }

    @NonNull
    private static String readFromStream (InputStream inputStream) throws IOException{
        StringBuilder output = new StringBuilder();
        if (inputStream != null){
            InputStreamReader streamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader bufferedReader = new BufferedReader(streamReader);
            String line = bufferedReader.readLine();
            while (line != null){
                output.append(line);
                line = bufferedReader.readLine();
            }
        }
        return output.toString();
    }

    public static ArrayList<EarthquakeItem> extractEarthquakes(String json_response) {
        ArrayList<EarthquakeItem> earthquakes = new ArrayList<>();
        try {
            JSONObject root = new JSONObject(json_response);
            JSONArray featuresArray = root.getJSONArray("features");
            for (int i = 0; i < featuresArray.length(); i++){
                JSONObject currentObj = featuresArray.getJSONObject(i);
                JSONObject properties = currentObj.getJSONObject("properties");
                double mag = properties.getDouble("mag");
                String place = properties.getString("place");
                long timeInMS = properties.getLong("time");
                String URL = properties.getString("url");
                EarthquakeItem newEarthObj = new EarthquakeItem(mag, place, timeInMS, URL);
                earthquakes.add(newEarthObj);
            }

        } catch (JSONException e) {
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
        }
        return earthquakes;
    }

    public static ArrayList<EarthquakeItem> fetchEarthquakeData (String requestURL){
        ArrayList<EarthquakeItem> earthquakeList = null;
        try{
            String json_response = makeHTTPRequest(createURL(requestURL));
            earthquakeList = extractEarthquakes(json_response);
        } catch (IOException exception){
            Log.e(LOG_TAG, "IOException Error", exception);
        }
        return earthquakeList;
    }
}