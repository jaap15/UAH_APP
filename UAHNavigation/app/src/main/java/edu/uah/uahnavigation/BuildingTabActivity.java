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

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_building_tab);

        // Defining our progress bar
        progressBar = (ProgressBar)findViewById(R.id.progressBar1);
        progressBar.setVisibility(View.GONE);

        // Defining our push buttons
        final Button findBtn = (Button) findViewById(R.id.findbtn);
        final Button returnBtn = (Button) findViewById(R.id.returnbtn);
        findBtn.setEnabled(false);

        // Associating our push buttons with click events
        findBtn.setOnClickListener(this);
        returnBtn.setOnClickListener(this);

        // Opening the database
        dbSource = new DatabaseSource(this);
        dbSource.open();

        // Grabbing all of our data from the database
        buildings = dbSource.GetFromBuildings(null, null, null);
        rooms = dbSource.GetFromRooms(null, null, null);

        // Converting list data to array data
        roomsArray = rooms.toArray(new Rooms[rooms.size()]);
        buildingsArray = buildings.toArray(new Buildings[buildings.size()]);

        // Creating our Rooms spinner
        spinnerRooms = (Spinner) findViewById(R.id.spinnerRoom);
        adapterRooms = new RoomsSpinAdapter(this, android.R.layout.simple_spinner_item, roomsArray);
        spinnerRooms.setEnabled(false);
        spinnerRooms.setClickable(false);
        spinnerRooms.setAdapter(adapterRooms);
        spinnerRooms.setSelection(0);

        // Creating our Buildings spinner
        spinnerBuildings = (Spinner) findViewById(R.id.spinnerBuilding);
        adapterBuildings = new BuildingsSpinAdapter(this, android.R.layout.simple_spinner_item, buildingsArray);
        spinnerBuildings.setAdapter(adapterBuildings);
        spinnerBuildings.setSelection(0);

        // Setting up an action for Item Selected Event on our Buildings spinner
        spinnerBuildings.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int position, long id) {
                // Grabbing our active item in majors spinner
                Buildings building = adapterBuildings.getItem(position);


                // Disabling GUI elements when majors spinner is set to index 0
                if(building == buildings.get(0)) {

                    rooms = dbSource.GetFromRooms(null, null, null);
                    roomsArray = rooms.toArray(new Rooms[rooms.size()]);

                    // Updating the spinner for reflect the filtered data
                    adapterRooms.setCourses(roomsArray);
                    adapterRooms.notifyDataSetChanged();

                    // Disabling some of the GUI
                    spinnerRooms.setSelection(0);
                    spinnerRooms.setEnabled(false);
                    spinnerRooms.setClickable(false);
                    findBtn.setEnabled(false);
                } else {
                    // ProgressBar is visible
                    progressBar.setVisibility(View.VISIBLE);

                    // Enabling the rooms spinner now that a building has been selected
                    spinnerRooms.setEnabled(true);
                    spinnerRooms.setClickable(true);
                    spinnerRooms.setSelection(0);
                    findBtn.setEnabled(true);

                    // Filtering our database for rooms related to selected building
                    Long selected_building = building.getId();
                    rooms = dbSource.GetFromRooms("building_id==" + selected_building, null, null);
                    roomsArray = rooms.toArray(new Rooms[rooms.size()]);

                    // Updating the spinner for reflect the filtered data
                    adapterRooms.setCourses(roomsArray);
                    adapterRooms.notifyDataSetChanged();

                    // ProgressBar is invisible
                    progressBar.setVisibility(View.INVISIBLE);
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
            startActivity(new Intent(this, ExternalNavigationActivity.class));  //Not a class activity but a new activity
        }
        else if(view.getId() == R.id.returnbtn){
            startActivity(new Intent(this, MainActivity.class));
        }
    }


}
