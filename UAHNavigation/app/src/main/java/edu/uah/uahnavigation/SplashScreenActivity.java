package edu.uah.uahnavigation;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.uah.model.Buildings;
import edu.uah.model.Courses;
import edu.uah.model.Rooms;
import edu.uah.model.Majors;

public class SplashScreenActivity extends AppCompatActivity {

    DatabaseSource dbSource;
    List<Buildings> buildings;
    List<Rooms> rooms;
    private SharedPreferences settings;
    private AlertDialog.Builder builder;
    private AlertDialog alert;
    private boolean hasInternet;
    List<Majors> majors;
    List<Courses> courses;

    private TextView loadingView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        loadingView = (TextView) findViewById(R.id.loadingSplashTextView);

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

                majors = dbSource.GetFromMajors(null,null,null);
                if (majors.size() == 0) {
                    dbSource.InsertIntoMajors("Select_Major", "");
                }
                courses = dbSource.GetFromCourses(null, null, null);
                if (courses.size() == 0) {
                    dbSource.InsertIntoCourses("Select_Major", "NULL", "Select_Room", "99999", "Select_Course Select_Section", "NULL", "9", "NULL", "NULL", "NULL", "NULL");
                }

                buildings = dbSource.GetFromBuildings(null, null, null);
                if (buildings.size() == 0) {
                    createBuildingsData();
                }

                rooms = dbSource.GetFromRooms(null, null, null);
                if (rooms.size() == 0) {
                    createRoomsData();
                }

                dbSource.close();

                Webscraper scraper = new Webscraper(getApplicationContext(),"http://www.uah.edu", "/cgi-bin/schedule.pl?", "Webscrape_Resources");
                AsyncTaskRunnerSemesters AllSemestersRunner = new AsyncTaskRunnerSemesters(scraper);
                AllSemestersRunner.execute("Tmp");


                settings.edit().putBoolean("my_first_time", false).commit();
            }
            else
            {
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);

            }
        } else {
            alert.show();
        }
    }

    private class AsyncTaskRunnerSemesters extends AsyncTask<String, String, String> {

        private String[] semesters;
        private Webscraper scraper;
        private android.app.AlertDialog myDialog;
        private String selectedItem;
        private String[] currentSemesters;
        private Thread downloadThread;
        private ArrayList<Webscraper.Semester> cSemesters;
        private int choice;

        public AsyncTaskRunnerSemesters(Webscraper scraper)
        {
            this.scraper = scraper;
        }
        @Override
        protected String doInBackground(String... params) {

            downloadThread = new Thread() {
                public void run() {
                    Log.d("myMessage", "Scraping All semesters");
                    scraper.scrapePossibleSemesters();
                    cSemesters = scraper.getPossibleSemesters();
                    currentSemesters = new String[cSemesters.size()];
                    for(int i = 0; i < cSemesters.size();i++)
                    {
                        currentSemesters[i] = cSemesters.get(i).getSemesterName();
                    }
                }
            };

            downloadThread.start();

            return "test";
        }

        @Override
        protected void onPostExecute(String result) {

            try {
                downloadThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(SplashScreenActivity.this);

            builder.setTitle("Choose a Semester");

            builder.setSingleChoiceItems(currentSemesters, -1, new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            selectedItem = Arrays.asList(currentSemesters).get(i);
                            choice = i;

                        }
                    });
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Toast.makeText(getApplicationContext(),selectedItem,Toast.LENGTH_LONG).show();
                    scraper.setSemesterToScrape(cSemesters.get(choice));
                    AsyncTaskRunner semesterRunner = new AsyncTaskRunner(scraper);
                    semesterRunner.execute("Tmp");
                }
            });

            android.app.AlertDialog dialog = builder.create();
            dialog.show();
        }


        @Override
        protected void onPreExecute() {
        }


        @Override
        protected void onProgressUpdate(String... text) {

        }
    }

    private class AsyncTaskRunner extends AsyncTask<String, String, String>{

        private Webscraper scraper;

        public AsyncTaskRunner(Webscraper scraper)
        {
            Log.d("myMessage", "Constructor of semester runner");
            this.scraper = scraper;
            Log.d("myMessage", "Scraper");
        }

        @Override
        protected String doInBackground(String... params) {
            Log.d("myMessage", "Scraping");
            scraper.scrapeSemester();
            CoursesListParser cp = new CoursesListParser(getApplicationContext(), scraper.getSemesterToScrape().getSemesterName());
            publishProgress("Populating Database");
            cp.parseSemester();
            return "test";
        }

        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(getApplicationContext(), "App ready for use!",Toast.LENGTH_LONG).show();
            startActivity(new Intent(SplashScreenActivity.this, MainActivity.class));
            finish();
        }


        @Override
        protected void onPreExecute() {
            loadingView.setText("Initializing First-time Use");
        }


        @Override
        protected void onProgressUpdate(String... text) {
            loadingView.setText(text[0]);

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
