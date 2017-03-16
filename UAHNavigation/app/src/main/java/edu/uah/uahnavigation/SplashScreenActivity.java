package edu.uah.uahnavigation;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        final Thread downloadThread = new Thread() {
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

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);

        if (settings.getBoolean("my_first_time", true)) {
            Log.d("myMessage", "First time");

            downloadThread.start();

            settings.edit().putBoolean("my_first_time", false).commit();
        }

        Thread checkInternet = new Thread(){
            public void run() {
                Log.d("myMessage", "Connection: " + NetworkManager.hasInternetConnection());
            }
        };

        checkInternet.start();

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
    }
}
