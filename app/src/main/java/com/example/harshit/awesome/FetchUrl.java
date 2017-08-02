package com.example.harshit.awesome;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Harshit on 7/27/2017.
 */

class FetchUrl extends AsyncTask<String, Void, String> {
    Context mContext;
    GoogleMap mMap;
    ArrayList<LatLng> markerPoints;
    List<LatLng> pointList;
    Boolean isDeviation;

    public FetchUrl(Context mContext, GoogleMap mMap, ArrayList<LatLng> markerPoints,Boolean isDeviation){

        this.mContext=mContext;
        this.mMap=mMap;
        this.markerPoints=markerPoints;
        this.isDeviation=isDeviation;

    }

    @Override
    protected String doInBackground(String... url) {


        String data = "";

        try {

            Fetcher fetch=new Fetcher(markerPoints,mMap,mContext,isDeviation);
            data = fetch.downloadUrl(url[0]);
            Log.d("Background Task data", data.toString());
        } catch (Exception e) {
            Log.d("Background Task", e.toString());
        }
        return data;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);

        ParserTask parserTask = new ParserTask(mContext,mMap,isDeviation);


        parserTask.execute(result);

        pointList = parserTask.getList();



    }
    public List<LatLng> getList(){

        return pointList;
    }
}
