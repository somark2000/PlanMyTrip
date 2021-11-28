package Lab.planmytrip.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

import Lab.planmytrip.AddNewItem;
import Lab.planmytrip.Model.PackageItem;
import Lab.planmytrip.Package;
import Lab.planmytrip.R;

public class PackageItemAdapter extends RecyclerView.Adapter<PackageItemAdapter.ViewHolder> {
    private List<PackageItem> packageItemList;
    private Package activity;

    private FirebaseUser user;
    private FirebaseFirestore db;
    private String userID;

    //mai adauga db la constructor
    public PackageItemAdapter(FirebaseFirestore db, Package activity) {
        this.db = db;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.package_item_layout, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        //db open
        PackageItem packageItem = packageItemList.get(position);
        holder.check_box_item.setText(packageItem.getItemName());
        holder.check_box_item.setChecked(toBoolean(packageItem.getStatus()));
        holder.check_box_item.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //db update status 1
                } else {
                    //db update status 0
                }
            }
        });
    }
    private boolean toBoolean(int n) {
        return n != 0;
    }

    @Override
    public int getItemCount() {
        return packageItemList.size();
    }

    public Context getContext() {
        return activity;
    }

    public void setItem(List<PackageItem> packageItemList) {
        this.packageItemList = packageItemList;
        notifyDataSetChanged();
    }

    public void deleteItem(int position) {
        PackageItem packageItem = packageItemList.get(position);
        //db deleteItem
        packageItemList.remove(position);
        notifyItemRemoved(position);

    }

    public void editItem(int position) {
        PackageItem packageItem = packageItemList.get(position);
        Bundle bundle = new Bundle();
        bundle.putInt("id", packageItem.getId());
        bundle.putString("packageItem", packageItem.getItemName());
        AddNewItem fragment = new AddNewItem();
        fragment.setArguments(bundle);
        fragment.show(activity.getSupportFragmentManager(), AddNewItem.TAG);
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox check_box_item;

        ViewHolder(View view) {
            super(view);
            check_box_item = view.findViewById(R.id.check_box);
        }
    }
}
