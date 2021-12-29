package Lab.planmytrip;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import Lab.planmytrip.Model.MyApplication;
import Lab.planmytrip.databinding.ActivityMapsBinding;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    List<Location> savedLocation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        MyApplication myApplication = (MyApplication) getApplicationContext();
        savedLocation = myApplication.getLocations();
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

        LatLng lastLoaction=new LatLng(savedLocation.get(0).getLatitude(),savedLocation.get(0).getLongitude());
        int i=0;
        for (Location location:savedLocation) {
            LatLng latLng=new LatLng(location.getLatitude(),location.getLongitude());
            MarkerOptions markerOptions=new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.title("name");
            if(i==0) markerOptions.visible(false);
            else markerOptions.visible(true);
            mMap.addMarker(markerOptions);
            lastLoaction=latLng;
            ++i;
        }
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(lastLoaction,16.0f));
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(@NonNull Marker marker) {
                //further submenu popup

                Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse("google.navigation:q="+marker.getPosition().latitude+","+
                        marker.getPosition().longitude+"&mode=d"));
                intent.setPackage("com.google.android.apps.maps");
                if(intent.resolveActivity(getPackageManager())!=null) startActivity(intent);

                return false;
            }
        });
    }
}