package edu.uah.uahnavigation;


import android.content.Context;
import android.content.res.AssetManager;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by Jairo on 2/26/2017.
 */

public class Util {

    public static String getProperty(String key, Context context)
    {
        try {
            Properties properties = new Properties();
            AssetManager assetManager = context.getAssets();
            System.out.print(BuildConfig.CONFIG);
            InputStream inputStream = assetManager.open(BuildConfig.CONFIG);
            System.out.print("Debug 0");
            properties.load(inputStream);
            System.out.print("Debug 1");
            return properties.getProperty(key);
        } catch (IOException e)
        {
            e.printStackTrace();
        }

        return null;

    }

    public static String getAbout()
    {
        return "The UAH Navigation Application gives users the luxury of having a refinement" +
                " of the Google Maps API that will lead them to their desired location within " +
                "the UAH Campus. The Application will allow users to search for their destination " +
                "by selecting the Class ID, Room Number, or Building ID, and will lead the user to " +
                "the room. When the user enters a building, the app will open up a floor plan with " +
                "a traced path to lead the users to their destination. This Application will focus " +
                "on development for Android-compatible devices.";
    }

    public static String getContactUs()
    {
        return "Contact us at: \nUAH.NAVIGATION.APP@gmail.com";
    }
}
