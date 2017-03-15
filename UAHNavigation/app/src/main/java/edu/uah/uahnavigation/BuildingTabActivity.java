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

        findBtn.setEnabled(false);
        returnBtn.setEnabled(false);
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
                if(building == buildings.get(0)){
                    spinnerRooms.setSelection(0);
                    spinnerRooms.setEnabled(false);
                    spinnerRooms.setClickable(false);
                    findBtn.setEnabled(false);
                    returnBtn.setEnabled(false);
                    spinner.setVisibility(View.GONE);
                }else {
                    Long selected_building = building.getId();
                    rooms = dbSource.GetFromRooms("building_id==" + selected_building, null, null);
                    spinnerRooms.setEnabled(true);
                    spinnerRooms.setClickable(true);

                    spinnerRooms.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                            Rooms room = adapterRooms.getItem(position);
                            if(room == rooms.get(0)){

                                //findBtn.setEnabled(false);
                                //returnBtn.setEnabled(false);
                            }else {
                               // rooms = dbSource.GetFromRooms("building_id==" + selected_building, null, null);
                               // spinnerRooms.setEnabled(true);
                               // spinnerRooms.setClickable(true);
                                spinnerRooms.setSelection(0);
                                // spinner.setVisibility(View.VISIBLE);
                                roomsArray = rooms.toArray(new Rooms[rooms.size()]);
                                adapterRooms.setCourses(roomsArray);
                                adapterRooms.notifyDataSetChanged();
                                spinner.setVisibility(View.GONE);
                                findBtn.setEnabled(true);
                                returnBtn.setEnabled(true);
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
            startActivity(new Intent(this, MapsActivity.class));  //Not a class activity but a new activity
        }
        else if(view.getId() == R.id.returnbtn){
            startActivity(new Intent(this, MainActivity.class));
        }
    }


}
