package Lab.planmytrip;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import Lab.planmytrip.Adapter.PackageItemAdapter;
import Lab.planmytrip.Model.PackageItem;
import Lab.planmytrip.Utils.DatabaseHandler;

public class Package extends AppCompatActivity implements DialogCloseListener {

    private FirebaseUser user;
    private FirebaseFirestore db;
    private String userID;

    private DatabaseHandler dbHandler;

    private RecyclerView itemRecyclerView;
    private PackageItemAdapter packageItemAdapter;
    private FloatingActionButton fab;

    private List<PackageItem> packageItemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_package);
        Objects.requireNonNull(getSupportActionBar()).hide();

        db = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        userID = Objects.requireNonNull(user).getUid();

        dbHandler = new DatabaseHandler();

        itemRecyclerView = findViewById(R.id.itemsRecyclerView);
        itemRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        packageItemAdapter = new PackageItemAdapter(db, this);
        itemRecyclerView.setAdapter(packageItemAdapter);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new RecyclerPackageItemTouchHelper(packageItemAdapter));
        itemTouchHelper.attachToRecyclerView(itemRecyclerView);

        fab = findViewById(R.id.fab);

        //packageItemList=dbHandler.getAllPackageItems();
        packageItemList = new ArrayList<>();

        //TODO:caca asta de trip==>comenteaza ca altfel pica
        Trips trips = new Trips();
        String currentTrip=trips.getGetTrip();

        db.collection("users").document(userID)
                .collection("trips").document(currentTrip)
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    Log.e(">>>>>>>> it is OKAY", " documentSnapshot not null");
                    System.out.println(documentSnapshot.getData().get("package"));
                    List<java.util.Map<String, Boolean>> packageArray = (List<java.util.Map<String, Boolean>>) documentSnapshot.getData().get("package");

                    int i = 0;
                    for (java.util.Map<String, Boolean> entry : packageArray) {
                        i++;
                        int id = i;
                        String itemName = String.valueOf(entry.get("itemName"));
                        Boolean status = entry.get("status");
                        PackageItem packageItem = new PackageItem(id, status, itemName);
                        packageItemList.add(packageItem);
                    }

                    //for me, after need -> DELETE
                    Log.e(">>>>>>>> packageItemList before",String.valueOf(packageItemList));
                    System.out.println(packageItemList);
                    Collections.reverse(packageItemList);
                    Log.e(">>>>>>>> packageItemList after",String.valueOf(packageItemList));
                    System.out.println(packageItemList);
                    packageItemAdapter.setItem(packageItemList);
                } else {
                    Log.e(">>>>>>>> error ", " documentSnapshot = null");
                }
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddNewItem.newInstace().show(getSupportFragmentManager(), AddNewItem.TAG);
            }
        });
    }

    @Override
    public void handleDialogClose(DialogInterface dialog) {
        //packageItemList=dbHandler.getAllPackageItems();
        Collections.reverse(packageItemList);
        packageItemAdapter.setItem(packageItemList);
        packageItemAdapter.notifyDataSetChanged();
    }
}