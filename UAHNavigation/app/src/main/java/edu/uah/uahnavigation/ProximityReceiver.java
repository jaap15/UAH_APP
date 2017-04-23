package edu.uah.uahnavigation;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import static android.content.ContentValues.TAG;

/**
 * Created by Daniel on 4/5/2017.
 */

public class ProximityReceiver extends BroadcastReceiver {

    private static final int NOTIFICATION_ID = 1000;
    private static final String LOGTAG = "WERT";
    private LocationManager locationManager = null;

    @Override
    public void onReceive(Context context, Intent intent) {

        String key = LocationManager.KEY_PROXIMITY_ENTERING;

        Boolean entering = intent.getBooleanExtra(key, false);

        String buildingName = "";
        String newname = "";
        try {
            buildingName = intent.getStringExtra("building");
        } catch (NullPointerException e) {

        }
        Log.d(LOGTAG, "buildingname" + buildingName);

        if (entering) {
            Toast.makeText(context, "Entering proximity alert", Toast.LENGTH_SHORT).show();
            NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            Notification myNotication = createNotification(context, buildingName);
            manager.notify(NOTIFICATION_ID, myNotication);
            goToInterior(context, intent);
        } else {
            Toast.makeText(context, "Exiting proximity alert", Toast.LENGTH_SHORT).show();
        }

    }

    private void goToInterior(Context context, Intent intent) {
        String sourceName = "";
        String destinationName = "";
        String buildingName = "";

        try {
            sourceName = intent.getStringExtra("source").toUpperCase();
        } catch (NullPointerException e) {

        }

        try {
            destinationName = intent.getStringExtra("destination").toUpperCase();
        } catch (NullPointerException e) {
            destinationName = "ENG246";
        }

        try {
            buildingName = intent.getStringExtra("building").toUpperCase();
        } catch (NullPointerException e) {
            buildingName = "ENG";
        }

        Intent i = new Intent(context, InteriorNavigationActivity.class);
        if (buildingName == "ENG") {
            i.putExtra("source", getENGEntrances(context));
        } else if (buildingName == "MSB") {

        }
        i.putExtra("destination", destinationName);
        i.putExtra("building", buildingName);
        context.startActivity(i);
    }

    private Notification createNotification(Context context, String buildingName) {
        Notification.Builder builder = new Notification.Builder(context);

        builder.setAutoCancel(false);
        builder.setTicker("Arrived at " + buildingName);
        builder.setContentTitle("Arrived at " + buildingName);
        builder.setContentText("UAH Navigation");
        builder.setSmallIcon(R.drawable.arrow_down);

        builder.setOngoing(true);
        builder.setSubText("Click to view floorplan");
        builder.setOngoing(false);
        builder.build();

        builder.setLights(Color.WHITE, 300, 1500);

        return builder.build();
    }

    private String getENGEntrances(Context context) {

        Location lctn = null;
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            lctn = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        } else {

        }

        Location[] entrances = new Location[4];
        entrances[0] = new Location("E102");
        entrances[1] = new Location("E121");
        entrances[2] = new Location("E135");
        entrances[3] = new Location("E147");
        entrances[0].setLatitude(34.722765);
        entrances[0].setLongitude(-86.641024);
        entrances[1].setLatitude(34.722984);
        entrances[1].setLongitude(-86.640461);
        entrances[2].setLatitude(34.722840);
        entrances[2].setLongitude(-86.639951);
        entrances[3].setLatitude(34.722335);
        entrances[3].setLongitude(-86.640101);


        int smallestIndex=99;
        if (lctn != null) {
            float[] distances = new float[4];
            distances[0] = lctn.distanceTo(entrances[0]);
            distances[1] = lctn.distanceTo(entrances[1]);
            distances[2] = lctn.distanceTo(entrances[2]);
            distances[3] = lctn.distanceTo(entrances[3]);

            float smallest = Integer.MAX_VALUE;
            for (int i=0; i < 4; i++) {
                if (distances[i] < smallest) {
                    smallest = distances[i];
                    smallestIndex = i;
                }
            }
        }

        return entrances[smallestIndex].getProvider();
    }

    private String getMSBEntrances(Context context) {

        Location lctn = null;
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            lctn = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        } else {

        }

        Location[] entrances = new Location[4];
        entrances[0] = new Location("E102");
        entrances[1] = new Location("E121");
        entrances[2] = new Location("E135");
        entrances[3] = new Location("E147");
        entrances[0].setLatitude(34.722765);
        entrances[0].setLongitude(-86.641024);
        entrances[1].setLatitude(34.722984);
        entrances[1].setLongitude(-86.640461);
        entrances[2].setLatitude(34.722840);
        entrances[2].setLongitude(-86.639951);
        entrances[3].setLatitude(34.722335);
        entrances[3].setLongitude(-86.640101);


        int smallestIndex=99;
        if (lctn != null) {
            float[] distances = new float[4];
            distances[0] = lctn.distanceTo(entrances[0]);
            distances[1] = lctn.distanceTo(entrances[1]);
            distances[2] = lctn.distanceTo(entrances[2]);
            distances[3] = lctn.distanceTo(entrances[3]);

            float smallest = Integer.MAX_VALUE;
            for (int i=0; i < 4; i++) {
                if (distances[i] < smallest) {
                    smallest = distances[i];
                    smallestIndex = i;
                }
            }
        }

        return entrances[smallestIndex].getProvider();
    }
}