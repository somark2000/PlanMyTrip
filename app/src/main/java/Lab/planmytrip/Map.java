package Lab.planmytrip;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
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
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.common.base.Predicates;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import Lab.planmytrip.Model.MyApplication;

public class Map extends AppCompatActivity implements OnMapReadyCallback {

    private MapView mapView;

    private static final int PERMISSION_FINE_LOCATION = 99;

    private boolean zoom=false;

    //current location
    Location currentLocation;

    //saved locations
    List<Location> locationList;
    List<Location> savedLocation;

    //location request
    LocationRequest locationRequest;
    LocationCallback locationCallBack;

    //google location API
    FusedLocationProviderClient fusedLocationProviderClient;
    private GoogleMap mMap;

    //UI
    SearchView searchView;
    FloatingActionButton packageButton;

    //DB
    private FirebaseFirestore db;
    private FirebaseUser user;
    private String userID;
    private String tripID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        getSupportActionBar().hide();

        //floating button
        packageButton= findViewById(R.id.package_butt);
        packageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyApplication myApplication = (MyApplication) getApplicationContext();
                Log.e(">>>>>>>> it is not OKAY", "notrip selected");
                System.out.println(myApplication.getTripID());
                if(myApplication.getTripID()==null) {
                    Toast.makeText(getApplicationContext(), "You need to select a trip first!", Toast.LENGTH_SHORT).show();
                }
                else {
                    Intent intent=new Intent(Map.this, Package.class);
                    startActivity(intent);
                }
            }
        });

        //searchbar
        searchView = findViewById(R.id.idSearchView);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // on below line we are getting the
                // location name from search view.
                String location = searchView.getQuery().toString();

                // below line is to create a list of address
                // where we will store the list of all address.
                List<Address> addressList = null;

                // checking if the entered location is null or not.
                if (location != null || location.equals("")) {
                    // on below line we are creating and initializing a geo coder.
                    Geocoder geocoder = new Geocoder(Map.this);
                    try {
                        // on below line we are getting location from the
                        // location name and adding that location to address list.
                        addressList = geocoder.getFromLocationName(location, 1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    // on below line we are getting the location
                    // from our list a first position.
                    Address address = addressList.get(0);

                    // on below line we are creating a variable for our location
                    // where we will add our locations latitude and longitude.
                    LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());

                    // on below line we are adding marker to that position.
                    mMap.addMarker(new MarkerOptions().position(latLng).title(location));

                    // below line is to animate camera to that position.
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        //navbar
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

        //location stuff

        locationRequest = new com.google.android.gms.location.LocationRequest();
        locationRequest.setInterval(30);
        locationRequest.setFastestInterval(50);
        locationRequest.setPriority(com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationCallBack = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                super.onLocationResult(locationResult);
                //save location
                //updateValues(locationResult.getLastLocation());
            }
        };

        MyApplication myApplication = (MyApplication) getApplicationContext();
        locationList = myApplication.getLocations();
        savedLocation = myApplication.getLocations();
        startLocationUpdates();

        updateGPS();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void startLocationUpdates() {
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
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(Map.this);
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallBack, null);
        updateGPS();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case PERMISSION_FINE_LOCATION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    updateGPS();
                } else {
                    Toast.makeText(this, "This application requires permission to access location to work properly", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
        }
    }

    private void updateGPS() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(Map.this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            //permission granted
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    getlocation();
                    locationList.add(currentLocation);
                    savedLocation.add(currentLocation);
                }
            });
        } else {
            //permission not granted
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_FINE_LOCATION);
            }
        }
    }

    private void getlocation() {
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
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if(location!=null){
                    LatLng lastLoaction=new LatLng(location.getLatitude(),location.getLongitude());
                    currentLocation=new Location(LocationManager.GPS_PROVIDER);
                    currentLocation.setLatitude(lastLoaction.latitude);
                    currentLocation.setLongitude(lastLoaction.longitude);
                    if(zoom==false) {
                        zoom=true;
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(lastLoaction,16.0f));
                    }
                }
            }
        });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
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
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMapToolbarEnabled(true);

        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(@NonNull LatLng latLng) {
                mMap.addMarker(new MarkerOptions().position(latLng));
                Location location=new Location(LocationManager.GPS_PROVIDER);
                location.setLatitude(latLng.latitude);
                location.setLongitude(latLng.longitude);
                Toast.makeText(getApplicationContext(),"Waypoint added",Toast.LENGTH_SHORT).show();
                db = FirebaseFirestore.getInstance();
                user = FirebaseAuth.getInstance().getCurrentUser();
                userID = Objects.requireNonNull(user).getUid();
                MyApplication myApplication=(MyApplication) getApplicationContext();
                savedLocation=myApplication.getLocations();
                savedLocation.add(location);
                savedLocation= Lists.newArrayList(Iterables.filter(savedLocation, Predicates.notNull()));
                tripID=myApplication.getTripID();
                ArrayList<GeoPoint> checkpoints=new ArrayList<>();
                Log.e("yay", "location");
                System.out.println(savedLocation);
                for(int i=0;i<savedLocation.size();++i){
                    System.out.println(i);
                    GeoPoint geoPoint=new GeoPoint(savedLocation.get(i).getLatitude(),savedLocation.get(i).getLongitude());
                    checkpoints.add(geoPoint);
                }
                System.out.println(checkpoints);
                db.collection("users").document(userID).collection("trips").document(tripID)
                        .update("checkpoints",checkpoints);
            }
        });

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(@NonNull Marker marker) {
                //further submenu popup
                LatLng lastLoaction=new LatLng(marker.getPosition().latitude,marker.getPosition().longitude);
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(lastLoaction,16.0f));
                Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse("google.navigation:q="+marker.getPosition().latitude+","+
                        marker.getPosition().longitude+"&mode=d"));
                intent.setPackage("com.google.android.apps.maps");
                if(intent.resolveActivity(getPackageManager())!=null) startActivity(intent);

                return false;
            }
        });

        Log.e("yay", "location");

        MyApplication myApplication=(MyApplication) getApplicationContext();
        savedLocation=myApplication.getLocations();
        System.out.println(savedLocation);

        for (Location location:savedLocation) {
            if(location.getLatitude()!=0.1&&location.getLongitude()!=0.1){
                LatLng latLng=new LatLng(location.getLatitude(),location.getLongitude());
                MarkerOptions markerOptions=new MarkerOptions();
                markerOptions.position(latLng);
                markerOptions.title("name");
                mMap.addMarker(markerOptions);
            }
        }
    }
}
