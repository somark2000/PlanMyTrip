package Lab.planmytrip.Model;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;
import java.util.Locale;

public class POI extends AppCompatActivity {
    // the point of interest displayed on the map
    private double longitude;
    private double latitude;
    private String address;
    private String city;
    private String state;
    private String country;
    private String name;

    public POI(Location l) {
        longitude=l.getLongitude();
        latitude=l.getLatitude();
        name="name";
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());
        try{
            addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            city = addresses.get(0).getLocality();
            state= addresses.get(0).getAdminArea();
            country = addresses.get(0).getCountryName();
            name = addresses.get(0).getFeatureName();
        }
        catch (Exception e){
            address = "Not available";
            city ="";
            state= "";
            country = "";
            name ="Name not available";
        }

    }

    @Override
    public String toString() {
        return  name + "\n"+
                longitude +" " + latitude +"\n"+
                address + " " + city + " " + state + " "+ country;
    }
}
