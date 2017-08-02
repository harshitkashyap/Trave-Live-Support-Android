package com.example.harshit.awesome;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Harshit on 7/27/2017.
 */

public class Fetcher {

    ArrayList<LatLng> MarkerPoints;
    GoogleMap mMap;
    Context mContext;
    List<LatLng> pointList;
    Boolean isDeviation;


    public Fetcher(ArrayList<LatLng> MarkerPoints, GoogleMap mMap, Context mContext,Boolean isDeviation){

        this.MarkerPoints=MarkerPoints;
        this.mMap=mMap;
        this.mContext=mContext;
        this.isDeviation=isDeviation;
    }

    public void setMarker(LatLng mPoint){


        if (MarkerPoints.size() > 1 ) {
            MarkerPoints.clear();
            if(!isDeviation) {
                mMap.clear();
            }

        }


        MarkerPoints.add(mPoint);


        MarkerOptions options = new MarkerOptions();


        options.position(mPoint);


        if (MarkerPoints.size() == 1) {
            options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
        } else if (MarkerPoints.size() == 2) {
            if(isDeviation) {
                options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
            }
            else{
                options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
            }
        }



        mMap.addMarker(options);



        if (MarkerPoints.size() >= 2) {
            LatLng origin = MarkerPoints.get(0);
            LatLng dest = MarkerPoints.get(1);


            String url = getUrl(origin, dest);
            Log.d("onMapClick", url.toString());
            FetchUrl fetchUrl = new FetchUrl(mContext,mMap,MarkerPoints,isDeviation);




            fetchUrl.execute(url);

            pointList=fetchUrl.getList();



            mMap.moveCamera(CameraUpdateFactory.newLatLng(origin));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(11));
        }


    }


    private String getUrl(LatLng origin, LatLng dest) {


        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;


        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;



        String sensor = "sensor=false";


        String parameters = str_origin + "&" + str_dest + "&" + sensor;


        String output = "json";


        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;


        return url;
    }


    public String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);


            urlConnection = (HttpURLConnection) url.openConnection();


            urlConnection.connect();


            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();
            Log.d("downloadUrl", data.toString());
            br.close();

        } catch (Exception e) {
            Log.d("Exception", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    public List<LatLng> getList(){

        return this.pointList;
    }
}
