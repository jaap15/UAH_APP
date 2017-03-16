package edu.uah.uahnavigation;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class SplashScreenActivity extends AppCompatActivity {

    private boolean fakeInternetConnection;
    private Thread downloadThread;
    private SharedPreferences settings;
    private AlertDialog.Builder builder;
    private AlertDialog alert;
    private boolean hasInternet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        downloadThread = new Thread() {
            public void run() {
                Log.d("myMessage", "Scraping");
                String URL = "http://www.uah.edu/cgi-bin/schedule.pl?file=sprg2017.html&segment=NDX";
                Webscraper.Semester s = new Webscraper.Semester(URL, "Spring");
                Webscraper scraper = new Webscraper(getApplicationContext(),"http://www.uah.edu", "/cgi-bin/schedule.pl?file=sprg2017.html&segment=NDX", "Webscrape_Resources");
                scraper.setSemesterToScrape(s);
                scraper.scrapeSemester();
            }
        };

        final String PREFS_NAME = "MyPrefsFile";

        settings = getSharedPreferences(PREFS_NAME, 0);


        // Alert Dialog code (mostly copied from the Android docs
        builder = new AlertDialog.Builder(this);
        builder.setMessage("Click retry to connect to the internet or exit to close the application").setTitle("No Internet Connection");
        builder.setNegativeButton("Retry", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                StartApplication();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        StartApplication();
                    }
                }, 500);
            }
        });

        builder.setPositiveButton("Exit", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                StartApplication();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        System.exit(0);
                    }
                }, 500);
            }
        });
        alert = builder.create();

        if (Util.getProperty("CHECK_INTERNET", this).equals("true")) {
            hasInternet = NetworkManager.hasInternetConnection();
        } else {
            hasInternet = true;
        }


        StartApplication();
    }

    public void StartApplication() {
        if (hasInternet) {
            if (settings.getBoolean("my_first_time", true)) {
                Log.d("myMessage", "First time");

                downloadThread.start();

                settings.edit().putBoolean("my_first_time", false).commit();
            }
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    try {
                        downloadThread.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    startActivity(new Intent(SplashScreenActivity.this, MainActivity.class));
                    finish();
                }
            }, 4000);
        } else {
            alert.show();
        }
    }
}
