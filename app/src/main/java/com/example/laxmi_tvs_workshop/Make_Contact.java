package com.example.laxmi_tvs_workshop;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.rpc.context.AttributeContext;

import org.w3c.dom.Text;

import java.util.Calendar;
import java.util.Objects;

public class Make_Contact extends AppCompatActivity {
    public static final String KEY_NAME_CALLS = "calls";
ImageButton go_back_home;
public static androidx.recyclerview.widget.RecyclerView customer_list;
DatabaseReference databaseReference;
public static RecyclerView.LayoutManager mLayoutManager;
Adapter_For_Make_Contact adapter_for_make_contact;
public static TextView counter_text_box;
public static TextView call_counter;
Dialog  successDialog;
public static final int REQUEST_CODE = 123;
public static boolean sending_message = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_contact);
        BASIC_DATA_HOLDER.setActivity(this);

        databaseReference = FirebaseDatabase.getInstance("https://laxmi-tvs-workshop-default-rtdb.asia-southeast1.firebasedatabase.app").getReference();
        call_counter = (TextView)findViewById(R.id.call_counter);
        customer_list = (androidx.recyclerview.widget.RecyclerView) findViewById(R.id.customer_list);
        mLayoutManager = new LinearLayoutManager(this,RecyclerView.VERTICAL,false);
        successDialog = new Dialog(this);
        successDialog.setContentView(R.layout.alert_update_sms_list);
        successDialog.setCancelable(true);
        counter_text_box = (TextView)findViewById(R.id.counter_text_box);

        
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this,DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(this,R.drawable.divider));
        customer_list.addItemDecoration(dividerItemDecoration);

       /* successDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                DataSnapshot snapshot = Adapter_For_Make_Contact.hashMap.get(Adapter_For_Make_Contact.key_holder.get(0));
                String key = snapshot.getKey();
                remove_after_message_sent(key);
            }
        });
*/

    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref",MODE_PRIVATE);
        SharedPreferences.Editor myEdit = sharedPreferences.edit();
        Log.d("testing", "onStop: " + sending_message);
// Storing the key and its value as the data fetched from edittext
        myEdit.putBoolean("sendingmessage", sending_message);
        myEdit.commit();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        counter_check();
        SharedPreferences sh = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        sending_message = sh.getBoolean("sendingmessage",false);
        Log.d("testing", "onPostResume: "+ sending_message);
        if(!sending_message ){
            adapter_for_make_contact = new Adapter_For_Make_Contact(this, databaseReference);
            customer_list.setLayoutManager(mLayoutManager);
            customer_list.setAdapter(adapter_for_make_contact);
        }/*else if (sending_message && Adapter_For_Make_Contact.hashMap.size()>0){
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
        }*/
        if(!BASIC_DATA_HOLDER.isMessage_mode()){
            if(sending_message && adapter_for_make_contact!=null){
                adapter_for_make_contact.last_call_details();
                try {
                    BASIC_DATA_HOLDER.progress_bar();
                    Thread.sleep(3000);
                    DataSnapshot snapshot = Adapter_For_Make_Contact.hashMap.get(Adapter_For_Make_Contact.key_holder.get(0));
                    assert snapshot != null;
                    Customer_Class customer_class = snapshot.getValue(Customer_Class.class);
                    Log.d("testing", "onPostResume: "+ Call_Details.getDuration());
                    assert customer_class != null;
                    if(Long.parseLong( Call_Details.getCall_number()) == (customer_class.getMobile_1()) ||
                            Long.parseLong( Call_Details.getCall_number()) == customer_class.getMobile_2()){
                        Log.d("testing", "onPostResume: Number matched ");
                        long duration = Long.parseLong(Call_Details.getDuration());
                        if(duration > 4){
                            Log.d("testing", "onPostResume: duration matched ");
                            update_counter();
                            BASIC_DATA_HOLDER.loading_dialog.dismiss();
                        }else{
                            Log.d("testing", "onPostResume: duration not matched ");
                            BASIC_DATA_HOLDER.loading_dialog.dismiss();

                        }
                    }else{
                        Log.d("testing", "onPostResume: Number NOT matched ");
                        BASIC_DATA_HOLDER.loading_dialog.dismiss();

                    }
                } catch (InterruptedException e) {
                    Log.d("testing", "onPostResume: Sleep error ");
                    e.printStackTrace();
                    BASIC_DATA_HOLDER.loading_dialog.dismiss();

                }
            }
        }else{
            if(sending_message && adapter_for_make_contact!=null){
                String key =  Adapter_For_Make_Contact.hashMap.get(Adapter_For_Make_Contact.key_holder.get(0)).getKey();
                remove_after_message_sent(key);
            }

        }

    }

    @Override
    protected void onStop() {
        super.onStop();


        if(adapter_for_make_contact != null && !sending_message){
            adapter_for_make_contact.cleanup();
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

    public void counter_check() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        long today = cal.getTimeInMillis();
        Log.d("counter", "Todays date at start: " + today);

        String string_today = String.valueOf(today);
        databaseReference.child("Counter").child(string_today).child(KEY_NAME_CALLS).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Make_Contact.call_counter.setText(snapshot.getValue(String.class));
                } else {
                    databaseReference.child("Counter").child(string_today).child(KEY_NAME_CALLS).setValue("0");
                    Make_Contact.call_counter.setText("0");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Make_Contact.call_counter.setText("0");
            }
        });
    }

    public void update_counter() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        long today = cal.getTimeInMillis();
        Log.d("counter", "On Update: " + today);
        String string_today = String.valueOf(today);
        int current = Integer.parseInt(Make_Contact.call_counter.getText().toString());
        int new_counter = current + 1;
        databaseReference.child("Counter").child(string_today).child(KEY_NAME_CALLS).setValue(String.valueOf(new_counter));
        sending_message = false;
     }
    public void remove_after_message_sent(String key) {
        Log.d("remove", "remove_after_message_sent: " + key);
        databaseReference.child(BASIC_DATA_HOLDER.getCalling_type()).child(key).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d("remove", "removed successfully ");
                BASIC_DATA_HOLDER.loading_dialog.dismiss();
                Make_Contact.sending_message = false;
            }
        });
    }
}