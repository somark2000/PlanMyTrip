package Lab.planmytrip;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.maps.MapView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Map extends AppCompatActivity {

    private MapView mapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.nav_view);

        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(2);
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
    }
}