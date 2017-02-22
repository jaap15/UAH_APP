package edu.uah.uahnavigation;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

public class CourseTabActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_tab);

        final Spinner spcourse = (Spinner) findViewById(R.id.spCourse);
        final Spinner spcoursenum = (Spinner) findViewById(R.id.spCourseNumber);
        final Spinner spsection = (Spinner) findViewById(R.id.spSection);

        //  List<Buildings> buildings;
        //  DatabaseSource dbSource;
        //   dbSource = new DatabaseSource(this);
        //    dbSource.open();
        //    buildings = dbSource.GetFromBuildings(null, null, null);

        final Button find = (Button) findViewById(R.id.findbtn3);
        final Button ret = (Button) findViewById(R.id.returnbtn3);

        find.setEnabled(false); //Disable the find button
        ret.setEnabled(false); //Disable the return button
        spcoursenum.setEnabled(false);
        spsection.setEnabled(false);

        find.setOnClickListener(this);
        ret.setOnClickListener((View.OnClickListener) this);

        ArrayAdapter<String> courseAdapter = new ArrayAdapter<String>(CourseTabActivity.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.majoracronymes));

        // ArrayAdapter<Buildings> courseAdapter = new ArrayAdapter<Buildings>(CourseTab.this,
        //               android.R.layout.simple_list_item_1, buildings);

        courseAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spcourse.setAdapter(courseAdapter);


        ArrayAdapter<String> coursenumAdapter = new ArrayAdapter<String>(CourseTabActivity.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.coursenumbers));

        coursenumAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spcoursenum.setAdapter(coursenumAdapter);

        ArrayAdapter<String> sectionAdapter = new ArrayAdapter<String>(CourseTabActivity.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.sections));

        sectionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spsection.setAdapter(sectionAdapter);

        spcourse.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                int spcoursepos = spcourse.getSelectedItemPosition();
                if(spcoursepos != 0){
                    Toast.makeText(CourseTabActivity.this, adapterView.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
                    spcoursenum.setEnabled(true);

                    spcoursenum.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            int spcoursenumpos = spcoursenum.getSelectedItemPosition();
                            if(spcoursenumpos != 0){
                                Toast.makeText(CourseTabActivity.this, adapterView.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
                                spsection.setEnabled(true);

                                spsection.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                        int spsectionpos = spsection.getSelectedItemPosition();
                                        if(spsectionpos != 0){
                                            Toast.makeText(CourseTabActivity.this, adapterView.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
                                            find.setEnabled(true);
                                            ret.setEnabled(true);
                                        }
                                        else{
                                            find.setEnabled(false);
                                            ret.setEnabled(false);
                                        }
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> adapterView) {

                                    }
                                });
                            }
                            else{
                                spsection.setEnabled(false);
                                find.setEnabled(false);
                                ret.setEnabled(false);
                            }
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });
                }
                else{
                    spcoursenum.setEnabled(false);
                    spsection.setEnabled(false);
                    find.setEnabled(false);
                    ret.setEnabled(false);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.findbtn3){
            //  startActivity(new Intent(this, CourseTab.class));  //Suppose to be MapView class
        }
        else if(view.getId() == R.id.returnbtn3){
            startActivity(new Intent(this, ClassTabActivity.class));
        }
    }
}
