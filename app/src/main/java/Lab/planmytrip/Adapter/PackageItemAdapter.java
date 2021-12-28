package Lab.planmytrip.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;
import java.util.List;

import Lab.planmytrip.AddNewItemActivity;
import Lab.planmytrip.Model.PackageItem;
import Lab.planmytrip.TripPackage;
import Lab.planmytrip.R;

public class PackageItemAdapter extends RecyclerView.Adapter<PackageItemAdapter.ViewHolder> {
    private List<PackageItem> packageItemList;
    private TripPackage activity;

    public PackageItemAdapter(TripPackage activity) {
        this.activity = activity;
        this.packageItemList = new ArrayList<>();
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
        PackageItem packageItem = packageItemList.get(position);
        holder.check_box_item.setText(packageItem.getItemName());
        holder.check_box_item.setChecked(packageItem.getStatus());
        holder.check_box_item.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    activity.updateStatus(packageItem.getId(), true);
                } else {
                    activity.updateStatus(packageItem.getId(), false);
                }
            }
        });
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
        activity.deleteItem(position);
        notifyItemRemoved(position);
    }

    public void editItem(int position) {
        //PackageItem packageItem = packageItemList.get(position);
        Intent switchActivityIntent = new Intent(getContext(), AddNewItemActivity.class);
        switchActivityIntent.putExtra("id", position);
        getContext().startActivity(switchActivityIntent);
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        CheckBox check_box_item;

        ViewHolder(View view) {
            super(view);
            check_box_item = view.findViewById(R.id.check_box);
        }
    }
}