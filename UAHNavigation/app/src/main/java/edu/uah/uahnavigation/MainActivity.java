package edu.uah.uahnavigation;


import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import java.util.List;

import edu.uah.model.*;

import static android.R.id.list;
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    DatabaseSource dbSource;
    private String LOGTAG = "QWER";
    List<Majors> majors;
    List<Courses> courses;
    List<Buildings> buildings;
    List<Rooms> rooms;

    private Courses[] coursesArray;
    private Spinner spinnerMajors;
    private Spinner spinnerCourses;
    private MajorsSpinAdapter adapterMajors;
    private CoursesSpinAdapter adapterCourses;

    private ProgressBar spinner;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("myMessage", BuildConfig.CONFIG);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toast.makeText(this, "Config URL: "+ Util.getProperty("URL", this), Toast.LENGTH_LONG).show();

        final Button classBtn = (Button) findViewById(R.id.classbtn);
        final Button BlgdBtn = (Button) findViewById(R.id.buildingbtn);
        // bClass = (Button) findViewById(R.id.bClass);
        // bBuilding = (Button) findViewById(R.id.bBuilding);
        classBtn.setOnClickListener(this);
        BlgdBtn.setOnClickListener(this);

        dbSource = new DatabaseSource(this);
        if (dbSource.isOpen()) {
            Log.i(LOGTAG, "DATABASE IS OPEN");
        } else {
            Log.i(LOGTAG, "DATABASE IS CLOSED");
        }
        dbSource.open();
        if (dbSource.isOpen()) {
            Log.i(LOGTAG, "DATABASE IS OPEN");
        } else {
            Log.i(LOGTAG, "DATABASE IS CLOSED");
        }
        majors = dbSource.GetFromMajors(null, null, null); // Checking if table is empty
        if (majors.size() == 0) {
            createMajorsData();
        }

        buildings = dbSource.GetFromBuildings(null, null, null);
        if (buildings.size() == 0) {
            createBuildingsData();
        }

        rooms = dbSource.GetFromRooms(null, null, null);
        if (rooms.size() == 0) {
            createRoomsData();
        }

        courses = dbSource.GetFromCourses(null, null,  null);
        if (courses.size() == 0) {
            createCoursesData();
        }

        InputStream is = null;
        String image = "bldgNursing.jpg";
        try {
            is = getAssets().open(image);
        } catch (IOException e) {
            new DialogException(this, "IOException", "Error trying to open image " + image, new String[]{"Retry", "Exit"});
        }

        boolean internetAccess = false;
        boolean skipInternet = false;
        do {
            if (isNetworkAvailable()) {
                break;
            } else {
                new DialogException(this, "Internet Unavailable", "Unable to detect internet access. Would you like to try again or open the app without internet?", new String[]{"Yes", "No"});
            }
        } while(internetAccess = false && skipInternet == false);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void didTapGreetButton(View view) {
        EditText greetEditText =
                (EditText) findViewById(R.id.greetEditText);

        String name = greetEditText.getText().toString();
        String greeting = String.format("Hello, %s!", name);

        majors = dbSource.GetFromMajors("id<=5", null, "id DESC"); // Returns entries with id <= 5
        courses = dbSource.GetFromCourses("id<=5", null, "id DESC"); // Returns entries with id <= 5

        TextView messageTextView =
                (TextView) findViewById(R.id.messageTextView);

        messageTextView.setText(greeting);

    }

    @Override
    protected void onResume() {
        super.onResume();
        dbSource.open();
    }

    @Override
    protected void onPause() {
        super.onPause();
        dbSource.close();
    }

    private void createMajorsData() {
        Majors majors = new Majors();

        // Creating empty space in majors for spinner
        try {
            dbSource.InsertIntoMajors("Select_Major", "");
        } catch (final IndexOutOfBoundsException e) {
            new DialogException(this, "IndexOutOfBoundsException", "Error inserting into Courses : " + e.getMessage(), new String[]{"Exit"});
        }

        // Creating 3 entry into Majors table
        try {
            dbSource.InsertIntoMajors("CPE", "Computer Engineering");
        } catch (final IndexOutOfBoundsException e) {
            new DialogException(this, "IndexOutOfBoundsException", "Error inserting into Courses : " + e.getMessage(), new String[]{"Exit"});
        }

        try {
            dbSource.InsertIntoMajors("EE", "Electrical Engineering");
        } catch (final IndexOutOfBoundsException e) {
            new DialogException(this, "IndexOutOfBoundsException", "Error inserting into Courses : " + e.getMessage(), new String[]{"Exit"});
        }

        try {
            dbSource.InsertIntoMajors("ME", "Mechanical Engineering");
        } catch (final IndexOutOfBoundsException e) {
            new DialogException(this, "IndexOutOfBoundsException", "Error inserting into Courses : " + e.getMessage(), new String[]{"Exit"});
        }
    }

    private void createCoursesData() {
        Courses courses = new Courses();

        try {
            dbSource.InsertIntoCourses("Select_Major", "NULL", "Select_Room", 99999, "Select_Course Select_Section", "NULL", 9, "NULL", "NULL", "NULL", "NULL");
        } catch (final IndexOutOfBoundsException e) {
            new DialogException(this, "IndexOutOfBoundsException", "Error inserting into Courses : " + e.getMessage(), new String[]{"Exit"});
        }

        try {
            dbSource.InsertIntoCourses("CPE", "ENG", "134", 10165, "211 01", "INTRO COMPUTER PROG FOR ENGR", 3, "TR", "12:45", "02:05", "Bowman Ronald");
        } catch (final IndexOutOfBoundsException e) {
            new DialogException(this, "IndexOutOfBoundsException", "Error inserting into Courses : " + e.getMessage(), new String[]{"Exit"});
        }

        try {
            dbSource.InsertIntoCourses("CPE", "ENG", "207", 10166, "211 02", "INTRO COMPUTER PROG FOR ENGR", 3, "MW", "12:45", "02:05", "Bowman Ronald");
        } catch (final IndexOutOfBoundsException e) {
            new DialogException(this, "IndexOutOfBoundsException", "Error inserting into Courses : " + e.getMessage(), new String[]{"Exit"});
        }

        try {
            dbSource.InsertIntoCourses("ME", "OKT", "111", 10173, "323 01", "INTRO TO EMBEDDED COMPUTER SYS", 3, "MW", "12:45", "02:05", "Milenkovic Aleksander");
        } catch (final IndexOutOfBoundsException e) {
            new DialogException(this, "IndexOutOfBoundsException", "Error inserting into Courses : " + e.getMessage(), new String[]{"Exit"});
        }

        try {
            dbSource.InsertIntoCourses("ME", "MSB", "111", 10173, "323 01", "INTRO TO EMBEDDED COMPUTER SYS", 3, "MW", "12:45", "02:05", "Milenkovic Aleksander");
        } catch (final IndexOutOfBoundsException e) {
            new DialogException(this, "IndexOutOfBoundsException", "Error inserting into Courses : " + e.getMessage(), new String[]{"Exit"});
        }
    }

    private void createBuildingsData() {
        BuildingsPullParser parser = new BuildingsPullParser();
        List<Buildings> buildings = parser.parseXML(this);

        for (Buildings building : buildings) {
            try {
                dbSource.InsertIntoBuildings(building);
            } catch (final IndexOutOfBoundsException e) {
                new DialogException(this, "IndexOutOfBoundsException", "Error inserting into Courses : " + e.getMessage(), new String[]{"Exit"});
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
                new DialogException(this, "IndexOutOfBoundsException", "Error inserting into Courses : " + e.getMessage(), new String[]{"Exit"});
            }
        }
    }


    @Override
    public void onClick(View v) {

        if(v.getId() == R.id.classbtn){
            startActivity(new Intent(this, ClassTabActivity.class));
        }
        else if(v.getId() == R.id.buildingbtn){
            startActivity(new Intent(this, BuildingTabActivity.class));
        }
    }
}
