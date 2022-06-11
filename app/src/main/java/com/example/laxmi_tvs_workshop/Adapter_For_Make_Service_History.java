package com.example.laxmi_tvs_workshop;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;


public class Adapter_For_Make_Service_History extends RecyclerView.Adapter<Adapter_For_Make_Service_History.Holder> {
    Activity activity;
    DatabaseReference databaseReference;
    public static HashMap<String, DataSnapshot> hashMap;
    public static ArrayList<String> key_holder;
    Calendar calendar;
    String frame_no;
    Dialog service_history_dialog;
    public Adapter_For_Make_Service_History(Activity activity, DatabaseReference databaseReference,String frame_no ) {
        this.activity = activity;
        this.databaseReference = databaseReference;
        this.frame_no = frame_no;
        hashMap = new HashMap<>();
        calendar = Calendar.getInstance();
        key_holder = new ArrayList<>();

        BASIC_DATA_HOLDER.progress_bar();

        Log.d("testing", "Adapter_For_Make_Contact: CALLED");
        databaseReference.child("remarks").child(frame_no).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    if(snapshot.getChildrenCount()>0){
                        databaseReference.child("remarks").child(frame_no).limitToLast(4).addChildEventListener(childEventListener);
                    }else{
                        BASIC_DATA_HOLDER.loading_dialog.dismiss();
                        Toast.makeText(activity, "NO DATA IS AVAILABLE", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    BASIC_DATA_HOLDER.loading_dialog.dismiss();
                    Toast.makeText(activity, "NO DATA IS AVAILABLE", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                BASIC_DATA_HOLDER.loading_dialog.dismiss();
                Toast.makeText(activity, "SOMETHING WENT WRONG", Toast.LENGTH_SHORT).show();
            }
        });


    }


    @Override
    public Adapter_For_Make_Service_History.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_service_history, parent, false);
        Holder holder = new Holder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter_For_Make_Service_History.Holder holder, int position) {
      DataSnapshot snapshot = hashMap.get(key_holder.get(position));
      Remarks_Class remarks_class = snapshot.getValue(Remarks_Class.class);
      holder.date_value.setText(remarks_class.date_of_transaction);
      holder.remark_value.setText(remarks_class.getRemark());
    }

    @Override
    public int getItemCount() {
        return hashMap.size();
    }

    public class Holder extends RecyclerView.ViewHolder {

        TextView date_value;
        TextView remark_value;

        public Holder(@NonNull View itemView) {
            super(itemView);
            date_value = (TextView) itemView.findViewById(R.id.date_value);
            remark_value = (TextView) itemView.findViewById(R.id.remark_value);
        }
    }


    ChildEventListener childEventListener = new ChildEventListener() {
        @Override
        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
          if(previousChildName==null){
              BASIC_DATA_HOLDER.loading_dialog.dismiss();
          }
            hashMap.put(snapshot.getKey(), snapshot);
            Log.d("testing", "onChildAdded: " + snapshot.getKey());
            String key = snapshot.getKey();
            key_holder.add(key);
            notifyDataSetChanged();
        }

        @Override
        public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

        }

        @Override
        public void onChildRemoved(@NonNull DataSnapshot snapshot) {
            String key = snapshot.getKey();
            hashMap.remove(String.valueOf(key));
            key_holder.remove(key);
            notifyDataSetChanged();
        }

        @Override
        public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    };

    public void cleanup() {
        databaseReference.child("remarks").child(frame_no).removeEventListener(childEventListener);
        hashMap.clear();
    }















}

