package com.example.harshit.awesome;

import android.content.Context;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.PolyUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Harshit on 7/28/2017.
 */

public class Return {
    LatLng cPoint;
    LatLng dPoint;
    List<LatLng> pList;
    List<LatLng> firstList;
    List<LatLng> secondList;
    List<LatLng> finalList;


    public void reDirect(GoogleMap mMap,Context mContext,ArrayList<LatLng> MarkerPoints){


        cPoint= MapsActivity.point;
        pList=MapsActivity.points;

        dPoint=findNearestPoint(cPoint,pList);
        MarkerOptions options = new MarkerOptions();



        Fetcher first=new Fetcher(MarkerPoints,mMap,mContext,true);
        first.setMarker(cPoint);
        first.setMarker(dPoint);




        MapsActivity.points=MapsActivity.dPoints;

            Toast.makeText(mContext, "RE-ROUTED", Toast.LENGTH_SHORT).show();



















    }

    private LatLng findNearestPoint(LatLng test, List<LatLng> target) {
        double distance = -1;
        LatLng minimumDistancePoint = test;

        if (test == null || target == null) {
            return minimumDistancePoint;
        }

        for (int i = 0; i < target.size(); i++) {
            LatLng point = target.get(i);

            int segmentPoint = i + 1;
            if (segmentPoint >= target.size()) {
                segmentPoint = 0;
            }

            double currentDistance = PolyUtil.distanceToLine(test, point, target.get(segmentPoint));
            if (distance == -1 || currentDistance < distance) {
                distance = currentDistance;
                minimumDistancePoint = findNearestPoint(test, point, target.get(segmentPoint));
            }
        }

        return minimumDistancePoint;
    }

    private LatLng findNearestPoint(final LatLng p, final LatLng start, final LatLng end) {
        if (start.equals(end)) {
            return start;
        }

        final double s0lat = Math.toRadians(p.latitude);
        final double s0lng = Math.toRadians(p.longitude);
        final double s1lat = Math.toRadians(start.latitude);
        final double s1lng = Math.toRadians(start.longitude);
        final double s2lat = Math.toRadians(end.latitude);
        final double s2lng = Math.toRadians(end.longitude);

        double s2s1lat = s2lat - s1lat;
        double s2s1lng = s2lng - s1lng;
        final double u = ((s0lat - s1lat) * s2s1lat + (s0lng - s1lng) * s2s1lng)
                / (s2s1lat * s2s1lat + s2s1lng * s2s1lng);
        if (u <= 0) {
            return start;
        }
        if (u >= 1) {
            return end;
        }

        return new LatLng(start.latitude + (u * (end.latitude - start.latitude)),
                start.longitude + (u * (end.longitude - start.longitude)));


    }

    private List<LatLng> getFinal(List<LatLng> firstL, List<LatLng> secondList){

        for(int i=0;i<secondList.size();i++){

            firstL.add(secondList.get(i));


        }

        return firstL;
    }




}
