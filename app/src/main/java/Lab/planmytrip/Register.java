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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.Objects;

public class Register extends AppCompatActivity {
    Button b1;
    EditText fname, lname, passw1, passw2, mail, bdate, phonenr;//fname,lname,uname, passw,repassw,mail,date
    DatePickerDialog picker;

    //progressbar????

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

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
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        bdate.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                    }
                }, year, month, day);
                picker.show();
            }
        });

        b1 = (Button) findViewById(R.id.register_done);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }
        });
    }

    private void registerUser() {
        String s_fname = fname.getText().toString().trim();
        String s_lname = lname.getText().toString().trim();
        String s_passw1 = passw1.getText().toString().trim();
        String s_passw2 = passw2.getText().toString().trim();
        String s_mail = mail.getText().toString().trim();
        String s_bdate = bdate.getText().toString().trim();
        String s_phonenr = phonenr.getText().toString().trim();

        if (s_fname.isEmpty()) {
            fname.setError("First name is required!");
            fname.requestFocus();
            return;
        }
        if (s_lname.isEmpty()) {
            lname.setError("Last name is required!");
            lname.requestFocus();
            return;
        }
        if (s_passw1.isEmpty()) {
            passw1.setError("Password is required!");
            passw1.requestFocus();
            return;
        }
//        if(s_passw2.isEmpty()){
//            passw2.setError("2nd Password is required!");
//            passw2.requestFocus();
//            return;
//        }
        if (!s_passw1.equals(s_passw2) || s_passw1.length() < 6) {
            passw2.setError("Passwords should be the same and longer than 6 characters!");
            passw1.requestFocus();
            passw2.requestFocus();
            return;
        }if (s_mail.isEmpty()) {
            mail.setError("Email is required!");
            mail.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(s_mail).matches()) {
            mail.setError("Please provide an valid email!\nEx: abc@abc.abc");
            mail.requestFocus();
            return;
        }if (s_phonenr.isEmpty()) {
            phonenr.setError("Phone number is required!");
            phonenr.requestFocus();
            return;
        }
        if(!Patterns.PHONE.matcher(s_phonenr).matches()){
            phonenr.setError("Please provide an valid phone number!");
            phonenr.requestFocus();
            return;
        }
        if (s_bdate.isEmpty()) {
            bdate.setError("Birthdate is required!");
            bdate.requestFocus();
            return;
        }
        mAuth.createUserWithEmailAndPassword(s_mail, s_passw1)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            User user = new User(s_fname, s_lname, s_passw1, s_mail, s_bdate, s_phonenr);

                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(Register.this, "User has been registered successfully!", Toast.LENGTH_LONG).show();

                                        //redirect to login layout!
                                        Intent intentr = new Intent(Register.this, Login.class);
                                        startActivity(intentr);
                                    } else {
                                        Toast.makeText(Register.this, "Failed to register!\n Try again!", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(Register.this, "Failed to register!\n Try again!", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}