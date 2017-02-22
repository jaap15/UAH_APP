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

public class BuildingTabActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_building_tab);

        final Spinner building = (Spinner) findViewById(R.id.spinnerBuilding);
        final Spinner room = (Spinner) findViewById(R.id.spinnerRoom);

        final Button find = (Button) findViewById(R.id.findbtn2);
        final Button ret = (Button) findViewById(R.id.returnbtn2);

        find.setEnabled(false); //Disable the find button
        ret.setEnabled(false); //Disable the return button
        // building.setEnabled(false);
        room.setEnabled(false);

        find.setOnClickListener(this);
        ret.setOnClickListener((View.OnClickListener) this);

        ArrayAdapter<String> buildingAdapter = new ArrayAdapter<String>(BuildingTabActivity.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.buildings));

        buildingAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        building.setAdapter(buildingAdapter);


        ArrayAdapter<String> roomAdapter = new ArrayAdapter<String>(BuildingTabActivity.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.rooms));

        roomAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        room.setAdapter(roomAdapter);

        building.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                int buildingpos = building.getSelectedItemPosition();
                if(buildingpos != 0){
                    building.setEnabled(true);
                    Toast.makeText(BuildingTabActivity.this, adapterView.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
                    room.setEnabled(true);

                    room.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            int roompos = room.getSelectedItemPosition();
                            if(roompos != 0){
                                Toast.makeText(BuildingTabActivity.this, adapterView.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
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
                    room.setSelection(0);
                    room.setEnabled(false);
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
        if(view.getId() == R.id.findbtn2){
            startActivity(new Intent(this, ClassTabActivity.class));  //Not a class activity but a new activity
        }
        else if(view.getId() == R.id.returnbtn2){
            startActivity(new Intent(this, MainActivity.class));
        }
    }
}
