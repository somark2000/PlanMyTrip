package Lab.planmytrip;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

import Lab.planmytrip.Model.User;

public class Profile extends AppCompatActivity {

    private TextView title;
    private TextView name;
    private TextView surname;
    private TextView email;
    private TextView passw;
    private TextView phone;
    private TextView bdate;

    private Button logout;
    private Button edit;

    private FirebaseUser user;
    private FirebaseFirestore db;

    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        getSupportActionBar().hide();

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.nav_view);

        db = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        userID = Objects.requireNonNull(user).getUid();

        title = (TextView) findViewById(R.id.title_profile);
        name = (TextView) findViewById(R.id.prenume_p);
        surname = (TextView) findViewById(R.id.nume_p);
        email = (TextView) findViewById(R.id.email_p);
        passw = (TextView) findViewById(R.id.passw_p);
        phone = (TextView) findViewById(R.id.phone_p);
        bdate = (TextView) findViewById(R.id.bdate_p);
        edit = (Button) findViewById(R.id.edit_profile);
        logout = (Button) findViewById(R.id.logout);

        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(2);
        menuItem.setChecked(true);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_map:
                        Intent intent1 = new Intent(Profile.this, Map.class);
                        startActivity(intent1);
                        overridePendingTransition(0, 0);
                        break;
                    case R.id.navigation_profile:
//                        Intent intent2 = new Intent(Trips.this, Profile.class);
//                        startActivity(intent2);
//                        overridePendingTransition(0, 0);
                        break;
                    case R.id.navigation_trips:
                        Intent intent3 = new Intent(Profile.this, Trips.class);
                        startActivity(intent3);
                        overridePendingTransition(0, 0);
                        break;
                    default:
                        throw new IllegalStateException("Unexpected value: " + item.getItemId());
                }
                return false;
            }
        });

        db.collection("users")
                .document(userID)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(@NonNull DocumentSnapshot documentSnapshot) {
                        User curr_user = documentSnapshot.toObject(User.class);
                        if (curr_user != null) {
                            String fullName = curr_user.fname + " " + curr_user.lname;
                            title.setText(String.format("%s %s!", getString(R.string.greeting), fullName));

                            name.setText(curr_user.fname);
                            surname.setText(curr_user.lname);
                            phone.setText(curr_user.phone);
                            email.setText(curr_user.email);
                            passw.setText(curr_user.passw);
                            bdate.setText( String.format("%tF",curr_user.bdate));
                        } else {
                            Toast.makeText(Profile.this, "Something wrong happened!" + userID, Toast.LENGTH_LONG).show();
                        }
                    }
                });

        edit.setText(R.string.edit_profile);
        edit = (Button) findViewById(R.id.edit_profile);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Profile.this, EditProfile.class));
            }
        });

        logout.setText(R.string.logout);
        logout = (Button) findViewById(R.id.logout);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(Profile.this, Login.class));
            }
        });
    }
}