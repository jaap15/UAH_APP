package edu.uah.uahnavigation;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import edu.uah.modules.*;

public class ExternalNavigationActivity extends FragmentActivity implements OnMapReadyCallback, DirectionFinderListener {

    private GoogleMap mMap;
    private int MY_LOCATION_REQUEST_CODE = 66;
    private List<Marker> originMarkers = new ArrayList<>();
    private List<Marker> destinationMarkers = new ArrayList<>();
    private List<Polyline> polylinePaths = new ArrayList<>();
    private ProgressDialog progressDialog;
    private Button btnFindPath;
    private Intent i = getIntent();
    private LocationManager locationManager;
    private LocationListener locationListener;
    private String origin;
    private static final String LOGTAG = "WERT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_external_navigation);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        btnFindPath = (Button) findViewById(R.id.btnFindPath);

        btnFindPath.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRequest();
            }
        });

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
        } else {
            // Show rationale and request permission.
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_LOCATION_REQUEST_CODE);
        }
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

        Log.d(LOGTAG, "Address is " + getIntent().getStringExtra("Address"));

        // Grabbing LatLng positions from config.properties
        double lat = Double.parseDouble(Util.getProperty("UAH_LAT", this));
        double lng = Double.parseDouble(Util.getProperty("UAH_LONG", this));

        // UAH Markers
        LatLng uah = new LatLng(lat, lng);
        LatLng ENG = new LatLng(34.722338, -86.640705);
        LatLng OKT = new LatLng(34.719095, -86.646477);
        LatLng MSB = new LatLng(34.722394, -86.638184);
        LatLng CTC = new LatLng(34.722394, -86.638184);
        LatLng UFC = new LatLng(34.726534, -86.636901);
        LatLng NUR = new LatLng(34.729870, -86.638607);
        LatLng SST = new LatLng(34.725971, -86.641359);


        // Adding markers to map
        mMap.addMarker(new MarkerOptions().position(OKT).title("Olin B. King Technology Hall").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA)));
        mMap.addMarker(new MarkerOptions().position(ENG).title("Engineering Building").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
        mMap.addMarker(new MarkerOptions().position(MSB).title("Material Science Building").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
        mMap.addMarker(new MarkerOptions().position(CTC).title("Central Campus Residence Hall").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
        mMap.addMarker(new MarkerOptions().position(UFC).title("University Fitness Center").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
        mMap.addMarker(new MarkerOptions().position(NUR).title("Nursing Building").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE)));
        mMap.addMarker(new MarkerOptions().position(SST).title("Shelby Center").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));

        // Defining Zoom parameters
        mMap.setMinZoomPreference(10);
        mMap.setMaxZoomPreference(20);

        // Aiming the Camera at UAH and defining the inital zoom
        mMap.moveCamera(CameraUpdateFactory.newLatLng(uah));
        mMap.moveCamera(CameraUpdateFactory.zoomTo(15));
// Acquire a reference to the system Location Manager
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

// Define a listener that responds to location updates
        locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                // Called when a new location is found by the network location provider.
                makeUseOfNewLocation(mMap, location);
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }
        };

        // Register the listener with the Location Manager to receive location updates
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        } else {
            // Show rationale and request permission.
        }
    }

    public void makeUseOfNewLocation(GoogleMap mMap, Location location) {
        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude())));
        origin = location.getLatitude() + "," + location.getLongitude();
    }

    private void sendRequest() {
//        String origin = etOrigin.getText().toString();
        // Register the listener with the Location Manager to receive location updates
        Location lctn = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (lctn != null) {
            Log.d(LOGTAG, "last known location is " + lctn.getLatitude() + "," + lctn.getLongitude());
            origin = lctn.getLatitude() + "," + lctn.getLongitude();
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
            } else {
                // Show rationale and request permission.
            }
            String destination = getIntent().getStringExtra("Address");
            if (origin.isEmpty()) {
                Toast.makeText(this, "Please enter origin address!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (destination.isEmpty()) {
                Toast.makeText(this, "Please enter destination address!", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                new DirectionFinder(this, this, origin, destination).execute();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } else {
            new DialogException(this, "No location data available", "Unable to get location data, please wait for GPS to respond", new String[]{"Cancel"});
        }
    }

    @Override
    public void onDirectionFinderStart() {
        progressDialog = ProgressDialog.show(this, "Please wait.",
                "Finding direction..!", true);

        if (originMarkers != null) {
            for (Marker marker : originMarkers) {
                marker.remove();
            }
        }

        if (destinationMarkers != null) {
            for (Marker marker : destinationMarkers) {
                marker.remove();
            }
        }

        if (polylinePaths != null) {
            for (Polyline polyline:polylinePaths ) {
                polyline.remove();
            }
        }
    }

    @Override
    public void onDirectionFinderFailure() {
        new DialogException(this, "Error", "Could not find directions for specified address", new String[]{"Cancel"});
    }

    @Override
    public void onDirectionFinderSuccess(List<Route> routes) {
        progressDialog.dismiss();
        polylinePaths = new ArrayList<>();
        originMarkers = new ArrayList<>();
        destinationMarkers = new ArrayList<>();

        for (Route route : routes) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(route.startLocation, 16));
            ((TextView) findViewById(R.id.tvDuration)).setText(route.duration.text);
            ((TextView) findViewById(R.id.tvDistance)).setText(route.distance.text);

            originMarkers.add(mMap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.start_blue))
                    .title(route.startAddress)
                    .position(route.startLocation)));
            destinationMarkers.add(mMap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.end_green))
                    .title(route.endAddress)
                    .position(route.endLocation)));

            PolylineOptions polylineOptions = new PolylineOptions().
                    geodesic(true).
                    color(Color.BLUE).
                    width(10);

            for (int i = 0; i < route.points.size(); i++)
                polylineOptions.add(route.points.get(i));

            polylinePaths.add(mMap.addPolyline(polylineOptions));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == MY_LOCATION_REQUEST_CODE) {
            if (permissions.length == 1 &&
                    permissions[0] == Manifest.permission.ACCESS_FINE_LOCATION &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            } else {
                // Permission was denied. Display an error message.
            }
        }
    }
}
