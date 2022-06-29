package com.example.laxmi_tvs_workshop;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Objects;

public class Make_Contact extends AppCompatActivity {
    public static final String KEY_NAME_CALLS = "calls";
    public static final String LOGS_DATABASE_REFERENCE = "LOGS";
    public static final int MINIMUM_CALL_DURATION =20;
ImageButton go_back_home;
public static androidx.recyclerview.widget.RecyclerView customer_list;
DatabaseReference databaseReference;
public static LinearLayoutManager mLayoutManager;
Adapter_For_Make_Contact adapter_for_make_contact;
public static TextView counter_text_box;
public static TextView call_counter;
Dialog  successDialog;
public static final int REQUEST_CODE = 123;
public static boolean sending_message = false;
ImageButton filter_button;
ImageButton remove_filter_button;
SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yy");
TextView header_title;
public static boolean mobile_one_value;
public static boolean mobile_two_value;
public static String last_attempt = "";





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
        filter_button = (ImageButton)findViewById(R.id.filter_button);
        filter_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.HOUR, 0);
                cal.set(Calendar.MINUTE, 0);
                cal.set(Calendar.SECOND, 0);
                cal.set(Calendar.MILLISECOND, 0);


                DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        cal.set(Calendar.YEAR,year);
                        cal.set(Calendar.MONTH,month);
                        cal.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                        long date_value = cal.getTimeInMillis();
                        Log.d("date_search", "onDateSet: " + dateFormat.format(date_value));
                       adapter_for_make_contact.search(dateFormat.format(date_value));
                    }
                };
                new DatePickerDialog(Make_Contact.this,android.R.style.Theme_Holo_Light_Dialog_MinWidth,dateSetListener,cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        
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
        remove_filter_button = (ImageButton) findViewById(R.id.remove_filter_button);
        remove_filter_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter_for_make_contact.search("");
            }
        });

        header_title = (TextView) findViewById(R.id.header_title);
        header_title.setText(BASIC_DATA_HOLDER.getCalling_type() + "_" + BASIC_DATA_HOLDER.getUser());
        call_counter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                counter_roll_over();
            }
        });
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
       counter_roll_over();

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
        databaseReference.child("Counter").child(string_today).child(BASIC_DATA_HOLDER.getUser()).child(KEY_NAME_CALLS).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Make_Contact.call_counter.setText(snapshot.getValue(String.class));
                } else {
                    databaseReference.child("Counter").child(string_today).child(BASIC_DATA_HOLDER.getUser()).child(KEY_NAME_CALLS).setValue("0");
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
        databaseReference.child("Counter").child(string_today).child(BASIC_DATA_HOLDER.getUser()).child(KEY_NAME_CALLS).setValue(String.valueOf(new_counter));
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

    public void counter_roll_over(){
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
                BASIC_DATA_HOLDER.progress_bar();
                int postion = Adapter_For_Make_Contact.active_view_position;

                DataSnapshot snapshot;
                if(Adapter_For_Make_Contact.filter_value.equals("")){
                    snapshot = Adapter_For_Make_Contact.hashMap.get(Adapter_For_Make_Contact.key_holder.get(postion));
                    assert snapshot != null;
                }else{
                    snapshot = Adapter_For_Make_Contact.searchviewMap.get(Adapter_For_Make_Contact.search_view_key_holder_array.get(postion));
                    assert snapshot != null;
                }


                Customer_Class customer_class = snapshot.getValue(Customer_Class.class);

                assert customer_class != null;
                if(Adapter_For_Make_Contact.global_mobile1_selection){
                    if(Long.parseLong( Call_Details.getCall_number()) == (customer_class.getMobile_1())){
                        Log.d("last_call_make_contact", "mobile 1 called" + customer_class.getMobile_1());
                        Log.d("last_call_make_contact", "number received from call details" + Call_Details.getCall_number());
                        long duration = Long.parseLong(Call_Details.getDuration());
                        if(duration > MINIMUM_CALL_DURATION){
                            Log.d("last_call_make_contact", "duration has been matched");
                            Adapter_For_Make_Contact.last_dialled_number = String.valueOf(Adapter_For_Make_Contact.customer_class_gloabl.getMobile_1()) ;
                            Log.d("last_call_make_contact", "last dialled value set" + Adapter_For_Make_Contact.last_dialled_number);
                            update_counter();
                            Call_Details.setDuration("0");
                            Call_Details.setCall_number("0");
                            Call_Details.setCall_time_stamp("0");
                            Calendar calendar = Calendar.getInstance();
                            calendar.set(Calendar.HOUR,0);
                            calendar.set(Calendar.MINUTE,0);
                            calendar.set(Calendar.SECOND,0);
                            calendar.set(Calendar.MILLISECOND,0);
                            String frame = Adapter_For_Make_Contact.customer_class_gloabl.getFrame_no();
                            String contact_date = String.valueOf(calendar.getTimeInMillis());
                            String contact_number = "";
                            String current_time = String.valueOf(Calendar.getInstance().getTimeInMillis()) ;
                            if(Adapter_For_Make_Contact.global_mobile1_selection){
                                contact_number =  String.valueOf(Adapter_For_Make_Contact.customer_class_gloabl.getMobile_1());
                            }
                            if(Adapter_For_Make_Contact.global_mobile2_selection){
                                contact_number =  String.valueOf(Adapter_For_Make_Contact.customer_class_gloabl.getMobile_2());
                            }
                            //String user, String frame_no, String message, String contact_date, String contact_type, String contact_number
                            LOG_CLASS  log_class = new LOG_CLASS(BASIC_DATA_HOLDER.getUser(),frame,"",contact_date,"PHONE_CALL_CONNECT",contact_number );
                            databaseReference.child(LOGS_DATABASE_REFERENCE).child(contact_number).child(String.valueOf(current_time)).setValue(log_class);
                            BASIC_DATA_HOLDER.loading_dialog.dismiss();
                        }else if(duration == 0){
                            Log.d("last_call_make_contact", "duration was ZERO");
                            Adapter_For_Make_Contact.last_dialled_number = "";
                            Log.d("last_call_make_contact", "last dialled value set" + Adapter_For_Make_Contact.last_dialled_number);
                            Calendar calendar = Calendar.getInstance();
                            calendar.set(Calendar.HOUR,0);
                            calendar.set(Calendar.MINUTE,0);
                            calendar.set(Calendar.SECOND,0);
                            calendar.set(Calendar.MILLISECOND,0);
                            String frame = Adapter_For_Make_Contact.customer_class_gloabl.getFrame_no();
                            String contact_date = String.valueOf(calendar.getTimeInMillis());
                            String contact_number = "";
                            String current_time = String.valueOf(Calendar.getInstance().getTimeInMillis()) ;
                            contact_number =  String.valueOf(Adapter_For_Make_Contact.customer_class_gloabl.getMobile_1());
                            //String user, String frame_no, String message, String contact_date, String contact_type, String contact_number
                            LOG_CLASS  log_class = new LOG_CLASS(BASIC_DATA_HOLDER.getUser(),frame,"",contact_date,"PHONE_CALL_NOT_CONNECT",contact_number );
                            databaseReference.child(LOGS_DATABASE_REFERENCE).child(contact_number).child(String.valueOf(current_time)).setValue(log_class);
                            BASIC_DATA_HOLDER.loading_dialog.dismiss();

                        }else{
                            Log.d("last_call_make_contact", "Duration below acceptable");
                            Adapter_For_Make_Contact.last_dialled_number = "";
                            Log.d("last_call_make_contact", "last dialled value set" + Adapter_For_Make_Contact.last_dialled_number);

                            BASIC_DATA_HOLDER.loading_dialog.dismiss();
                        }
                    }else{
                        Log.d("last_call_make_contact", "NUmber Not Matched");
                        BASIC_DATA_HOLDER.loading_dialog.dismiss();
                    }
                }else{
                    if(Long.parseLong(Call_Details.getCall_number()) == customer_class.getMobile_2()){
                        Log.d("last_call_make_contact", "mobile 2 called" + customer_class.getMobile_2());
                        Log.d("last_call_make_contact", "number received from call details" + Call_Details.getCall_number());
                        long duration = Long.parseLong(Call_Details.getDuration());
                        if(duration > MINIMUM_CALL_DURATION){
                            Log.d("last_call_make_contact", "duration has been matched");
                            Adapter_For_Make_Contact.last_dialled_number = String.valueOf(Adapter_For_Make_Contact.customer_class_gloabl.getMobile_2()) ;
                            Log.d("last_call_make_contact", "last dialled value set" + Adapter_For_Make_Contact.last_dialled_number);
                            update_counter();
                            Call_Details.setDuration("0");
                            Call_Details.setCall_number("0");
                            Call_Details.setCall_time_stamp("0");
                            Calendar calendar = Calendar.getInstance();
                            calendar.set(Calendar.HOUR,0);
                            calendar.set(Calendar.MINUTE,0);
                            calendar.set(Calendar.SECOND,0);
                            calendar.set(Calendar.MILLISECOND,0);
                            String frame = Adapter_For_Make_Contact.customer_class_gloabl.getFrame_no();
                            String contact_date = String.valueOf(calendar.getTimeInMillis());
                            String contact_number = "";
                            String current_time = String.valueOf(Calendar.getInstance().getTimeInMillis()) ;
                            contact_number =  String.valueOf(Adapter_For_Make_Contact.customer_class_gloabl.getMobile_2());
                            //String user, String frame_no, String message, String contact_date, String contact_type, String contact_number
                            LOG_CLASS  log_class = new LOG_CLASS(BASIC_DATA_HOLDER.getUser(),frame,"",contact_date,"PHONE_CALL_CONNECT",contact_number );
                            databaseReference.child(LOGS_DATABASE_REFERENCE).child(contact_number).child(String.valueOf(current_time)).setValue(log_class);
                            BASIC_DATA_HOLDER.loading_dialog.dismiss();
                        }else if(duration == 0){
                            Log.d("last_call_make_contact", "duration was ZERO");
                            Adapter_For_Make_Contact.last_dialled_number = "";
                            Log.d("last_call_make_contact", "last dialled value set" + Adapter_For_Make_Contact.last_dialled_number);
                            Calendar calendar = Calendar.getInstance();
                            calendar.set(Calendar.HOUR,0);
                            calendar.set(Calendar.MINUTE,0);
                            calendar.set(Calendar.SECOND,0);
                            calendar.set(Calendar.MILLISECOND,0);
                            String frame = Adapter_For_Make_Contact.customer_class_gloabl.getFrame_no();
                            String contact_date = String.valueOf(calendar.getTimeInMillis());
                            String contact_number = "";
                            String current_time = String.valueOf(Calendar.getInstance().getTimeInMillis()) ;
                            contact_number =  String.valueOf(Adapter_For_Make_Contact.customer_class_gloabl.getMobile_2());
                            //String user, String frame_no, String message, String contact_date, String contact_type, String contact_number
                            LOG_CLASS  log_class = new LOG_CLASS(BASIC_DATA_HOLDER.getUser(),frame,"",contact_date,"PHONE_CALL_NOT_CONNECT",contact_number );
                            databaseReference.child(LOGS_DATABASE_REFERENCE).child(contact_number).child(String.valueOf(current_time)).setValue(log_class);
                            BASIC_DATA_HOLDER.loading_dialog.dismiss();

                        }else{
                            Log.d("last_call_make_contact", "Duration below acceptable");
                            Adapter_For_Make_Contact.last_dialled_number = "";
                            Log.d("last_call_make_contact", "last dialled value set" + Adapter_For_Make_Contact.last_dialled_number);
                            BASIC_DATA_HOLDER.loading_dialog.dismiss();
                        }
                    }else{
                        Log.d("last_call_make_contact", "Number Not Matched");
                        BASIC_DATA_HOLDER.loading_dialog.dismiss();
                    }
                }


            }
        }else{
            if(sending_message && adapter_for_make_contact!=null){
                int postion = Adapter_For_Make_Contact.active_view_position;
                DataSnapshot snapshot;
                if(Adapter_For_Make_Contact.filter_value.equals("")){
                    snapshot = Adapter_For_Make_Contact.hashMap.get(Adapter_For_Make_Contact.key_holder.get(postion));
                    assert snapshot != null;
                    Log.d("active_position", "onPostResume: " + postion);
                    String key =  Adapter_For_Make_Contact.hashMap.get(Adapter_For_Make_Contact.key_holder.get(postion)).getKey();
                    remove_after_message_sent(key);
                }else{
                    snapshot = Adapter_For_Make_Contact.searchviewMap.get(Adapter_For_Make_Contact.search_view_key_holder_array.get(postion));
                    assert snapshot != null;
                    Log.d("active_position", "onPostResume: " + postion);
                    String key =  Adapter_For_Make_Contact.searchviewMap.get(Adapter_For_Make_Contact.search_view_key_holder_array.get(postion)).getKey();
                    remove_after_message_sent(key);
                }

            }

        }
    }
}