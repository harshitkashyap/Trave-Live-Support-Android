package com.example.harshit.awesome;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * Created by Harshit on 7/27/2017.
 */

class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

    Context mContext;
    GoogleMap mMap;
    List<LatLng> pointList;
    Boolean isDeviation;
    public ParserTask(Context mContext, GoogleMap mMap, Boolean isDeviation){

        this.mContext=mContext;
        this.mMap=mMap;
        this.isDeviation=isDeviation;
    }

    // Parsing the data in non-ui thread
    @Override
    protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

        JSONObject jObject;
        List<List<HashMap<String, String>>> routes = null;

        try {
            jObject = new JSONObject(jsonData[0]);
            Log.d("ParserTask",jsonData[0].toString());
            DataParser parser = new DataParser(mContext,isDeviation);


            Log.d("ParserTask", parser.toString());

            // Starts parsing data
            routes = parser.parse(jObject);

            pointList=parser.getList();



            Log.d("ParserTask","Executing routes");
            Log.d("ParserTask",routes.toString());

        } catch (Exception e) {
            Log.d("ParserTask",e.toString());
            e.printStackTrace();
        }
        return routes;
    }
    protected  void onProgressUpdate(Integer... help){


    }

    // Executes in UI thread, after the parsing process
    @Override
    protected void onPostExecute(List<List<HashMap<String, String>>> result) {
        ArrayList<LatLng> points;
        PolylineOptions lineOptions = null;


        for (int i = 0; i < result.size(); i++) {
            points = new ArrayList<>();
            lineOptions = new PolylineOptions();


            List<HashMap<String, String>> path = result.get(i);


            for (int j = 0; j < path.size(); j++) {
                HashMap<String, String> point = path.get(j);

                double lat = Double.parseDouble(point.get("lat"));
                double lng = Double.parseDouble(point.get("lng"));
                LatLng position = new LatLng(lat, lng);

                points.add(position);
            }


            lineOptions.addAll(points);
            lineOptions.width(10);
            if(!isDeviation) {
                lineOptions.color(Color.BLUE);
            }
            else{
                lineOptions.color(Color.GREEN);
            }
            Log.d("onPostExecute","onPostExecute lineoptions decoded");

        }


        if(lineOptions != null) {
            mMap.addPolyline(lineOptions);
        }
        else {
            Log.d("onPostExecute","without Polylines drawn");
        }
    }
    public List<LatLng> getList(){

        return pointList;
    }
}
