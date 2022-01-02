package Lab.planmytrip;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import Lab.planmytrip.Model.MyApplication;
import Lab.planmytrip.Model.PackageItem;

public class AddNewItemActivity extends AppCompatActivity {

    private EditText newItemText;
    private Button newItemSaveButton;

    private FirebaseUser user;
    private FirebaseFirestore db;
    private String userID;

    private List<PackageItem> packageItemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_item);
        getSupportActionBar().hide();


        newItemText = (EditText) findViewById(R.id.tb_package_item);
        newItemSaveButton = findViewById(R.id.save_button);

        db = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        userID = Objects.requireNonNull(user).getUid();

        MyApplication myApplication = (MyApplication) getApplicationContext();
        String currentTrip = myApplication.getTripID();

        packageItemList = new ArrayList<>();

        if (getIntent().hasExtra("itemName")) {
            newItemText.setText(getIntent().getExtras().getString("itemName"));
        }

        db.collection("users").document(userID)
                .collection("trips").document(currentTrip)
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(@NonNull DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    Log.e(">>>>>>>> eeedit:save ", " documentSnapshot not null");
                    List<java.util.Map<String, Boolean>> packageArray = (List<java.util.Map<String, Boolean>>) Objects.requireNonNull(documentSnapshot.getData()).get("baggage"); //package

                    int i = 0;
                    if (packageArray != null) {
                        for (java.util.Map<String, Boolean> entry : packageArray) {
                            i++;
                            int id = i;
                            String itemName = String.valueOf(entry.get("itemName"));
                            Boolean status = entry.get("status");
                            if (!itemName.equals("empty")) {
                                PackageItem packageItem = new PackageItem(id, status, itemName);
                                packageItemList.add(packageItem);
                            }
                        }
                    }
                }
            }
        });

        boolean isUpdate = false;
        int ID = -1;
        if (getIntent().hasExtra("id")) {
            ID = getIntent().getExtras().getInt("id");
            isUpdate = true;
        }

        boolean finalIsUpdate = isUpdate;
        int finalID = ID;
        newItemSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String newItemName = newItemText.getText().toString();
                if (finalIsUpdate) {//update packageItem
                    Log.e(">>>>>>>> eeedit", finalID + "");
                    System.out.println(finalID);

                    Log.e("...", packageItemList.size() + "");
                    for (int i = 0; i < packageItemList.size(); i++) {
                        if (i == finalID) {
                            packageItemList.get(i).setItemName(newItemName);
                        }
                    }
                    db.collection("users").document(userID)
                            .collection("trips").document(currentTrip)
                            .update("baggage", packageItemList); //package
                } else {//insert packageItem
                    PackageItem packageItem = new PackageItem();
                    packageItem.setItemName(newItemName);
                    packageItem.setStatus(false);

                    Object itemToBeSaved = new Object() {
                        public String itemName = newItemName;
                        public boolean status = false;
                    };
                    db.collection("users").document(userID)
                            .collection("trips").document(currentTrip)
                            .update("baggage", FieldValue.arrayUnion(itemToBeSaved)); //package
                }
                finish();
            }
        });
    }
}