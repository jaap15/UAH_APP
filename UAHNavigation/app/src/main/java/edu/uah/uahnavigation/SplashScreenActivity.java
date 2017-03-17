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

import java.util.List;

import edu.uah.model.Buildings;
import edu.uah.model.Rooms;

public class SplashScreenActivity extends AppCompatActivity {

    DatabaseSource dbSource;
    List<Buildings> buildings;
    List<Rooms> rooms;
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
                CoursesListParser cp = new CoursesListParser(getApplicationContext(), "Spring");
                cp.parseSemester();
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

                dbSource = new DatabaseSource(this);
                dbSource.open();

                buildings = dbSource.GetFromBuildings(null, null, null);
                if (buildings.size() == 0) {
                    createBuildingsData();
                }

                rooms = dbSource.GetFromRooms(null, null, null);
                if (rooms.size() == 0) {
                    createRoomsData();
                }

                dbSource.close();

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
                        Log.d("myMessage", "DownloadThread Exception");
                        e.printStackTrace();
                    }
                    Log.d("myMessage", "Switch to Main Activity");
                    startActivity(new Intent(SplashScreenActivity.this, MainActivity.class));
                    finish();
                }
            }, 4000);
        } else {
            alert.show();
        }
    }

    private void createBuildingsData() {
        BuildingsPullParser parser = new BuildingsPullParser();
        List<Buildings> buildings = parser.parseXML(this);

        for (Buildings building : buildings) {
            try {
                dbSource.InsertIntoBuildings(building);
            } catch (final IndexOutOfBoundsException e) {
                new DialogException(this, "IndexOutOfBoundsException", "Error inserting into Buildings : " + e.getMessage(), new String[]{"Exit"});
            }
        }
    }

    private void createRoomsData() {
        RoomsPullParser parser = new RoomsPullParser();
        List<Rooms> rooms = parser.parseXML(this);

        for (Rooms room : rooms) {
            try {
                dbSource.InsertIntoRooms(room);
            } catch (final IndexOutOfBoundsException e) {
                new DialogException(this, "IndexOutOfBoundsException", "Error inserting into Rooms : " + e.getMessage(), new String[]{"Exit"});
            }
        }
    }

}
