package Lab.planmytrip;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

import Lab.planmytrip.Model.User;

public class Register extends AppCompatActivity {
    private Button b1;
    private EditText fname, lname, passw1, passw2, mail, bdate, phonenr;

    private DatePickerDialog picker;
    private SimpleDateFormat sdf;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().hide();

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        fname = (EditText) findViewById(R.id.fname);
        lname = (EditText) findViewById(R.id.lname);
        passw1 = (EditText) findViewById(R.id.passw1);
        passw2 = (EditText) findViewById(R.id.passw2);
        mail = (EditText) findViewById(R.id.emailReg);
        phonenr = (EditText) findViewById(R.id.phone);

        bdate = (EditText) findViewById(R.id.bdate);
        bdate.setInputType(InputType.TYPE_NULL);
        bdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(Register.this, new DatePickerDialog.OnDateSetListener() {

                    @SuppressLint("DefaultLocale")
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        bdate.setText(String.format("%d/%d/%d", dayOfMonth, monthOfYear + 1, year));
                    }
                }, year, month, day);
                picker.show();
            }
        });

        b1 = (Button) findViewById(R.id.register_done);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    registerUser();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void registerUser() throws ParseException {
        String s_fname = fname.getText().toString().trim();
        String s_lname = lname.getText().toString().trim();
        String s_passw1 = passw1.getText().toString().trim();
        String s_passw2 = passw2.getText().toString().trim();
        String s_mail = mail.getText().toString().trim();

        sdf = new SimpleDateFormat(getString(R.string.date_format));

        Date s_bdate = sdf.parse(bdate.getText().toString().trim());
        String s_phonenr = phonenr.getText().toString().trim();

        if (s_fname.isEmpty()) {
            fname.setError(getString(R.string.required_firstName));
            fname.requestFocus();
            return;
        }
        if (s_lname.isEmpty()) {
            lname.setError(getString(R.string.required_lastName));
            lname.requestFocus();
            return;
        }
        if (s_passw1.isEmpty()) {
            passw1.setError(getString(R.string.required_password));
            passw1.requestFocus();
            return;
        }
        if(s_passw2.isEmpty()){
            passw2.setError(getString(R.string.required_password2));
            passw2.requestFocus();
            return;
        }
        if (!s_passw1.equals(s_passw2) || s_passw1.length() < 6) {
            passw2.setError(getString(R.string.validation_password));
            passw1.requestFocus();
            passw2.requestFocus();
            return;
        }
        if (s_mail.isEmpty()) {
            mail.setError(getString(R.string.required_email));
            mail.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(s_mail).matches()) {
            mail.setError(getString(R.string.valid_email_R));
            mail.requestFocus();
            return;
        }
        if (s_phonenr.isEmpty()) {
            phonenr.setError(getString(R.string.required_phoneNumber));
            phonenr.requestFocus();
            return;
        }
        if (!Patterns.PHONE.matcher(s_phonenr).matches()) {
            phonenr.setError(getString(R.string.valid_phoneNumber));
            phonenr.requestFocus();
            return;
        }
        if (s_bdate== null) {
            bdate.setError(getString(R.string.required_birthdate));
            bdate.requestFocus();
            return;
        }

        mAuth.createUserWithEmailAndPassword(s_mail, s_passw1)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            User user = new User(s_fname, s_lname, s_mail, s_passw1, s_phonenr, s_bdate);
                            userID = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();

                            db.collection("users").document(userID)
                                    .set(user)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(@NonNull Void aVoid) {
                                            Toast.makeText(Register.this, "User has been registered successfully!", Toast.LENGTH_LONG).show();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(Register.this, "Failed to register!\n Try again!", Toast.LENGTH_LONG).show();
                                        }
                                    });

                            //redirect to login layout!
                            Intent intentr = new Intent(Register.this, Login.class);
                            startActivity(intentr);
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(Register.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}