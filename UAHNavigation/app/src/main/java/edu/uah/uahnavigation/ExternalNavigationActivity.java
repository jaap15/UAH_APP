package edu.uah.uahnavigation;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.provider.SyncStateContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
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

     // Google map object
    private GoogleMap mMap;

    // Provides access to the system location services
    private LocationManager locationManager = null;
    private LocationListener locationListener;

    // Variables used for getLastKnownLocation
    private static final long MIN_DISTANCE_UPDATE = 1; // in meters
    private static final long MIN_TIME_UPDATE = 1000; // in milliseconds

    // Variables used for proximityAlert
    private PendingIntent proximityIntent;
    private static final long POINT_RADIUS = 100;
    private static final long POINT_EXPIRATION = -1;
    private static final String PROX_ALERT_INTENT = "edu.uah.uahnavigation.ProximityReceiver";

    // Used for GPS permission
    private int MY_LOCATION_REQUEST_CODE = 66;

    // Used for path finding and marking mMap
    private List<Marker> originMarkers = new ArrayList<>();
    private List<Marker> destinationMarkers = new ArrayList<>();
    private List<Polyline> polylinePaths = new ArrayList<>();

    // GUI elements
    private ProgressDialog progressDialog;
    private Button btnFindPath;

    private String origin;
    private static final String LOGTAG = "WERT";
    private final String PROX_ALERT = "app.test.PROXIMITY_ALERT";

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
                if (ContextCompat.checkSelfPermission(ExternalNavigationActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
                    Location lctn = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    makeUseOfNewLocation(mMap, lctn);
                    interior();
                } else {

                }
                //sendRequest();
            }
        });
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

        // Add markers to google maps
        addMarkers(mMap);

        // Aiming the Camera at UAH and defining the initial zoom
        LatLng uah = new LatLng(34.726523, -86.639696);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(uah));
        mMap.moveCamera(CameraUpdateFactory.zoomTo(15));
        mMap.setMinZoomPreference(10);
        mMap.setMaxZoomPreference(20);

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
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_UPDATE, MIN_DISTANCE_UPDATE, locationListener);

            try {
                locationManager.removeProximityAlert(proximityIntent);
            } catch (IllegalArgumentException e) {

            }

            sendRequest();
            String bldgName = getIntent().getStringExtra("building");
            switch(bldgName) {
                case "ENG": addProximityAlert(34.722665, -86.640562, 0); Log.d(LOGTAG, "ENG PROXIMITY ALERT"); drawCircle(new LatLng(34.722665, -86.640562), mMap); break;
                case "OKT": addProximityAlert(34.718763, -86.646563, 1); Log.d(LOGTAG, "OKT PROXIMITY ALERT"); drawCircle(new LatLng(34.718763, -86.646563), mMap); break;
                case "MSB": addProximityAlert(34.722449, -86.638581, 2); Log.d(LOGTAG, "MSB PROXIMITY ALERT"); drawCircle(new LatLng(34.722449, -86.638581), mMap); break;
                default:
            }

        } else {
            // Show rationale and request permission.
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_LOCATION_REQUEST_CODE);

        }
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(getBaseContext(), MainActivity.class);
        startActivity(i);
        finish();
    }

    private void drawCircle(LatLng point, GoogleMap mMap){
        // Instantiating CircleOptions to draw a circle around the marker
        CircleOptions circleOptions = new CircleOptions();

        // Specifying the center of the circle
        circleOptions.center(point);

        // Radius of the circle
        circleOptions.radius(POINT_RADIUS);

        // Border color of the circle
        circleOptions.strokeColor(Color.BLACK);

        // Fill color of the circle
        circleOptions.fillColor(0x30ff0000);

        // Border width of the circle
        circleOptions.strokeWidth(2);

        // Adding the circle to the GoogleMap
        mMap.addCircle(circleOptions);
    }

    private void addMarkers(GoogleMap googleMap) {
        // UAH Markers
        LatLng ENG = new LatLng(34.722338, -86.640705);
        LatLng OKT = new LatLng(34.718763, -86.646563);
        LatLng MSB = new LatLng(34.722394, -86.638184);
        LatLng CTC = new LatLng(34.722394, -86.638184);
        LatLng UFC = new LatLng(34.726534, -86.636901);
        LatLng NUR = new LatLng(34.729870, -86.638607);
        LatLng SST = new LatLng(34.725971, -86.641359);


        // Adding markers to map
        googleMap.addMarker(new MarkerOptions().position(OKT).title("Olin B. King Technology Hall").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA)));
        googleMap.addMarker(new MarkerOptions().position(ENG).title("Engineering Building").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
        googleMap.addMarker(new MarkerOptions().position(MSB).title("Material Science Building").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
        googleMap.addMarker(new MarkerOptions().position(CTC).title("Central Campus Residence Hall").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
        googleMap.addMarker(new MarkerOptions().position(UFC).title("University Fitness Center").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
        googleMap.addMarker(new MarkerOptions().position(NUR).title("Nursing Building").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE)));
        googleMap.addMarker(new MarkerOptions().position(SST).title("Shelby Center").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
    }

    private void interior() {
        Intent j = getIntent();
        String sourceName = "";
        String destinationName = "";
        String buildingName = "";

        try {
            sourceName = j.getStringExtra("source").toUpperCase();
        } catch (NullPointerException e) {

        }

        try {
            destinationName = j.getStringExtra("destination").toUpperCase();
        } catch (NullPointerException e) {
            destinationName = "ENG246";
        }

        try {
            buildingName = j.getStringExtra("building").toUpperCase();
        } catch (NullPointerException e) {
            buildingName = "ENG";
        }

        Intent i = new Intent(this, InteriorNavigationActivity.class);
        i.putExtra("source", "E147");
        i.putExtra("destination", destinationName);
        Log.d("TESTTEST", "Passing buildname to proximityReceiver: " + buildingName);
        i.putExtra("building", buildingName);
        try {
            i.putExtra("from", j.getStringExtra("from"));
        } catch (NullPointerException e) {

        }
        startActivity(i);
        finish();
    }

    private void addProximityAlert(double latitude, double longitude, int id) {
        // Checking OS for GPS permission, ACCESS_FINE_LOCATION = GPS, ACCESS_COURSE_LOCATION = Wifi
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // If granted

            String destinationName = null;
            String buildingName = null;

            try {
                destinationName = getIntent().getStringExtra("destination").toUpperCase();
                Log.d(LOGTAG, "destinationName = " + destinationName);
            } catch (NullPointerException e) {

            }

            try {
                buildingName = getIntent().getStringExtra("building").toUpperCase();
                Log.d(LOGTAG, "buildingname = " + buildingName);
            } catch (NullPointerException e) {
            }

            Intent k = new Intent(PROX_ALERT_INTENT);
            if (buildingName != null) {
                k.putExtra("building", buildingName);
            }

            if (destinationName != null) {
                k.putExtra("destination", destinationName);
            }

            try {
                k.putExtra("from", getIntent().getStringExtra("from"));
                k.putExtra("topSpinner", getIntent().getIntExtra("topSpinner", 0));
                k.putExtra("middleSpinner", getIntent().getIntExtra("middleSpinner", 0));
                k.putExtra("bottomSpinner", getIntent().getIntExtra("bottomSpinner", 0));
            } catch (NullPointerException e) {

            }
            proximityIntent = PendingIntent.getBroadcast(this, 0+id, k, PendingIntent.FLAG_UPDATE_CURRENT);

            locationManager.addProximityAlert (
                    latitude,
                    longitude,
                    POINT_RADIUS,
                    POINT_EXPIRATION,
                    proximityIntent
            );

            IntentFilter filter = new IntentFilter(PROX_ALERT_INTENT);
            registerReceiver(new ProximityReceiver(), filter);

        } else {
            Log.d("PROX", "Unable to create proximity alerts due to permission issues");
        }
    }

    public void makeUseOfNewLocation(GoogleMap mMap, Location location) {
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude())));
        origin = location.getLatitude() + "," + location.getLongitude();
        //Location pointLocation = retrievelocationFromPreferences();
        //float distance = location.distanceTo(pointLocation);
        //Toast.makeText(ExternalNavigationActivity.this, "Distance from Point:"+distance, Toast.LENGTH_LONG).show();
    }

    private Location retrievelocationFromPreferences() {
        Location location = new Location("POINT_LOCATION");
        location.setLatitude(34.710720);
        location.setLongitude(-86.724797);
        return location;
    }

    private void sendRequest() {
        // Register the listener with the Location Manager to receive location updates
        Location lctn = null;
        try {
            lctn = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        } catch (SecurityException E) {
            new DialogException(this, "SecurityException", "You must allow the UAH app to access GPS", new String[]{"Cancel"});
        }

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

            try {
                new DirectionFinder(this, this, origin, destination).execute();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                new DialogException(this, "UnsupportedEncodingException", "Unable to get location data, please check your GPS widget", new String[]{"Cancel"});
            }
        } else {
            new DialogException(this, "No location data available", "Unable to get location data, please check your GPS widget", new String[]{"Cancel"});
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
            Log.d("WERT", "ALLOW FOR GPS");
            if (permissions.length == 1 && permissions[0] == Manifest.permission.ACCESS_FINE_LOCATION && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("WERT", "PERMISSION GRANTED");

            } else {
                // Permission was denied. Display an error message.
                Log.d("WERT", "PERMISSION NOT GRANTED");
            }

            // Register the listener with the Location Manager to receive location updates
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                mMap.setMyLocationEnabled(true);
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_UPDATE, MIN_DISTANCE_UPDATE, locationListener);

                try {
                    locationManager.removeProximityAlert(proximityIntent);
                } catch (IllegalArgumentException e) {

                }

                sendRequest();
                String bldgName = getIntent().getStringExtra("building");
                switch(bldgName) {
                    case "ENG": addProximityAlert(34.722665, -86.640562, 0); Log.d(LOGTAG, "ENG PROXIMITY ALERT"); drawCircle(new LatLng(34.722665, -86.640562), mMap); break;
                    case "OKT": addProximityAlert(34.718763, -86.646563, 1); Log.d(LOGTAG, "OKT PROXIMITY ALERT"); drawCircle(new LatLng(34.718763, -86.646563), mMap); break;
                    case "MSB": addProximityAlert(34.722449, -86.638581, 2); Log.d(LOGTAG, "MSB PROXIMITY ALERT"); drawCircle(new LatLng(34.722449, -86.638581), mMap); break;
                    default:
                }

            }

        }
    }
}
