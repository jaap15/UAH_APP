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
}
