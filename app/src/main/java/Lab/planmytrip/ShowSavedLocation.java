package Lab.planmytrip;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import Lab.planmytrip.Model.MyApplication;
import Lab.planmytrip.Model.POI;

public class ShowSavedLocation extends AppCompatActivity {
    ListView lv_savedlocations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_saved_location);
        getSupportActionBar().hide();

        MyApplication myApplication=(MyApplication) getApplicationContext();
        List<Location> locations=myApplication.getLocations();
        List<String> pois=new ArrayList<>();

        int i=0;
        for(Location location:locations){
            if(i>0){
                POI poi=new POI(location);
                pois.add(poi.toString());
            }
            i++;
        }

        lv_savedlocations=findViewById(R.id.lv_savedlocations);
        lv_savedlocations.setAdapter(new ArrayAdapter<String>( this, android.R.layout.simple_list_item_1 , pois));

        //onclick event for items
        lv_savedlocations.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Create a Uri from an intent string. Use the result to create an Intent.
                Uri gmmIntentUri = Uri.parse("google.streetview:cbll="+locations.get(position+1).getLatitude()+
                        ","+locations.get(position+1).getLongitude());

                // Create an Intent from gmmIntentUri. Set the action to ACTION_VIEW
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                // Make the Intent explicit by setting the Google Maps package
                mapIntent.setPackage("com.google.android.apps.maps");

                // Attempt to start an activity that can handle the Intent
                startActivity(mapIntent);
            }
        });
    }
}