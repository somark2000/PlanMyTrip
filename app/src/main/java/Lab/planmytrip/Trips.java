package Lab.planmytrip;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.internal.GenericIdpActivity;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.SortedMap;

import Lab.planmytrip.Model.MyApplication;

public class Trips extends AppCompatActivity {
    //UI
    private ListView tripsListView;
    private FloatingActionButton tripButton;

    //Data storing
    private List<String> titles = new ArrayList<>();
    private List<LatLng> locations = new ArrayList<>();
    private List<DocumentSnapshot> trips;

    //DB stuff
    private FirebaseUser user;
    private FirebaseFirestore db;

    private String userID;
    private String tripID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trips);
        getSupportActionBar().hide();

        //floating button
        tripButton = findViewById(R.id.add_trip);
        tripButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Trips.this, AddTripActivity.class);
                startActivity(intent);
            }
        });

        //navbar
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.nav_view);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(1);
        menuItem.setChecked(true);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_map:
                        Intent intent1 = new Intent(Trips.this, Map.class);
                        finish();
                        startActivity(intent1);
                        overridePendingTransition(0, 0);
                        break;
                    case R.id.navigation_profile:
                        Intent intent2 = new Intent(Trips.this, Profile.class);
                        finish();
                        startActivity(intent2);
                        overridePendingTransition(0, 0);
                        break;
                    case R.id.navigation_trips:
//                        Intent intent3 = new Intent(Trips.this, Trips.class);
//                        startActivity(intent3);
//                        overridePendingTransition(0, 0);
                        break;
                    default:
                        throw new IllegalStateException("Unexpected value: " + item.getItemId());
                }
                return false;
            }
        });

        //get data from DB
        db = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        userID = Objects.requireNonNull(user).getUid();

        db.collection("users").document(userID).collection("trips").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(@NonNull QuerySnapshot queryDocumentSnapshots) {
                if (!queryDocumentSnapshots.isEmpty()) {
                    Log.e(">>>>>>>>Trips: it is OKAY", " documentSnapshot not null");
                    trips = queryDocumentSnapshots.getDocuments();
                    for (DocumentSnapshot documentSnapshot : trips) {
                        System.out.println(documentSnapshot.getData().get("name"));
                        titles.add((String) documentSnapshot.getData().get("name"));
                    }
                    ArrayAdapter<String> titleAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1, titles);
                    tripsListView.setAdapter(titleAdapter);

                } else {
                    Log.e(">>>>>>>>Trips: it is not OKAY", " documentSnapshot null");
                    Toast.makeText(Trips.this, "There are no trips.\n Add a trip.", Toast.LENGTH_LONG).show();
                }
            }
        });

        //listview
        tripsListView = findViewById(R.id.tripListView);

        tripsListView.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, titles));

        tripsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //DB stuff
                for (DocumentSnapshot documentSnapshot : trips) {
                    if (titles.get(position).equals((String) documentSnapshot.getData().get("name"))) {
                        Log.e(">>>>>>>>Trips: read", " documentSnapshot ");
                        System.out.println(documentSnapshot.getData());
                        List<GeoPoint> geoPoints = (List<GeoPoint>) documentSnapshot.getData().get("checkpoints");
                        for (GeoPoint geoPoint : geoPoints) {
                            double lat = geoPoint.getLatitude();
                            double lng = geoPoint.getLongitude();
                            LatLng latLng = new LatLng(lat, lng);
                            locations.add(latLng);
                        }
                        MyApplication myApplication = (MyApplication) getApplicationContext();
                        tripID = documentSnapshot.getId();
                        myApplication.setTripID(tripID);
                    }
                }

                //location to list
                MyApplication myApplication = (MyApplication) getApplicationContext();
                Log.e(">>>>>>>>Trips: location size", " documentSnapshot null");
                System.out.println(locations.size());
                List<Location> locationList = new ArrayList<>();
                for (LatLng latLng : locations) {
                    Location location = new Location(LocationManager.GPS_PROVIDER);
                    location.setLatitude(latLng.latitude);
                    location.setLongitude(latLng.longitude);
                    locationList.add(location);
                }
                Log.e(">>>>>>>>Trips: locations", " wait for singleton");

                myApplication.setLocations(locationList);
                System.out.println(myApplication.getLocations());

                //move to maps fragment
                Intent intent1 = new Intent(Trips.this, Map.class);
                finish();
                startActivity(intent1);
                overridePendingTransition(0, 0);
            }
        });

    }
}