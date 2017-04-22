package edu.uah.uahnavigation;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import edu.uah.model.Buildings;
import edu.uah.model.Courses;
import edu.uah.model.CoursesListAdapter;
import edu.uah.model.CoursesSpinAdapter;
import edu.uah.model.Majors;
import edu.uah.model.MajorsSpinAdapter;
import edu.uah.model.Rooms;
import edu.uah.model.SectionSpinAdapter;

import static edu.uah.uahnavigation.R.id.spinnerSection;

public class ClassTabActivity extends BaseActivity  implements View.OnClickListener{

        DatabaseSource dbSource;
        private String LOGTAG = "EXT";
        List<Majors> majors;
        List<Courses> courses;
        List<Courses> sections;
        List<Buildings> buildings;
        List<Rooms> rooms;

        private Courses[] coursesArray;
        private Courses[] sectionsArray;
        private Majors [] majorsArray;
        private Spinner spinnerMajors;
        private Spinner spinnerCourses;
        private Spinner spinnerSections;
        private MajorsSpinAdapter adapterMajors;
        private CoursesSpinAdapter adapterCourses;
        private SectionSpinAdapter adapterSection;
        private CoursesListAdapter classInfoAdapter;
        private ListView classInformation;

        private ProgressBar progressBar;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_class_tab);

            // Defining our progress bar
            progressBar = (ProgressBar)findViewById(R.id.progressBar1);
            progressBar.setVisibility(View.INVISIBLE);
            classInformation = (ListView)findViewById(R.id.classInfo);

            // Defining our push buttons
            final Button findButton = (Button)findViewById(R.id.findbtn);
            final Button retButton= (Button)findViewById(R.id.returnbtn);

            // Associating our push buttons with click events
            findButton.setOnClickListener(this);
            retButton.setOnClickListener((View.OnClickListener) this);

            // Opening the database
            dbSource = new DatabaseSource(this);
            dbSource.open();

            // Grabbing all of our data from the database
            majors = dbSource.GetFromMajors(null, null, null); // Checking if table is empty
            buildings = dbSource.GetFromBuildings(null, null, null);
            rooms = dbSource.GetFromRooms(null, null, null);
            courses = dbSource.GetFromCourses(null, null, null);
            sections = dbSource.GetFromCourses(null, null, null);

            // Converting list data to array data
            sectionsArray = sections.toArray(new Courses[courses.size()]);
            coursesArray = courses.toArray(new Courses[courses.size()]);
            majorsArray = majors.toArray(new Majors[majors.size()]);

            // Creating our Section spinner
            spinnerSections = (Spinner) findViewById(spinnerSection);
            adapterSection = new SectionSpinAdapter(this, android.R.layout.simple_spinner_item, sectionsArray);
            spinnerSections.setEnabled(false);
            spinnerSections.setClickable(false);
            spinnerSections.setAdapter(adapterSection);

            // Creating our Course spinner
            spinnerCourses = (Spinner) findViewById(R.id.spinnerCourse);
            adapterCourses = new CoursesSpinAdapter(this, android.R.layout.simple_spinner_item, coursesArray);
            spinnerCourses.setEnabled(false);
            spinnerCourses.setClickable(false);
            spinnerCourses.setAdapter(adapterCourses);
            classInfoAdapter = new CoursesListAdapter(this, coursesArray);
            classInformation.setAdapter(classInfoAdapter);

            // Creating our Major spinner
            spinnerMajors = (Spinner) findViewById(R.id.spinnerMajor);
            adapterMajors = new MajorsSpinAdapter(this, android.R.layout.simple_spinner_item, majorsArray);
            spinnerMajors.setAdapter(adapterMajors);
            spinnerMajors.setSelection(0);
            classInfoAdapter.setCourses(new Courses[]{});
            classInfoAdapter.notifyDataSetChanged();

            // Setting up an action for Item Selected Event on our Majors spinner
            spinnerMajors.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view,
                                           int position, long id) {

                    // Grabbing our active item in majors spinner
                    Majors majorpos = adapterMajors.getItem(position);

                    // Disabling GUI elements when majors spinner is set to index 0
                    if(majorpos == majors.get(0)) {

                        courses = dbSource.GetFromCourses(null, null, null);
                        coursesArray = courses.toArray(new Courses[courses.size()]);

                        // Updating the spinner to reflect the filtered data
                        adapterCourses.setCourses(coursesArray);
                        adapterCourses.notifyDataSetChanged();
                        spinnerCourses.setSelection(0, true);
                        spinnerCourses.setEnabled(false);
                        spinnerCourses.setClickable(false);

                        sections = dbSource.GetFromCourses(null, null, null);
                        sectionsArray = sections.toArray(new Courses[sections.size()]);

                        // Updating the spinner to reflect the filtered data
                        adapterSection.setSections(sectionsArray);
                        adapterSection.notifyDataSetChanged();
                        spinnerSections.setSelection(0, true);
                        spinnerSections.setEnabled(false);
                        spinnerSections.setClickable(false);

                        findButton.setEnabled(false);
                        classInfoAdapter.setCourses(new Courses[]{});
                        classInfoAdapter.notifyDataSetChanged();
                    }
                    else {
                        // ProgressBar is visible
                        progressBar.setVisibility(View.VISIBLE);

                        // Enabling the courses spinner now that a major has been selected
                        spinnerCourses.setEnabled(true);
                        spinnerCourses.setClickable(true);

                        // Filtering our database for courses related to selected major
                        long selected_major_id = majorpos.getId();
                        courses = dbSource.GetFromCourses("major_id==" + selected_major_id, null, null);
                        coursesArray = courses.toArray(new Courses[courses.size()]);
                        Courses[] adapterArray = removeDuplicates(coursesArray);

                        // Updating the spinner to reflect the filtered data
                        adapterCourses.setCourses(adapterArray);
                        adapterCourses.notifyDataSetChanged();
                        spinnerCourses.setAdapter(adapterCourses);
                        spinnerCourses.setSelection(0);

                        // ProgressBar is invisible
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            // Setting up an action for Item Selected Event on our Courses spinner
            spinnerCourses.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {

                    Log.d(LOGTAG, "SPINNER COURSE ON ITEM SELECTED");
                    // Grabbing our active item in courses spinner
                    final Courses coursepos = adapterCourses.getItem(position);

                    // ProgressBar is visible
                    progressBar.setVisibility(View.VISIBLE);

                    // Enabling the sections spinner now that a course has been selected
                    spinnerSections.setEnabled(true);
                    spinnerSections.setClickable(true);
                    findButton.setEnabled(true);

                    // Filtering our data for sections related to selected course
                    String selected_course = null;
                    try {
                        selected_course = coursepos.getCourse();
                    } catch (NullPointerException e) {

                    }

                    if (selected_course != null) {
                        long selected_major = coursepos.getMajor();
                        sections = dbSource.GetFromCourses("course==\"" + selected_course + "\" AND major_id==" + selected_major, null, null);
                        sectionsArray = sections.toArray(new Courses[sections.size()]);

                        // Updating the spinner to reflect the filtered data
                        adapterSection.setSections(sectionsArray);
                        adapterSection.notifyDataSetChanged();
                        spinnerSections.setAdapter(adapterSection);
                        Log.d(LOGTAG, "SPINNER COURSE ON ITEM SELECTED 2");
                    } else {
                        sections = new ArrayList<Courses>();
                        sectionsArray = sections.toArray(new Courses[sections.size()]);

                        // Updating the spinner to reflect the filtered data
                        adapterSection.setSections(sectionsArray);
                        adapterSection.notifyDataSetChanged();
                        spinnerSections.setAdapter(adapterSection);
                        classInfoAdapter.setCourses(new Courses[]{});
                        classInfoAdapter.notifyDataSetChanged();
                    }

                    spinnerSections.setSelection(0);

                    // ProgressBar is invisible
                    progressBar.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

            // Setting up an action for Item Selected Event on our Sections spinner
            spinnerSections.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {

                    // Grabbing our active item in sections spinner
                    Courses sectionpos = adapterSection.getItem(position);
                    Log.d(LOGTAG, "sectionpos: " + sectionpos + " " + sectionpos.getCourse() + " " + sectionpos.getRoom() + " " + sectionpos.getSection());
                    classInfoAdapter.setCourses(new Courses[]{sectionpos});
                    classInfoAdapter.notifyDataSetChanged();
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
        }

        @Override
        public void onClick(View view) {
            if(view.getId() == R.id.findbtn) {

                Courses crse = null;
                try {
                    crse = (Courses) spinnerCourses.getSelectedItem();
                } catch (IndexOutOfBoundsException e) {

                }
                if (crse != null) {
                    String[] longToString = new String[]{String.valueOf(crse.getRoom())};

                    List<Rooms> room = null;
                    try {
                        room = dbSource.GetFromRooms("id=?", longToString, null);
                    } catch (IndexOutOfBoundsException e) {
                        new DialogException(this, "IndexOutofBoundsException", "room is equal to null", new String[]{"Cancel"});
                        room = null;
                    }

                    if (room != null) {
                        String[] selectArgs = new String[]{};
                        try {
                            selectArgs = new String[]{String.valueOf(room.get(0).getBuilding())};
                        } catch (IndexOutOfBoundsException e) {

                        }

                        List<Buildings> bldg = null;
                        try {
                            bldg = dbSource.GetFromBuildings("id=?", selectArgs, null);
                        } catch (IndexOutOfBoundsException e) {
                            new DialogException(this, "IndexOutofBoundsException", "bldg is equal to null", new String[]{"Cancel"});
                        }

                        try {
                            Intent i = new Intent(getBaseContext(), ExternalNavigationActivity.class);

                            try {
                                room = dbSource.GetFromRooms("id=?", new String[]{String.valueOf(crse.getRoom())}, null);
                            } catch (IndexOutOfBoundsException e) {

                            }

                            try {
                                bldg = dbSource.GetFromBuildings("id=?", new String[]{String.valueOf(room.get(0).getBuilding())}, null);
                            } catch (IndexOutOfBoundsException e) {

                            }

                            try {
                                i.putExtra("Address", bldg.get(0).getAddress());
                            } catch (NullPointerException e) {

                            }

                            try {
                                i.putExtra("destination", bldg.get(0).getDescription()+room.get(0).getRoom());
                            } catch (NullPointerException e) {

                            }

                            try {
                                i.putExtra("building", bldg.get(0).getDescription());
                            } catch (NullPointerException e) {

                            }
                            startActivity(i);  //Not a class activity but a new activity
                            finish();
                        } catch (IndexOutOfBoundsException e) {
                            new DialogException(this, "Cannot find room", "External Navigation cannot work without a room number. If you want to navigate to a building, please visit the " +
                                    "BUILDINGS tab!", new String[]{"Cancel"});
                        }
                    } else {
                        new DialogException(this, "IndexOutofBoundsException", "Rooms is equal to null", new String[]{"Cancel"});
                    }
                } else {
                    new DialogException(this, "IndexOutofBoundsException", "Crse is equal to null", new String[]{"Cancel"});
                }
                } else if (view.getId() == R.id.returnbtn) {
                    startActivity(new Intent(this, MainActivity.class));
                    finish();
                }
        }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(getBaseContext(), MainActivity.class);
        startActivity(i);
        finish();
    }

    public static Courses[] removeDuplicates(Courses[] arr) {
        Courses[] whitelist = Arrays.copyOfRange(arr, 0,1);
        String oldCourse = " ";

        try {
            oldCourse = arr[0].getCourse();
        } catch (final ArrayIndexOutOfBoundsException e) {
            Log.d("QWER", e.getMessage());
        }

        for (Courses nextElem : arr) {
            String newCourse = nextElem.getCourse();
            if (oldCourse.equals(newCourse)) {

            } else {
                whitelist = Arrays.copyOf(whitelist, whitelist.length + 1);
                whitelist[whitelist.length - 1] = nextElem;
                oldCourse = newCourse;
            }
        }

        return whitelist;
    }
}

