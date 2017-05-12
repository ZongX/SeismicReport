/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.quakereport;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import static com.example.android.quakereport.QueryUtils.fetchEarthquakeData;

public class EarthquakeActivity extends AppCompatActivity {

    public static final String LOG_TAG = EarthquakeActivity.class.getName();
    public static final String query_request_url = "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&eventtype=earthquake&orderby=time&minmag=6&limit=10";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);

        EarthquakeHTTPTask earthquakeHTTPTask = new EarthquakeHTTPTask();
        earthquakeHTTPTask.execute(query_request_url);

    }

    private class EarthquakeHTTPTask extends AsyncTask<String, Void, ArrayList<EarthquakeItem>>{
        @Override
        protected ArrayList<EarthquakeItem> doInBackground(String... URLs) {
            if (URLs.length < 1 || URLs[0] == null){
                return null;
            }
            ArrayList<EarthquakeItem> returnThisList = fetchEarthquakeData (URLs[0]);
            return returnThisList;
        }

        @Override
        protected void onPostExecute(ArrayList<EarthquakeItem> earthquakeList) {
            updateUI(earthquakeList);
        }
    }

    public void updateUI (ArrayList<EarthquakeItem> earthquakeList){
        ListView listView = (ListView) findViewById(R.id.list);
        final EarthquakeAdapter adapter = new EarthquakeAdapter(this, earthquakeList);
        listView.setAdapter(adapter);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                EarthquakeItem currentEarthquake = adapter.getItem(i);
                Uri earthquakeUri = Uri.parse(currentEarthquake.getUrl());
                Intent websiteIntent = new Intent (Intent.ACTION_VIEW, earthquakeUri);
                startActivity(websiteIntent);
                return true;
            }
        });
    }
}
