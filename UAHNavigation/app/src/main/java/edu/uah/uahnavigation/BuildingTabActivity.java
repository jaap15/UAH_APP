package edu.uah.uahnavigation;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Arrays;
import java.util.List;

import edu.uah.model.Buildings;
import edu.uah.model.BuildingsSpinAdapter;
import edu.uah.model.Rooms;
import edu.uah.model.RoomsSpinAdapter;

public class BuildingTabActivity extends AppCompatActivity implements View.OnClickListener {

    DatabaseSource dbSource;
    private String LOGTAG = "QWER";
    List<Buildings> buildings;
    List<Rooms> rooms;

    private Spinner spinnerBuildings;
    private Spinner spinnerRooms;
    private Buildings[] buildingsArray;
    private Rooms[] roomsArray;
    private BuildingsSpinAdapter adapterBuildings;
    private RoomsSpinAdapter adapterRooms;

    private ProgressBar spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_building_tab);

        spinner = (ProgressBar)findViewById(R.id.progressBar1);
        spinner.setVisibility(View.GONE);
        final Button findBtn = (Button) findViewById(R.id.findbtn);
        final Button returnBtn = (Button) findViewById(R.id.returnbtn);

        findBtn.setOnClickListener(this);
        returnBtn.setOnClickListener(this);

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
        buildings = dbSource.GetFromBuildings(null, null, null);
        rooms = dbSource.GetFromRooms(null, null, null);

        roomsArray= rooms.toArray(new Rooms[rooms.size()]);
        spinnerRooms = (Spinner) findViewById(R.id.spinnerRoom);
        adapterRooms = new RoomsSpinAdapter(this, android.R.layout.simple_spinner_item, roomsArray);
        spinnerRooms.setEnabled(false);
        spinnerRooms.setClickable(false);
        spinnerRooms.setAdapter(adapterRooms);
        spinnerRooms.setSelection(0);

        buildingsArray = buildings.toArray(new Buildings[buildings.size()]);
        spinnerBuildings = (Spinner) findViewById(R.id.spinnerBuilding);
        adapterBuildings = new BuildingsSpinAdapter(this, android.R.layout.simple_spinner_item, buildingsArray);
        spinnerBuildings.setAdapter(adapterBuildings);
        spinnerBuildings.setSelection(0);

        spinnerBuildings.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {
                // Here you get the current item (a User object) that is selected by its position
                Buildings building = adapterBuildings.getItem(position);
                // Here you can do the action you want to...
                spinnerRooms.setEnabled(true);
                spinnerRooms.setClickable(true);
                spinner.setVisibility(View.VISIBLE);

                Long selected_building = building.getId();
                rooms = dbSource.GetFromRooms("building_id=="+selected_building, null, null);
                roomsArray = rooms.toArray(new Rooms[rooms.size()]);

                adapterRooms.setCourses(roomsArray);
                adapterRooms.notifyDataSetChanged();

                spinner.setVisibility(View.GONE);
                spinnerRooms.setSelection(0);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.findbtn){
            startActivity(new Intent(this, CourseTabActivity.class));  //Not a class activity but a new activity
        }
        else if(view.getId() == R.id.returnbtn){
            startActivity(new Intent(this, MainActivity.class));
        }
    }


}
