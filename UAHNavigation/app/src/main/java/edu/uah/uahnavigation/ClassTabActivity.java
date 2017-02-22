package edu.uah.uahnavigation;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.Spinner;

import java.util.List;

import edu.uah.model.Buildings;
import edu.uah.model.Courses;
import edu.uah.model.CoursesSpinAdapter;
import edu.uah.model.Majors;
import edu.uah.model.MajorsSpinAdapter;
import edu.uah.model.Rooms;

public class ClassTabActivity extends AppCompatActivity implements View.OnClickListener{

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
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_class_tab);
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
            buildings = dbSource.GetFromBuildings(null, null, null);
            rooms = dbSource.GetFromRooms(null, null, null);
            courses = dbSource.GetFromCourses(null, null, null);

            coursesArray = courses.toArray(new Courses[courses.size()]);
            spinnerCourses = (Spinner) findViewById(R.id.spinnerCourse);
            adapterCourses = new CoursesSpinAdapter(this, android.R.layout.simple_spinner_item, coursesArray);
            spinnerCourses.setEnabled(false);
            spinnerCourses.setClickable(false);
            spinnerCourses.setAdapter(adapterCourses);

            Majors[] majorsArray;
            majorsArray = majors.toArray(new Majors[majors.size()]);
            spinnerMajors = (Spinner) findViewById(R.id.spinnerMajor);
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
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
/*

                final Spinner major = (Spinner) findViewById(R.id.spinnerMajor);
        final Spinner course = (Spinner) findViewById(R.id.spinnerCourse);
        final Spinner section = (Spinner) findViewById(R.id.spinnerSection);

        final Button find = (Button) findViewById(R.id.findbtn);
        final Button ret = (Button) findViewById(R.id.returnbtn);

        find.setEnabled(false); //Disable the find button
        ret.setEnabled(false); //Disable the return button
        course.setEnabled(false);
        section.setEnabled(false);

        find.setOnClickListener(this);
        ret.setOnClickListener((View.OnClickListener) this);

        ArrayAdapter<String> majorAdapter = new ArrayAdapter<String>(ClassTabActivity.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.majors));

        majorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        major.setAdapter(majorAdapter);


        ArrayAdapter<String> courseAdapter = new ArrayAdapter<String>(ClassTabActivity.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.courses));

        courseAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        course.setAdapter(courseAdapter);

        ArrayAdapter<String> sectionAdapter = new ArrayAdapter<String>(ClassTabActivity.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.sections));

        sectionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        section.setAdapter(sectionAdapter);


        major.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                int majorpos = major.getSelectedItemPosition();
                if(majorpos != 0){
                    Toast.makeText(ClassTabActivity.this, adapterView.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
                    course.setEnabled(true);

                    course.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            int coursepos = course.getSelectedItemPosition();
                            if(coursepos != 0){
                                Toast.makeText(ClassTabActivity.this, adapterView.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
                                section.setEnabled(true);

                                section.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                        int sectionpos = section.getSelectedItemPosition();
                                        if(sectionpos != 0){
                                            Toast.makeText(ClassTabActivity.this, adapterView.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
                                            Button find = (Button) findViewById(R.id.findbtn);
                                            find.setEnabled(true);
                                            Button ret = (Button) findViewById(R.id.returnbtn);
                                            ret.setEnabled(true);

                                        }
                                        else {
                                            find.setEnabled(false);
                                            ret.setEnabled(false);
                                        }
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> adapterView) {

                                    }
                                });
                            }
                            else {
                                section.setSelection(0);
                                section.setEnabled(false);
                                find.setEnabled(false);
                                ret.setEnabled(false);
                            }

                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });
                }
                else {
                    course.setSelection(0);
                    section.setSelection(0);
                    course.setEnabled(false);
                    section.setEnabled(false);
                    find.setEnabled(false);
                    ret.setEnabled(false);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });*/
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
