package edu.uah.uahnavigation;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        // Grabbing LatLng positions from config.properties
        double lat = Double.parseDouble(Util.getProperty("UAH_LAT", this));
        double lng = Double.parseDouble(Util.getProperty("UAH_LONG", this));
        double user_lat = Double.parseDouble(Util.getProperty("USER_LAT", this));
        double user_lng = Double.parseDouble(Util.getProperty("USER_LONG", this));

        // UAH Markers
        LatLng uah = new LatLng(lat, lng);
        LatLng shelbyCenter = new LatLng(34.725971, -86.641359);
        LatLng ChargerUnion = new LatLng(34.727294, -86.640201);

        // Adding markers to map
        mMap.addMarker(new MarkerOptions().position(uah).title("University of Alabama in Huntsville"));
        mMap.addMarker(new MarkerOptions().position(shelbyCenter).title("Shelby Center").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                .title("Shelby Center for Science and Mathematics").snippet("1668 enrolled Science students"));
        mMap.addMarker(new MarkerOptions().position(ChargerUnion).title("Charger Union").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));

        // Defining Zoom parameters
        mMap.setMinZoomPreference(10);
        mMap.setMaxZoomPreference(20);

        // Aiming the Camera at UAH and defining the inital zoom
        mMap.moveCamera(CameraUpdateFactory.newLatLng(uah));
        mMap.moveCamera(CameraUpdateFactory.zoomTo(15));
    }
}
