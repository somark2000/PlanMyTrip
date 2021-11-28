package Lab.planmytrip;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

public class Map extends AppCompatActivity {

    private MapView mapView;

    private static final int PERMISSION_FINE_LOCATION = 99;

    //UI elements
    TextView tv_lat;
    TextView tv_lon;
    TextView tv_altitude;
    TextView tv_accuracy;
    TextView tv_speed;
    TextView tv_sensor;
    TextView tv_updates;
    TextView tv_address;
    TextView tv_waypointCounts;

    Switch sw_locationupdates;
    Switch sw_gps;

    Button btn_newWaypoint;
    Button btn_showWaypointList;
    Button btn_showMap;

    //location tracking
    boolean updateOn = false;

    //current location
    Location currentLocation;

    //saved locations
    List<Location> locationList;

    //location request
    LocationRequest locationRequest;
    LocationCallback locationCallBack;

    //google location API
    FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.nav_view);

        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(0);
        menuItem.setChecked(true);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_map:
//                        Intent intent1 = new Intent(Map.this, Map.class);
//                        startActivity(intent1);
//                        overridePendingTransition(0, 0);
                        break;
                    case R.id.navigation_profile:
                        Intent intent2 = new Intent(Map.this, Profile.class);
                        startActivity(intent2);
                        overridePendingTransition(0, 0);
                        break;
                    case R.id.navigation_trips:
                        Intent intent3 = new Intent(Map.this, Trips.class);
                        startActivity(intent3);
                        overridePendingTransition(0, 0);
                        break;
                    default:
                        throw new IllegalStateException("Unexpected value: " + item.getItemId());
                }
                return false;
            }
        });

        mapView = (MapView) findViewById(R.id.mapView);


        //give values

        tv_lat = findViewById(R.id.tv_lat);
        tv_lon = findViewById(R.id.tv_lon);
        tv_altitude = findViewById(R.id.tv_altitude);
        tv_accuracy = findViewById(R.id.tv_accuracy);
        tv_speed = findViewById(R.id.tv_speed);
        tv_sensor = findViewById(R.id.tv_sensor);
        tv_updates = findViewById(R.id.tv_updates);
        tv_address = findViewById(R.id.tv_address);
        tv_waypointCounts=findViewById(R.id.tv_countOfPoints);

        sw_locationupdates = findViewById(R.id.sw_locationsupdates);
        sw_gps = findViewById(R.id.sw_gps);

        btn_newWaypoint=findViewById(R.id.btn_newWayPoint);
        btn_showWaypointList=findViewById(R.id.btn_showWaypointList);
        btn_showMap=findViewById(R.id.btn_showMap);

        //location stuff

        locationRequest = new com.google.android.gms.location.LocationRequest();
        locationRequest.setInterval(30);
        locationRequest.setFastestInterval(50);
        locationRequest.setPriority(com.google.android.gms.location.LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        locationCallBack = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                super.onLocationResult(locationResult);
                //save location
                updateValues(locationResult.getLastLocation());
            }
        };

        sw_gps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sw_gps.isChecked()) {
                    //more accurate
                    locationRequest.setPriority(com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY);
                    tv_sensor.setText(R.string.using_gps);
                } else {
                    //less accurate
                    locationRequest.setPriority(com.google.android.gms.location.LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
                    tv_sensor.setText(R.string.tower_wifi);
                }
            }
        });

        sw_locationupdates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sw_locationupdates.isChecked()) {
                    //turn on
                    startLocationUpdates();

                } else {
                    //turn off
                    stopLocationUpdates();
                }
            }
        });

        btn_newWaypoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get location

                //add location to global list
                MyApplication myApplication=(MyApplication) getApplicationContext();
                locationList=myApplication.getLocations();
                locationList.add(currentLocation);
            }
        });

        btn_showWaypointList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Map.this,ShowSavedLocation.class);
                startActivity(intent);
            }
        });

        btn_showMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Map.this,MapsActivity.class );
                startActivity(intent);
            }
        });

        updateGPS();
    }

    private void startLocationUpdates() {
        tv_updates.setText(R.string.location_track);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallBack, null);
        updateGPS();
    }

    private void stopLocationUpdates() {
        tv_updates.setText(R.string.location_not_tracked);
        tv_lat.setText(R.string.location_not_tracked);
        tv_lon.setText(R.string.location_not_tracked);
        tv_speed.setText(R.string.location_not_tracked);
        tv_address.setText(R.string.location_not_tracked);
        tv_accuracy.setText(R.string.location_not_tracked);
        tv_altitude.setText(R.string.location_not_tracked);
        tv_sensor.setText(R.string.location_not_tracked);

        fusedLocationProviderClient.removeLocationUpdates(locationCallBack);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode){
            case PERMISSION_FINE_LOCATION:
                if(grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    updateGPS();
                }
                else {
                    Toast.makeText(this,"This application requires permission to access location to work properly",Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
        }
    }

    private void updateGPS(){
        fusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(Map.this);
        if(ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){
            //permission granted
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    updateValues(location);
                    currentLocation=location;
                    locationList.add(currentLocation);
                }
            });
        }
        else {
            //permission not granted
            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
                requestPermissions(new String[] {Manifest.permission.ACCESS_FINE_LOCATION},PERMISSION_FINE_LOCATION);
            }
        }
    }

    private void updateValues(Location location) {
        //update UI values

        tv_lat.setText(String.valueOf(location.getLatitude()));
        tv_lon.setText(String.valueOf(location.getLongitude()));
        tv_accuracy.setText(String.valueOf(location.getAccuracy()));
        if(location.hasAltitude()){
            tv_altitude.setText(String.valueOf(location.getAltitude()));
        }
        else{
            tv_altitude.setText(R.string.not_available);
        }
        if(location.hasSpeed()){
            tv_speed.setText(String.valueOf(location.getSpeed()));
        }
        else{
            tv_speed.setText(R.string.not_available);
        }

        Geocoder geocoder=new Geocoder(Map.this);
        try {
            List<Address> addresses=geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
            tv_address.setText(addresses.get(0).getAddressLine(0));
        }
        catch (Exception exception)
        {
            tv_address.setText(R.string.unable_location);
        }

        MyApplication myApplication= (MyApplication) getApplicationContext();
        locationList=myApplication.getLocations();
        tv_waypointCounts.setText(Integer.toString(locationList.size()-1));
    }
    }
