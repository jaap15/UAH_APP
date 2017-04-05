package edu.uah.uahnavigation;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import static android.content.ContentValues.TAG;

/**
 * Created by Daniel on 4/5/2017.
 */

public class ProximityReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context arg0, Intent intent) {
        if(intent.getData() != null)
            Log.v(TAG, intent.getData().toString());
        Bundle extras = intent.getExtras();
        if(extras != null) {
            Log.v("PROX", "Message: " + extras.getString("message"));
            Log.v("PROX", "Entering? " + extras.getBoolean(LocationManager.KEY_PROXIMITY_ENTERING));
        }
    }
}