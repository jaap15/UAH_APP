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

public class ClassTabActivity extends AppCompatActivity implements View.OnClickListener{

        DatabaseSource dbSource;
        private String LOGTAG = "QWER";
        List<Majors> majors;
        List<Courses> courses;
        List<Courses> sections;

        private Majors[] majorsArray;
        private Courses[] coursesArray;
        private Courses[] sectionsArray;

        private Spinner spinnerMajors;
        private Spinner spinnerCourses;
        private Spinner spinnerSection;

        private MajorsSpinAdapter adapterMajors;
        private CoursesSpinAdapter adapterCourses;
        private SectionSpinAdapter adapterSection;

        int remove_first_item = 0;

        private ProgressBar spinner;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_class_tab);

            // Linking buttons to layout
            final Button returnBtn = (Button)findViewById(R.id.returnbtn);
            final Button findBtn = (Button)findViewById(R.id.findbtn);

            returnBtn.setOnClickListener(this);
            findBtn.setOnClickListener(this);

            spinner = (ProgressBar)findViewById(R.id.progressBar1);
            spinner.setVisibility(View.GONE);

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
            courses = dbSource.GetFromCourses(null, null, null);
            sections = dbSource.GetFromCourses(null, null, null);

            sectionsArray = sections.toArray(new Courses[sections.size()]);
            spinnerSection = (Spinner) findViewById(R.id.spinnerSection);
            adapterSection = new SectionSpinAdapter(this, android.R.layout.simple_spinner_item, sectionsArray);
            spinnerSection.setEnabled(false);
            spinnerSection.setClickable(false);
            spinnerSection.setSelection(0);
            spinnerSection.setAdapter(adapterSection);

            coursesArray = courses.toArray(new Courses[courses.size()]);
            spinnerCourses = (Spinner) findViewById(R.id.spinnerCourse);
            adapterCourses = new CoursesSpinAdapter(this, android.R.layout.simple_spinner_item, coursesArray);
            spinnerCourses.setEnabled(false);
            spinnerCourses.setClickable(false);
            spinnerCourses.setSelection(0);
            spinnerCourses.setAdapter(adapterCourses);

            spinnerCourses.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view,
                                           int position, long id) {
                    // Here you get the current item (a User object) that is selected by its position
                    Courses course = adapterCourses.getItem(position);
                    // Here you can do the action you want to...
                    spinnerSection.setEnabled(true);
                    spinnerSection.setClickable(true);
                    spinner.setVisibility(View.VISIBLE);

                    String selected_course = course.getCourse();
                    sections = dbSource.GetFromCourses("course==\""+selected_course+"\"", null, null);
                    sectionsArray = sections.toArray(new Courses[sections.size()]);

                    adapterSection.setCourses(sectionsArray);
                    adapterSection.notifyDataSetChanged();

                    spinner.setVisibility(View.GONE);
                    spinnerSection.setSelection(0);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            majorsArray = majors.toArray(new Majors[majors.size()]);
            spinnerMajors = (Spinner) findViewById(R.id.spinnerMajor);
            adapterMajors = new MajorsSpinAdapter(this, android.R.layout.simple_spinner_item, majorsArray);
            spinnerMajors.setAdapter(adapterMajors);
            spinnerMajors.setSelection(0);

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
                    courses = dbSource.GetFromCourses("major_id=="+selected_major_id, null, null);
                    coursesArray = courses.toArray(new Courses[courses.size()]);

                    adapterCourses.setCourses(coursesArray);
                    adapterCourses.notifyDataSetChanged();

                    spinner.setVisibility(View.GONE);
                    spinnerCourses.setSelection(1);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }

        @Override
        public void onClick(View view) {
            if(view.getId() == R.id.findbtn) {
                startActivity(new Intent(this, CourseTabActivity.class));
            }
            else if(view.getId() == R.id.returnbtn) {
                startActivity(new Intent(this, MainActivity.class));
            }
        }
}
