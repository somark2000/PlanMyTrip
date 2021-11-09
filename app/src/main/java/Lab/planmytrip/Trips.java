package Lab.planmytrip;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Trips extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trips);

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.nav_view);

        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(2);
        menuItem.setChecked(true);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.navigation_map:
                        Intent intent1 = new Intent(Trips.this, Map.class);
                        startActivity(intent1);
                        break;
                    case R.id.navigation_profile:
                        Intent intent2 = new Intent(Trips.this, Profile.class);
                        startActivity(intent2);
                        break;
                    case R.id.navigation_trips:
//                        Intent intent3 = new Intent(Trips.this, Trips.class);
//                        startActivity(intent3);
                        break;
                    default:
                        throw new IllegalStateException("Unexpected value: " + item.getItemId());
                }
                return false;
            }
        });

//        case R.id.create_package:
//        Intent intent4 = new Intent(Trips.this, Package.class);
//        startActivity(intent4);
//        break;
    }
}