package edu.uah.uahnavigation;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        Thread checkInternet = new Thread(){
            public void run() {
//                Toast.makeText(getApplicationContext(), "Internet: "+ NetworkManager.hasInternetConnection(), Toast.LENGTH_LONG).show();
                Log.d("myMessage", "Connection: " + NetworkManager.hasInternetConnection());
            }
        };

        checkInternet.start();

        final Thread downloadThread = new Thread() {
            public void run() {
                String URL = "http://www.uah.edu/cgi-bin/schedule.pl?file=sprg2017.html&segment=NDX";
                Webscraper.Semester s = new Webscraper.Semester(URL, "Spring");
//                Toast.makeText(this, s.getURL() + "  " + s.getSemesterName(), Toast.LENGTH_LONG).show();
                Webscraper scraper = new Webscraper(getApplicationContext(),"http://www.uah.edu", "/cgi-bin/schedule.pl?file=sprg2017.html&segment=NDX", "Webscrape_Resources");
                scraper.setSemesterToScrape(s);
                scraper.scrapeSemester();
            }
        };

        downloadThread.start();

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
