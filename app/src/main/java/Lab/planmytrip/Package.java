package Lab.planmytrip;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class Package extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_package);
        getSupportActionBar().hide();
    }
}