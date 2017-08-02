package com.example.harshit.awesome;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.google.maps.android.PolyUtil;

import static com.example.harshit.awesome.MapsActivity.MarkerPoints;
import static com.example.harshit.awesome.MapsActivity.mContext;
import static com.example.harshit.awesome.MapsActivity.mMap;

/**
 * Created by Harshit on 8/2/2017.
 */

public class DeviationService extends Service {
    private Handler mHandler = new Handler();

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent,int flags, int startId) {


            mHandler.postDelayed(new Runnable() {
                public void run() {


                    boolean isLocationOnPath = true;
                    double tolerance = 1000; // meters

                    if (MapsActivity.points != null) {
                        if (PolyUtil.isLocationOnPath(MapsActivity.point, MapsActivity.points, true, tolerance)) {

                            isLocationOnPath = true;
                        } else {
                            isLocationOnPath = false;
                        }
                    }


                    startReturn(isLocationOnPath);
                }},5000);


        return START_STICKY;
    }

    @Override
    public void onDestroy() {

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void startReturn(Boolean dev){

        if(dev)
        {

            Toast.makeText(mContext, "ON PATH", Toast.LENGTH_SHORT).show();

        }
        else{
            Return rt=new Return();
            rt.reDirect(mMap,mContext,MarkerPoints);
        }
    }
}
