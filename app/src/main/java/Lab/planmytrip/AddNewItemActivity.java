package Lab.planmytrip;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
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
import java.util.Map;
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


        newItemText = (EditText) findViewById(R.id.tb_package_item);
        newItemSaveButton = findViewById(R.id.save_button);

        db = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        userID = Objects.requireNonNull(user).getUid();

        MyApplication myApplication = (MyApplication) getApplicationContext();
        String currentTrip = myApplication.getTripID();

        packageItemList = new ArrayList<>();

        db.collection("users").document(userID)
                .collection("trips").document(currentTrip)
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    Log.e(">>>>>>>> eeedit", " documentSnapshot not null");
                    List<java.util.Map<String, Boolean>> packageArray = (List<java.util.Map<String, Boolean>>) documentSnapshot.getData().get("package");

                    int i = 0;
                    assert packageArray != null;
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
                            .update("package", packageItemList);
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
                            .update("package", FieldValue.arrayUnion(itemToBeSaved));
                }
                finish();
            }
        });
    }
}