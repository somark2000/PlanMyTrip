package Lab.planmytrip;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Objects;

import Lab.planmytrip.Model.User;

public class EditProfile extends AppCompatActivity {

    private EditText name;
    private EditText surname;
    private EditText email;
    private EditText passw;
    private EditText phone;
    private EditText bdate;

    private Button save;

    private FirebaseUser user;
    private FirebaseFirestore db;

    private String userID;
    private User curr_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        getSupportActionBar().hide();

        db = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        userID = Objects.requireNonNull(user).getUid();

        name = (EditText) findViewById(R.id.prenume_e_p);
        surname = (EditText) findViewById(R.id.nume_e_p);
        email = (EditText) findViewById(R.id.email_e_p);
        passw = (EditText) findViewById(R.id.passw_e_p);
        phone = (EditText) findViewById(R.id.phone_e_p);
        bdate = (EditText) findViewById(R.id.bdate_e_p);

        db.collection("users")
                .document(userID)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        curr_user = documentSnapshot.toObject(User.class);
                        if (curr_user != null) {

                            name.setText(curr_user.fname);
                            surname.setText(curr_user.lname);
                            phone.setText(curr_user.phone);
                            email.setText(curr_user.email);
                            passw.setText(curr_user.passw);
                            bdate.setText( String.format("%tF",curr_user.bdate));
                        } else {
                            Toast.makeText(EditProfile.this, "Something wrong happened!" + userID, Toast.LENGTH_LONG).show();
                        }
                    }
                });

        save = (Button) findViewById(R.id.save_profile);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)  {
                curr_user.setFname(name.getText().toString().trim());
                curr_user.setLname(surname.getText().toString().trim());
                curr_user.setEmail(email.getText().toString().trim());
                curr_user.setPassw(passw.getText().toString().trim());
                curr_user.setPhone(phone.getText().toString().trim());
                SimpleDateFormat sdf=new SimpleDateFormat(getString(R.string.date_format_edit));
                try {
                    curr_user.setBdate(sdf.parse(bdate.getText().toString().trim()));

                } catch (ParseException e) {
                    e.printStackTrace();
                }

                db.collection("users")
                        .document(userID)
                        .set(curr_user)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(EditProfile.this, "User has been updated successfully!", Toast.LENGTH_LONG).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(EditProfile.this, "Failed to update!", Toast.LENGTH_LONG).show();
                            }
                        });
                finish();
                startActivity(new Intent(EditProfile.this, Profile.class));
            }
        });


    }
}