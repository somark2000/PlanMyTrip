package Lab.planmytrip.Utils;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import Lab.planmytrip.Model.PackageItem;

public class DatabaseHandler {

    private FirebaseUser user;
    private FirebaseFirestore db;
    private String userID;

    public DatabaseHandler() {
        this.db = FirebaseFirestore.getInstance();
        this.user = FirebaseAuth.getInstance().getCurrentUser();
        this.userID = Objects.requireNonNull(user).getUid();
    }

//    public void insertPackageItem(PackageItem packageItem) {
//        java.util.Map<String, java.util.Map<Integer, Integer>> packageMap = Map.of({packageItem.getItemName(),{"id",String.valueOf(packageItem.getId()), "status",String.valueOf(packageItem.getStatus())}});
//
//
//        db.collection("users").document(userID)
//                .collection("trips").document("MexKkh3whqgoo6joLJf1")
//                .child().setValueAsync(new PackageItem("December 9, 1906", "Grace Hopper"));
////                .set(packageMap)
////                .addOnSuccessListener(new OnSuccessListener<Void>() {
////                    @Override
////                    public void onSuccess(Void aVoid) {
////                        //Toast.makeText(, "User has been registered successfully!", Toast.LENGTH_LONG).show();
////                    }
////                })
////                .addOnFailureListener(new OnFailureListener() {
////                    @Override
////                    public void onFailure(@NonNull Exception e) {
////                        //Toast.makeText(Register.this, "Failed to register!\n Try again!", Toast.LENGTH_LONG).show();
////                    }
////                });
//    }

//    public List<PackageItem> getAllPackageItems() {
//        List<PackageItem> packageItemList = new ArrayList<>();
//
//        db.collection("users").document(userID)
//                .collection("trips").document("MexKkh3whqgoo6joLJf1")
//                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//            @Override
//            public void onSuccess(DocumentSnapshot documentSnapshot) {
//                if (documentSnapshot.exists()) {
//                    Log.e(">>>>>>>> it is OKAY", " documentSnapshot not null");
//                    System.out.println(documentSnapshot.getData().get("package"));
////                    java.util.Map<String, java.util.Map<Long, Long>> packageMap =
////                            (java.util.Map<String, java.util.Map<Long, Long>>) documentSnapshot.getData().get("package");
////                    assert packageMap != null;
////                    for (java.util.Map.Entry<String, java.util.Map<Long, Long>> entry : packageMap.entrySet()) {
////                        String itemName = entry.getKey();
////                        java.util.Map<Long, Long> entryValue = entry.getValue();
////                        Long id = entryValue.get("id");
////                        Long status = entryValue.get("status");
////                        PackageItem packageItem = new PackageItem(id.intValue(), status.intValue(), itemName);
////                        packageItemList.add(packageItem);
////                    }
//                    //for me, after need -> DELETE
//                    Log.e(">>>>>>>> packageItemList", String.valueOf(packageItemList));
//                } else {
//                    Log.e(">>>>>>>> error ", " documentSnapshot = null");
//                }
//            }
//        });
//        Log.e(">>>>>>>> packageItemList in handler dupa func", String.valueOf(packageItemList));
//        return packageItemList;
//    }

    public void updateStatus(int id, int status) {

    }

    public void updateItemName(int id, String itemName) {

    }

    public void deletePackageItem(int id) {

    }
}
