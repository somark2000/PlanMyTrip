package Lab.planmytrip;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class Home extends AppCompatActivity {

    private TextView title;
    private Button btn;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        title=(TextView) findViewById(R.id.textView);
        btn=(Button) findViewById(R.id.button);

        title.setText("Hello "+"@User");
        btn.setText("LOGIN");
    }
}