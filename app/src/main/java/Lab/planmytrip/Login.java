package Lab.planmytrip;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {

    Button b1, b2;
    EditText ed1, ed2;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        b1 = (Button) findViewById(R.id.login);
        ed1 = (EditText) findViewById(R.id.email);
        ed2 = (EditText) findViewById(R.id.passw);

        b2 = (Button) findViewById(R.id.register_goto);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userLogin();
            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openRegister();
            }
        });

        mAuth = FirebaseAuth.getInstance();
        //autologin
        if (mAuth.getCurrentUser() != null) {
            //user is signed in
            Toast.makeText(getApplicationContext(), "Redirecting...", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(Login.this, Trips.class));
            finish();
        }
    }

    private void openRegister() {
        Intent intent = new Intent(this, Register.class);
        startActivity(intent);
    }

    private void userLogin() {
        String email = ed1.getText().toString().trim();
        String passw = ed2.getText().toString().trim();

        if (email.isEmpty()) {
            ed1.setError(getString(R.string.required_email));
            ed1.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            ed1.setError(getString(R.string.valid_email));
            ed1.requestFocus();
            return;
        }
        if (passw.isEmpty()) {
            ed2.setError(getString(R.string.required_password));
            ed2.requestFocus();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, passw).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    //redirect to home
                    Toast.makeText(getApplicationContext(), "Redirecting...", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(Login.this, Trips.class));
                } else {
                    Toast.makeText(getApplicationContext(), "Failed to login!\n Please check your credentials.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}



