package Lab.planmytrip;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Trips extends AppCompatActivity {

    private Button createPackageList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trips);

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.nav_view);
        createPackageList = (Button) findViewById(R.id.create_package_list);

        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(2);
        menuItem.setChecked(true);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_map:
                        Intent intent1 = new Intent(Trips.this, Map.class);
                        startActivity(intent1);
                        overridePendingTransition(0, 0);
                        break;
                    case R.id.navigation_profile:
                        Intent intent2 = new Intent(Trips.this, Profile.class);
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

        createPackageList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCreatePackageList();
            }
        });

    }

    private void openCreatePackageList() {
        Intent intent = new Intent(this, Package.class);
        startActivity(intent);
    }
}