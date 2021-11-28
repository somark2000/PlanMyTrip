package Lab.planmytrip;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import Lab.planmytrip.Adapter.PackageItemAdapter;
import Lab.planmytrip.Model.PackageItem;

public class Package extends AppCompatActivity implements DialogCloseListener {

    private RecyclerView itemRecyclerView;
    private PackageItemAdapter packageItemAdapter;
    private FloatingActionButton fab;

    private List<PackageItem> packageItemList;

    private FirebaseUser user;
    private FirebaseFirestore db;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_package);
        getSupportActionBar().hide();

        packageItemList = new ArrayList<>();

        itemRecyclerView = findViewById(R.id.itemsRecyclerView);
        itemRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        packageItemAdapter = new PackageItemAdapter(db,this);
        itemRecyclerView.setAdapter(packageItemAdapter);

        fab = findViewById(R.id.fab);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new RecyclerPackageItemTouchHelper(packageItemAdapter));
        itemTouchHelper.attachToRecyclerView(itemRecyclerView);

        PackageItem packageItem1 = new PackageItem();
        packageItem1.setItemName("igiena");
        packageItem1.setStatus(0);
        packageItem1.setId(1);

        PackageItem packageItem2 = new PackageItem();
        packageItem2.setItemName("bluza");
        packageItem2.setStatus(0);
        packageItem2.setId(1);

        packageItemList.add(packageItem1);
        packageItemList.add(packageItem2);
        packageItemList.add(packageItem1);
        packageItemList.add(packageItem2);
        packageItemList.add(packageItem1);

        packageItemAdapter.setItem(packageItemList);

        db = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        userID = Objects.requireNonNull(user).getUid();

        //packageItemList get all Items from db

        //Collections.reverse(packageItemList);
        //itemAdapter.setItem(packageItemList);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddNewItem.newInstace().show(getSupportFragmentManager(),AddNewItem.TAG);
            }
        });

    }

    @Override
    public void handleDialogClose(DialogInterface dialog) {
        //packageItemList get all Items from db

        //Collections.reverse(packageItemList);
        //itemAdapter.setItem(packageItemList);
        //itemAdapter.notifyDataSetChanged();
    }
}