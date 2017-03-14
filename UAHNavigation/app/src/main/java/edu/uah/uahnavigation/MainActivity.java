package edu.uah.uahnavigation;


import android.content.Intent;
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

        Button clbtn = (Button) findViewById(R.id.classbtn);
        Button Bbtn = (Button) findViewById(R.id.buildingbtn);
        // bClass = (Button) findViewById(R.id.bClass);
        // bBuilding = (Button) findViewById(R.id.bBuilding);
        clbtn.setOnClickListener(this);
        Bbtn.setOnClickListener(this);

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
/*
        spinner = (ProgressBar)findViewById(R.id.progressBar1);
        spinner.setVisibility(View.GONE);


        coursesArray = courses.toArray(new Courses[courses.size()]);
        spinnerCourses = (Spinner) findViewById(R.id.spinnerCourses);
        adapterCourses = new CoursesSpinAdapter(this, android.R.layout.simple_spinner_item, coursesArray);
        spinnerCourses.setEnabled(false);
        spinnerCourses.setClickable(false);
        spinnerCourses.setAdapter(adapterCourses);

        Majors[] majorsArray;
        majorsArray = majors.toArray(new Majors[majors.size()]);
        spinnerMajors = (Spinner) findViewById(R.id.spinnerMajors);
        adapterMajors = new MajorsSpinAdapter(this, android.R.layout.simple_spinner_item, majorsArray);
        spinnerMajors.setAdapter(adapterMajors);
        spinnerMajors.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {
                // Here you get the current item (a User object) that is selected by its position
                Majors major = adapterMajors.getItem(position);
                // Here you can do the action you want to...
                spinnerCourses.setEnabled(true);
                spinnerCourses.setClickable(true);
                spinner.setVisibility(View.VISIBLE);

                long selected_major_id = major.getId();

                courses = dbSource.GetFromCourses("major_id=="+selected_major_id, null, null); // Returns entries with id <= 5
                coursesArray = courses.toArray(new Courses[courses.size()]);
                adapterCourses.setCourses(coursesArray);
                adapterCourses.notifyDataSetChanged();

                spinner.setVisibility(View.GONE);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterMajors) {  }
        });
    */
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
        if (!dbSource.InsertIntoMajors("Select_Major", "")) {
            Log.i(LOGTAG, "Error inserting Data into majors");
        }
        // Creating 3 entry into Majors table
        if (!dbSource.InsertIntoMajors("CPE", "Computer Engineering")) {
            Log.i(LOGTAG, "Error inserting Data into majors");
        }
        if (!dbSource.InsertIntoMajors("EE", "Electrical Engineering")) {
            Log.i(LOGTAG, "Error inserting Data into majors");
        }
        if (!dbSource.InsertIntoMajors("ME", "Mechanical Engineering")) {
            Log.i(LOGTAG, "Error inserting Data into majors");
        }
    }

    private void createCoursesData() {
        Courses courses = new Courses();

        if (!dbSource.InsertIntoCourses("Select_Major", "NULL", "Select_Room", 99999, "Select_Course Select_Section", "NULL", 9, "NULL", "NULL", "NULL", "NULL")) {
            Log.i(LOGTAG, "Error inserting Data into courses");
        }

        if (!dbSource.InsertIntoCourses("CPE", "ENG", "134", 10165, "211 01", "INTRO COMPUTER PROG FOR ENGR", 3, "TR", "12:45", "02:05", "Bowman Ronald")) {
            Log.i(LOGTAG, "Error inserting Data into courses");
        }

        if (!dbSource.InsertIntoCourses("CPE", "ENG", "207", 10166, "211 02", "INTRO COMPUTER PROG FOR ENGR", 3, "MW", "12:45", "02:05", "Bowman Ronald")) {
            Log.i(LOGTAG, "Error inserting Data into courses");
        }

        if (!dbSource.InsertIntoCourses("ME", "NUR", "111", 10173, "323 01", "INTRO TO EMBEDDED COMPUTER SYS", 3, "MW", "12:45", "02:05", "Milenkovic Aleksander")) {
            Log.i(LOGTAG, "Error inserting Data into courses");
        }
    }

    private void createBuildingsData() {
        BuildingsPullParser parser = new BuildingsPullParser();
        List<Buildings> buildings = parser.parseXML(this);

        for (Buildings building : buildings) {
            dbSource.InsertIntoBuildings(building);
        }
    }

    private void createRoomsData() {
        RoomsPullParser parser = new RoomsPullParser();
        List<Rooms> rooms = parser.parseXML(this);

        for (Rooms room : rooms) {
            dbSource.InsertIntoRooms(room);
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
