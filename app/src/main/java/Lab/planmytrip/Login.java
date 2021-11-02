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

    Button b1,b2;
    EditText ed1,ed2;

    private FirebaseAuth mAuth;
    //progressbar????

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        b1 = (Button)findViewById(R.id.login);
        ed1 = (EditText)findViewById(R.id.email);
        ed2 = (EditText)findViewById(R.id.passw);

        b2 = (Button)findViewById(R.id.register_goto);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { userLogin(); }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openRegister();
            }
        });

        mAuth=FirebaseAuth.getInstance();
        //autologin
        if(mAuth.getCurrentUser()!=null){
            //user is signed in
            Toast.makeText(getApplicationContext(),"Redirecting...",Toast.LENGTH_SHORT).show();
            startActivity(new Intent(Login.this,Index.class));
            finish();
        }
    }

    private void openRegister(){
        Intent intent=new Intent(this,Register.class);
        startActivity(intent);
    }

    private void userLogin(){
        String email=ed1.getText().toString().trim();
        String passw=ed2.getText().toString().trim();

        if(email.isEmpty()){
            ed1.setError("Email is required!");
            ed1.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            ed1.setError("Please enter a valid email!");
            ed1.requestFocus();
            return;
        }
        if(passw.isEmpty()){
            ed2.setError("Password is required!");
            ed2.requestFocus();
            return;
        }

        mAuth.signInWithEmailAndPassword(email,passw).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    //redirect to home
                    Toast.makeText(getApplicationContext(),"Redirecting...",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(Login.this,Index.class));
                }else{
                    Toast.makeText(getApplicationContext(), "Failed to login!\n Please check your credentials.",Toast.LENGTH_LONG).show();
                }
            }
        });
//        if(uname.equals("admin") &&passw.equals("admin"))
//            Toast.makeText(getApplicationContext(),"Redirecting...",Toast.LENGTH_SHORT).show();
//        else Toast.makeText(getApplicationContext(), "Wrong Credentials",Toast.LENGTH_SHORT).show();
    }
}



