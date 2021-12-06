package Lab.planmytrip;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

import Lab.planmytrip.Model.MyApplication;
import Lab.planmytrip.Model.PackageItem;

public class AddNewItemActivity extends AppCompatActivity {

    private EditText newItemText;
    private Button newItemSaveButton;

    private FirebaseUser user;
    private FirebaseFirestore db;
    private String userID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_item);


        newItemText = (EditText) findViewById(R.id.tb_package_item);
        newItemSaveButton = findViewById(R.id.save_button);

        db = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        userID = Objects.requireNonNull(user).getUid();

        MyApplication myApplication = (MyApplication) getApplicationContext();
        String currentTrip = myApplication.getTripID();

        boolean finalIsUpdate = false;
        newItemSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = newItemText.getText().toString();
                if (finalIsUpdate) {
                    Log.e("AddNewItem:>>>>>>>> it is OKAY", "???");
//                    System.out.println(bundle.getInt("id") - 1); //ia id-ul(indexul) de la elementul pe care vrei sa il modifici
                    //update
                    //todo: aici vezi cum faci navigation cu parametrii unde pasezi id-ul de la item pentru edit
                    //todo: aici nu e chiar aici, iei si tu datele undeva mai sus si in functie de asta setezi si finalIsUpdate

                } else {
                    PackageItem packageItem = new PackageItem();
                    packageItem.setItemName(text);
                    packageItem.setStatus(false);
                    //insert packageItem
                    Object itemToBeSaved = new Object() {
                        public String itemName = text;
                        public boolean status = true;
                    };
                    db.collection("users").document(userID)
                            .collection("trips").document(currentTrip)
                            .update("package", FieldValue.arrayUnion(itemToBeSaved));
                }
//                dismiss();
            }
        });
    }
}