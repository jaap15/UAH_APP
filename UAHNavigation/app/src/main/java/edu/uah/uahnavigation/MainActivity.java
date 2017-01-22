package edu.uah.uahnavigation;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

import edu.uah.model.*;

public class MainActivity extends AppCompatActivity {

    DatabaseSource dbSource;
    private String LOGTAG = "QWER";
    List<Majors> majors;
    List<Courses> courses;
    List<Buildings> buildings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbSource = new DatabaseSource(this);
        dbSource.open();
        majors = dbSource.GetFromMajors(null, null); // Checking if table is empty
        if (majors.size() == 0) {
            createMajorsData();
            createCoursesData();
            majors = dbSource.GetFromMajors(null, null); // Returns all entries
            courses = dbSource.GetFromCourses(null, null); // Returns all entries
        }
        buildings = dbSource.GetFromBuildings(null, null);
        if (buildings.size() == 0) {
            createBuildingsData();
        }
    }

    public void didTapGreetButton(View view) {
        EditText greetEditText =
                (EditText) findViewById(R.id.greetEditText);

        String name = greetEditText.getText().toString();
        String greeting = String.format("Hello, %s!", name);

        majors = dbSource.GetFromMajors("id<=5", "id DESC"); // Returns entries with id <= 5
        courses = dbSource.GetFromCourses("id<=5", "id DESC"); // Returns entries with id <= 5

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

        // Creating 3 entry into Majors table
        majors.setAll("CPE", "Computer Engineering");
        if (!dbSource.InsertIntoMajors(majors)) {
            Log.i(LOGTAG, "Error inserting Data into majors");
        }
        majors.setAll("EE", "Electrical Engineering");
        if (!dbSource.InsertIntoMajors(majors)) {
            Log.i(LOGTAG, "Error inserting Data into majors");
        }
        majors.setAll("ME", "Mechanical Engineering");
        if (!dbSource.InsertIntoMajors(majors)) {
            Log.i(LOGTAG, "Error inserting Data into majors");
        }
    }

    private void createCoursesData() {
        Courses courses = new Courses();

        courses.setAll(7, 134, 10165, "211 01", "INTRO COMPUTER PROG FOR ENGR", 3, "TR", "12:45", "02:05", "Bowman Ronald");
        if (!dbSource.InsertIntoCourses(courses)) {
            Log.i(LOGTAG, "Error inserting Data into courses");
        }
        courses.setAll(15, 114, 10173, "323 01", "INTRO TO EMBEDDED COMPUTER SYS", 3, "MW", "12:45", "02:05", "Milenkovic Aleksander");
        if (!dbSource.InsertIntoCourses(courses)) {
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
}
