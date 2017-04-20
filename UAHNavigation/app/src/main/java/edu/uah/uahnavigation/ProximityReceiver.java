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

        if (entering) {
            Toast.makeText(context, "Entering proximimty alert", Toast.LENGTH_SHORT).show();
            Log.d("PROX", "entering");
        } else {
            Toast.makeText(context, "Exiting proximimty alert", Toast.LENGTH_SHORT).show();
            Log.d("PROX", "exiting");
        }

        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification myNotication = createNotification(context);

        manager.notify(NOTIFICATION_ID, myNotication);
    }

    private Notification createNotification(Context context) {
        Notification.Builder builder = new Notification.Builder(context);

        builder.setAutoCancel(false);
        builder.setTicker("This is ticker text");
        builder.setContentTitle("View the floor plan of this building");
        builder.setContentText("Detected proximity to building");
        builder.setSmallIcon(R.drawable.arrow_down);

        builder.setOngoing(true);
        builder.setSubText("View the floor plan of this building");
        builder.setNumber(100);
        builder.build();

        return builder.getNotification();
    }
}