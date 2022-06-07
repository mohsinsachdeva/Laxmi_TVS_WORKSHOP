package com.example.laxmi_tvs_workshop;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.rpc.context.AttributeContext;

import org.w3c.dom.Text;

import java.util.Objects;

public class Make_Contact extends AppCompatActivity {
ImageButton go_back_home;
androidx.recyclerview.widget.RecyclerView customer_list;
DatabaseReference databaseReference;
RecyclerView.LayoutManager mLayoutManager;
Adapter_For_Make_Contact adapter_for_make_contact;
public static TextView counter_text_box;
Dialog  successDialog;
public static final int REQUEST_CODE = 123;
public static boolean sending_message = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_contact);

        databaseReference = FirebaseDatabase.getInstance("https://laxmi-tvs-workshop-default-rtdb.asia-southeast1.firebasedatabase.app").getReference();
        go_back_home = (ImageButton) findViewById(R.id.go_back_home);
        customer_list = (androidx.recyclerview.widget.RecyclerView) findViewById(R.id.customer_list);
        mLayoutManager = new LinearLayoutManager(this,RecyclerView.VERTICAL,false);
        successDialog = new Dialog(this);
        successDialog.setContentView(R.layout.alert_update_sms_list);
        successDialog.setCancelable(true);
        counter_text_box = (TextView)findViewById(R.id.counter_text_box);


        go_back_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent go_to_settings_intent= new Intent(Make_Contact.this, MainActivity.class);
                go_to_settings_intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(go_to_settings_intent);
                finish();
            }
        });

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this,DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(this,R.drawable.divider));
        customer_list.addItemDecoration(dividerItemDecoration);

        successDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                DataSnapshot snapshot = Adapter_For_Make_Contact.hashMap.get(Adapter_For_Make_Contact.key_holder.get(0));
                String key = snapshot.getKey();
                remove_after_message_sent(key);
            }
        });


    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        if(!sending_message){
            adapter_for_make_contact = new Adapter_For_Make_Contact(this, databaseReference);
            customer_list.setLayoutManager(mLayoutManager);
            customer_list.setAdapter(adapter_for_make_contact);
        }else if (sending_message && Adapter_For_Make_Contact.hashMap.size()>0){
            TextView tittle = (TextView) successDialog.findViewById(R.id.success_alert_tittle);
            Objects.requireNonNull(successDialog.getWindow()).setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
            Button ok_button = (Button) successDialog.findViewById(R.id.add_to_sms_list);
            ok_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DataSnapshot snapshot = Adapter_For_Make_Contact.hashMap.get(Adapter_For_Make_Contact.key_holder.get(0));
                    String key = snapshot.getKey();
                    Customer_Class customer_class = snapshot.getValue(Customer_Class.class);
                    databaseReference.child("SMSLIST").child(customer_class.getFrame_no()).setValue(customer_class);
                    remove_after_message_sent(key);
                    successDialog.dismiss();
                }
            });

            successDialog.show();
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        if(adapter_for_make_contact != null && !sending_message){
            adapter_for_make_contact.cleanup();
        }
    }

    public void remove_after_message_sent(String  key){
        Log.d("remove", "remove_after_message_sent: " + key);
        databaseReference.child(BASIC_DATA_HOLDER.getCalling_type()).child(key).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d("remove", "removed successfully ");
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("activityresult", "onActivityResult: request code "+ requestCode);
        Log.d("activityresult", "onActivityResult: request code "+ resultCode);

        if(data!=null){
            Log.d("activityresult", "onActivityResult: "+ data.getData().toString());
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        BASIC_DATA_HOLDER.setCalling_type("");
        Intent intent = new Intent(Make_Contact.this, Home_page.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
    }
}