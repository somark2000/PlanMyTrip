package Lab.planmytrip;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import Lab.planmytrip.Model.MyApplication;
import Lab.planmytrip.Model.Trip;

public class AddTripActivity extends AppCompatActivity {
    //UI
    private EditText editText;
    private Button button;

    //DB
    private DocumentReference documentReference;
    private FirebaseFirestore db;
    private FirebaseUser user;
    private String userID;
    private String tripID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_trip);
        getSupportActionBar().hide();

        editText = findViewById(R.id.trip_name);
        button = findViewById(R.id.new_trip);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editText.getText().toString().trim();
                if (name.isEmpty()) {
                    editText.setError("Name required");
                    editText.requestFocus();
                    return;
                }
                db = FirebaseFirestore.getInstance();
                user = FirebaseAuth.getInstance().getCurrentUser();
                userID = Objects.requireNonNull(user).getUid();
                MyApplication myApplication = (MyApplication) getApplicationContext();
                tripID = myApplication.getTripID();
                ArrayList<GeoPoint> checkpoints = new ArrayList<>();
                ArrayList<Map<String, Object>> packages = new ArrayList<>();
                checkpoints.add(new GeoPoint(0.1, 0.1));
                java.util.Map<String, Object> pack = new HashMap<>();
                pack.put("itemName", "empty");
                pack.put("status", false);
                packages.add(pack);
                Trip trip = new Trip();
                trip.setName(name);
                trip.setCheckpoints(checkpoints);
                trip.setBaggage(packages);
                db.collection("users").document(userID)
                        .collection("trips").add(trip)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(@NonNull DocumentReference documentReference) {
                                Log.d("AddTripActivity: yay", "DocumentSnapshot written with ID: " + documentReference.getId());
                                Intent intent = new Intent(AddTripActivity.this, Trips.class);
                                startActivity(intent);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w("AddTripActivity: sad", "Error adding document", e);
                            }
                        });
            }
        });
    }
}