package edu.uah.uahnavigation;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.uah.model.Buildings;
import edu.uah.model.Courses;
import edu.uah.model.CoursesSpinAdapter;
import edu.uah.model.Majors;
import edu.uah.model.MajorsSpinAdapter;
import edu.uah.model.Rooms;
import edu.uah.model.SectionSpinAdapter;

import static edu.uah.uahnavigation.R.id.spinnerSection;

public class ClassTabActivity extends AppCompatActivity implements View.OnClickListener{

        DatabaseSource dbSource;
        private String LOGTAG = "QWER";
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

        private ProgressBar progressBar;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_class_tab);

            // Defining our progress bar
            progressBar = (ProgressBar)findViewById(R.id.progressBar1);
            progressBar.setVisibility(View.INVISIBLE);

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

            // Creating our Major spinner
            spinnerMajors = (Spinner) findViewById(R.id.spinnerMajor);
            adapterMajors = new MajorsSpinAdapter(this, android.R.layout.simple_spinner_item, majorsArray);
            spinnerMajors.setAdapter(adapterMajors);

            // Setting up an action for Item Selected Event on our Majors spinner
            spinnerMajors.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view,
                                           int position, long id) {

                    // Grabbing our active item in majors spinner
                    Majors majorpos = adapterMajors.getItem(position);

                    // Disabling GUI elements when majors spinner is set to index 0
                    if(majorpos == majors.get(0)){


                        courses = dbSource.GetFromCourses(null, null, null);
                        coursesArray = courses.toArray(new Courses[courses.size()]);

                        // Updating the spinner to reflect the filtered data
                        adapterCourses.setCourses(coursesArray);
                        adapterCourses.notifyDataSetChanged();
                        spinnerCourses.setSelection(0);
                        spinnerCourses.setEnabled(false);
                        spinnerCourses.setClickable(false);

                        sections = dbSource.GetFromCourses(null, null, null);
                        sectionsArray = sections.toArray(new Courses[sections.size()]);

                        // Updating the spinner to reflect the filtered data
                        adapterSection.setSections(sectionsArray);
                        adapterSection.notifyDataSetChanged();
                        spinnerSections.setSelection(0);
                        spinnerSections.setEnabled(false);
                        spinnerSections.setClickable(false);

                        findButton.setEnabled(false);
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

                        // Updating the spinner to reflect the filtered data
                        adapterCourses.setCourses(coursesArray);
                        adapterCourses.notifyDataSetChanged();

                        // ProgressBar is invisible
                        progressBar.setVisibility(View.INVISIBLE);

                        // Setting up an action for Item Selected Event on our Courses spinner
                        spinnerCourses.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {

                                // Grabbing our active item in courses spinner
                                Courses coursepos = adapterCourses.getItem(position);

                                // Disabling some GUI elements when courses spinner is set to index 0
                                if(spinnerCourses.getSelectedItemPosition() == 0){
                                    spinnerSections.setSelection(0);
                                    spinnerSections.setEnabled(false);
                                    spinnerSections.setClickable(false);
                                    progressBar.setVisibility(View.GONE);
                                } else {
                                    // ProgressBar is visible
                                    progressBar.setVisibility(View.VISIBLE);

                                    // Enabling the sections spinner now that a course has been selected
                                    spinnerSections.setEnabled(true);
                                    spinnerSections.setClickable(true);

                                    // Filtering our data for sections related to selected course
                                    String selected_course = coursepos.getCourse();
                                    long selected_major = coursepos.getMajor();
                                    sections = dbSource.GetFromCourses("course==\"" + selected_course + "\" AND major_id==" + selected_major, null, null);
                                    sectionsArray = sections.toArray(new Courses[sections.size()]);

                                    // Updating the spinner to reflect the filtered data
                                    adapterSection.setSections(sectionsArray);
                                    adapterSection.notifyDataSetChanged();

                                    // ProgressBar is invisible
                                    progressBar.setVisibility(View.INVISIBLE);

                                    // Setting up an action for Item Selected Event on our Sections spinner
                                    spinnerSections.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                                        @Override
                                        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {

                                            // Grabbing our active item in sections spinner
                                            Courses sectionpos = adapterSection.getItem(position);

                                            // Disabling some GUI elements when courses spinner is set to index 0
                                            if(spinnerSections.getSelectedItemPosition() == 0) {
                                                findButton.setEnabled(false);
                                            } else {
                                                findButton.setEnabled(true);
                                            }
                                        }

                                        @Override
                                        public void onNothingSelected(AdapterView<?> adapterView) {

                                        }
                                    });
                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });
                    }

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }

        @Override
        public void onClick(View view) {
            if(view.getId() == R.id.findbtn){
                startActivity(new Intent(this, MapsActivity.class));
            }
            else if(view.getId() == R.id.returnbtn){
                startActivity(new Intent(this, MainActivity.class));
            }
        }
}

