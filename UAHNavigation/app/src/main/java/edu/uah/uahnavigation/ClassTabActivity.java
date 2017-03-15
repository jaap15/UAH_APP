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

        private ProgressBar spinner;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_class_tab);
            spinner = (ProgressBar)findViewById(R.id.progressBar1);
            spinner.setVisibility(View.GONE);

            final Button findButton = (Button)findViewById(R.id.findbtn);
            final Button retButton= (Button)findViewById(R.id.returnbtn);

            //findButton.setEnabled(false);
            //retButton.setEnabled(false);
            findButton.setOnClickListener(this);
            retButton.setOnClickListener((View.OnClickListener) this);

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
            buildings = dbSource.GetFromBuildings(null, null, null);
            rooms = dbSource.GetFromRooms(null, null, null);
            courses = dbSource.GetFromCourses(null, null, null);

            sectionsArray = courses.toArray(new Courses[courses.size()]);
            spinnerSections = (Spinner) findViewById(spinnerSection);
            adapterSection = new SectionSpinAdapter(this, android.R.layout.simple_spinner_item, sectionsArray);
            spinnerSections.setEnabled(false);
            spinnerSections.setClickable(false);
            spinnerSections.setAdapter(adapterSection);

            coursesArray = courses.toArray(new Courses[courses.size()]);
            spinnerCourses = (Spinner) findViewById(R.id.spinnerCourse);
            adapterCourses = new CoursesSpinAdapter(this, android.R.layout.simple_spinner_item, coursesArray);
            spinnerCourses.setEnabled(false);
            spinnerCourses.setClickable(false);
            spinnerCourses.setAdapter(adapterCourses);
/*
            spinnerCourses.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view,
                                           int position, long id) {
                    // Here you get the current item (a User object) that is selected by its position
                    Courses course = adapterCourses.getItem(position);
                    // Here you can do the action you want to...
                    spinnerSections.setEnabled(true);
                    spinnerSections.setClickable(true);
                    spinner.setVisibility(View.VISIBLE);

                    String selected_course = course.getCourse();
                    Log.i(LOGTAG, "Selected Course is : " + course.getCourse());
                    courses = dbSource.GetFromCourses("course==\""+selected_course+"\"", null, null);
                    sectionsArray = courses.toArray(new Courses[courses.size()]);

                    adapterSection.setCourses(sectionsArray);
                    adapterSection.notifyDataSetChanged();

                    spinner.setVisibility(View.GONE);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
*/
            majorsArray = majors.toArray(new Majors[majors.size()]);
            spinnerMajors = (Spinner) findViewById(R.id.spinnerMajor);
            adapterMajors = new MajorsSpinAdapter(this, android.R.layout.simple_spinner_item, majorsArray);
            spinnerMajors.setAdapter(adapterMajors);

            spinnerMajors.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view,
                                           int position, long id) {
                    // Here you get the current item (a User object) that is selected by its position
                    Majors majorpos = adapterMajors.getItem(position);
                    // Here you can do the action you want to...
                    if(majorpos == majors.get(0)){
                        spinnerCourses.setSelection(0);
                        spinnerCourses.setEnabled(false);
                        spinnerCourses.setClickable(false);
                        spinnerSections.setSelection(0);
                        spinnerSections.setEnabled(false);
                        spinnerSections.setClickable(false);
                        findButton.setEnabled(false);
                        spinner.setVisibility(View.GONE);
                    }
                    else {
                        long selected_major_id = majorpos.getId();
                        courses = dbSource.GetFromCourses("major_id==" + selected_major_id, null, null);
                       // adapterCourses.setCourses(coursesArray);
                        Toast.makeText(ClassTabActivity.this, adapterView.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
                        spinnerCourses.setEnabled(true);
                        spinnerCourses.setClickable(true);
                        adapterCourses.notifyDataSetChanged();
                        spinner.setVisibility(View.GONE);

                        spinnerCourses.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                                adapterCourses.setCourses(coursesArray);
                                // Here you get the current item (a User object) that is selected by its position
                                Courses coursepos = adapterCourses.getItem(position);
                                // Here you can do the action you want to...
                                String CourseNotSelected = "";
                            //    if(CourseNotSelected == "Select_Course") {
                                if(spinnerCourses.getSelectedItemPosition() == 0){
                                    spinnerSections.setSelection(0);
                                    spinnerSections.setEnabled(false);
                                    spinnerSections.setClickable(false);
                                    spinner.setVisibility(View.GONE);
                                }else {
                                    String selected_course = coursepos.getCourse();
                                    Log.i(LOGTAG, "Selected Course is : " + coursepos.getCourse());
                                    courses = dbSource.GetFromCourses("course==\"" + selected_course + "\"", null, null);
                              //      sectionsArray = courses.toArray(new Courses[courses.size()]);
                              //      adapterSection.setCourses(sectionsArray);
                                    Toast.makeText(ClassTabActivity.this, adapterView.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
                                    spinnerSections.setEnabled(true);
                                    spinnerSections.setClickable(true);
                                    adapterSection.notifyDataSetChanged();

                                    spinnerSections.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

                                        @Override
                                        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {

                                            sectionsArray = courses.toArray(new Courses[courses.size()]);
                                  //          adapterSection.setCourses(sectionsArray);
                                            Courses sectionpos = adapterSection.getItem(position);

                                            if(spinnerSections.getSelectedItemPosition() == 0){

                                                findButton.setEnabled(false);
                                            }else {

                                                Toast.makeText(ClassTabActivity.this, adapterView.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
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
                startActivity(new Intent(this, CourseTabActivity.class));
            }
            else if(view.getId() == R.id.returnbtn){
                startActivity(new Intent(this, MainActivity.class));
            }
        }
}
