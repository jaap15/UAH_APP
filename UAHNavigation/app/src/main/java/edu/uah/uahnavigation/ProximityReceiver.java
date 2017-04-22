package edu.uah.uahnavigation;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import static android.content.ContentValues.TAG;

/**
 * Created by Daniel on 4/5/2017.
 */

public class ProximityReceiver extends BroadcastReceiver {

    private static final int NOTIFICATION_ID = 1000;

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
        Log.d("PROX", "buildingname" + buildingName);

        if (entering) {
            Toast.makeText(context, "Entering proximity alert", Toast.LENGTH_SHORT).show();
            NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            Notification myNotication = createNotification(context, buildingName);
            manager.notify(NOTIFICATION_ID, myNotication);
            goToInterior(context, intent);
            Log.d("PROX", "entering");
        } else {
            Toast.makeText(context, "Exiting proximity alert", Toast.LENGTH_SHORT).show();
            Log.d("PROX", "exiting");
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
        i.putExtra("source", "E102");
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
}