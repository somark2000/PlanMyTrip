package Lab.planmytrip;

import androidx.appcompat.app.AppCompatActivity;

import android.location.Location;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

public class ShowSavedLocation extends AppCompatActivity {
    ListView lv_savedlocations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_saved_location);

        MyApplication myApplication=(MyApplication) getApplicationContext();
        List<Location> locations=myApplication.getLocations();

        lv_savedlocations=findViewById(R.id.lv_savedlocations);
        lv_savedlocations.setAdapter(new ArrayAdapter<Location>( this, android.R.layout.simple_list_item_1 , locations));

    }
}