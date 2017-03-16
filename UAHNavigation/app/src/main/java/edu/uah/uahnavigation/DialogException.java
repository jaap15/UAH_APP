package edu.uah.uahnavigation;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import edu.uah.uahnavigation.SplashScreenActivity;

import java.util.Arrays;

/**
 * Created by Daniel on 3/15/2017.
 */

public class DialogException  {
    public DialogException(final Context context, String title, String body, String[] buttons) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setMessage(body).setTitle(title);

        // Exit button will close the application
        if (stringContainsItemFromList("Exit", buttons)) {
            builder.setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    System.exit(0);
                }
            });
        }

        // Cancel button will close the dialog
        if (stringContainsItemFromList("Cancel", buttons)) {
            builder.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User clicked OK button
                }
            });
        }

        if (stringContainsItemFromList("Retry", buttons)) {
            builder.setNegativeButton("Retry", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User cancelled the dialog
                    if (context.toString().equals("edu.uah.uahnavigation.SplashScreenActivity@c83c008")) {
                        new SplashScreenActivity().StartApplication();
                    }
                }
            });
        }
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public static boolean stringContainsItemFromList(String inputStr, String[] items) {
        for(int i =0; i < items.length; i++) {
            if(inputStr.contains(items[i])) {
                return true;
            }
        }
        return false;
    }
}
