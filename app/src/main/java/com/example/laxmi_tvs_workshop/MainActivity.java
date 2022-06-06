package com.example.laxmi_tvs_workshop;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

import pl.droidsonroids.gif.GifImageView;

public class MainActivity extends AppCompatActivity {

    public static FutureTask mFutureTask;
    public static ExecutorService sService;
    DatabaseReference databaseReference;
    public static Dialog loading_dialog;
    public static TextView loading;
    long number_of_enteries;
    Spinner language_spinner;
    ArrayAdapter<CharSequence> adapter_for_spinner;

    Spinner due_type_spinner;
    ArrayAdapter<CharSequence> adapter_for_reminder_type;


    ArrayList<DataSnapshot> datalist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        databaseReference = FirebaseDatabase.getInstance("https://laxmi-tvs-workshop-default-rtdb.asia-southeast1.firebasedatabase.app").getReference();
        datalist = new ArrayList<>();
        loading_dialog = new Dialog(this);
        loading_dialog.setContentView(R.layout.progress_loading);
        sService = Executors.newFixedThreadPool(500);

        language_spinner = (Spinner)findViewById(R.id.spinner);
        adapter_for_spinner = ArrayAdapter.createFromResource(this,R.array.message_type, android.R.layout.simple_spinner_dropdown_item);
        language_spinner.setAdapter(adapter_for_spinner);

        due_type_spinner = (Spinner)findViewById(R.id.spinner_reminder_type);
        adapter_for_reminder_type = ArrayAdapter.createFromResource(this, R.array.reminder_type, android.R.layout.simple_spinner_dropdown_item);
        due_type_spinner.setAdapter(adapter_for_reminder_type);



        Button sms_button = (Button)findViewById(R.id.send_sms_button);
        Button send_watsapp = (Button)findViewById(R.id. send_watsapp);

        Button create_contact = (Button)findViewById(R.id.create_contact);
        create_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent go_to_settings_intent= new Intent(MainActivity.this, Make_Contact.class);
                go_to_settings_intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(go_to_settings_intent);
                finish();
            }
        });

        sms_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(due_type_spinner.getSelectedItemPosition()> 0){
                    if(language_spinner.getSelectedItemPosition()>0 && language_spinner.getSelectedItemPosition()!=3){
                        int language_code;

                        if(language_spinner.getSelectedItemPosition()==1){
                            language_code = 1;
                        }else if (language_spinner.getSelectedItemPosition()==2){
                            language_code = 2;
                        }else{
                            language_code = 3;
                        }

                        int size = datalist.size();
                        Log.d("print_result", "onClick: "+ datalist.size());
                        for(int i = 0; i<size; i++){
                            DataSnapshot snapshot = datalist.get(i);
                            String name = snapshot.child("Customer").getValue(String.class).toUpperCase(Locale.ROOT);
                            String due_date = snapshot.child("Due Date").getValue(String.class);
                            String mobile = String.valueOf(snapshot.child("Mobile").getValue(Long.class));
                            String model = snapshot.child("Model").getValue(String.class);

                            Log.d("print_result", "onClick: " + name + " " + due_date + " " + mobile + " " + model);
                            Send_SMS send_sms = new Send_SMS(name,model,due_date,mobile,language_code);
                            mFutureTask = new FutureTask(send_sms);
                            sService.execute(mFutureTask);

                            if(i-datalist.size()==-1){
                                loading_dialog.dismiss();
                                databaseReference.child("Calling").removeValue();
                            }
                        }
                    }else{
                        Toast.makeText(MainActivity.this, "Please Select Language", Toast.LENGTH_SHORT).show();
                    }

                }else{
                    Toast.makeText(MainActivity.this, "PLease Selecte Reminder Type", Toast.LENGTH_SHORT).show();
                }



            }
        });
        sms_button.setEnabled(false);

        send_watsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(due_type_spinner.getSelectedItemPosition()>0){
                    if(language_spinner.getSelectedItemPosition()>0){

                        int language_code;
                        int due_type;

                        if(language_spinner.getSelectedItemPosition()==1){
                            language_code = 1;
                        }else if(language_spinner.getSelectedItemPosition()==2){
                            language_code = 2;
                        }else{
                            language_code = 3;
                        }


                        if(due_type_spinner.getSelectedItemPosition()==1){
                            due_type =1;
                        }else if(due_type_spinner.getSelectedItemPosition()==2){
                            due_type = 2;
                        }else{
                            due_type = 3;
                        }
                       /* int size = datalist.size();
                        Log.d("print_result", "onClick: "+ datalist.size());
                        for(int i = 0; i<size; i++){
                            DataSnapshot snapshot = datalist.get(i);
                            String name = snapshot.child("Customer").getValue(String.class);
                            String due_date = snapshot.child("Due Date").getValue(String.class);
                            String mobile = String.valueOf(snapshot.child("Mobile").getValue(Long.class));
                            String model = snapshot.child("Model").getValue(String.class);

                            Log.d("print_result", "onClick: " + name + " " + due_date + " " + mobile + " " + model);
                            Send_WATSAPP watsapp = new Send_WATSAPP(name,model,due_date,mobile,language_code,due_type);
                            mFutureTask = new FutureTask(watsapp);
                            sService.execute(mFutureTask);

                            if(i-datalist.size()==-1){
                                loading_dialog.dismiss();
                                databaseReference.child("Calling").removeValue();
                            }


                        }*/
                    /*    Send_WATSAPP watsapp = new Send_WATSAPP("mohsin","Jupiter","01/06/2022","9815520300",language_code,due_type);
                        mFutureTask = new FutureTask(watsapp);
                        sService.execute(mFutureTask);*/

                        try {
                            String break_line = "%0A";
                            String mobile = "919815520300";
                            String msg = "Dear "+"Mohsin"+","+break_line+"Your "+"Jupiter"+" is due for service. *The due date is "+"01/06/2022"+".*"+break_line+"Timely service of your Two-Wheeler *will give you good performance and great average*. Kindly bring your vehicle to the workshop soon." + break_line + "Workshop Timming:"+break_line+
                                    "Monday - Friday : 9am - 5:30pm"+break_line+"Sunday : 9am - 3pm"+break_line+"Laxmi TVS"+break_line+"Call : 8872084211, 8872084210";
                            String mainMessage="";

                            for ( String str : msg.split(break_line)){

                                mainMessage = mainMessage + str + "\n";

                                Log.d("string_test", "call: "+mainMessage);;
                            }

                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://api.whatsapp.com/send?phone=" + mobile + "&text=" + mainMessage)));
                        }catch (Exception e){
                            //whatsapp app not install
                        }

                    }else{
                        Toast.makeText(MainActivity.this, "Please Select Language", Toast.LENGTH_SHORT).show();
                    }

                }else{
                    Toast.makeText(MainActivity.this, "PLease Selecte Reminder Type", Toast.LENGTH_SHORT).show();
                }




            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        number_of_enteries = 0;
        datalist.clear();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        databaseReference.child("Calling").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
            if(snapshot.exists()){
                number_of_enteries =  snapshot.getChildrenCount();
                progress_bar();
            get_data();
            }else{
                Log.d("print_result", "onData was found ");
                loading_dialog.dismiss();
            }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("print_result", "onData was found ");
            }
        });

    }

    public void get_data(){
        databaseReference.child("Calling").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                datalist.add(snapshot);
                Log.d("print_result", "onChildAdded: "+ snapshot.getKey());
                if(datalist.size() == (int) number_of_enteries){
                    loading_dialog.dismiss();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    public static void progress_bar(){

        loading = (TextView)loading_dialog.findViewById(R.id.text_view_progressbar);
        loading.setText(R.string.loading);
        GifImageView progress_gif = (GifImageView)loading_dialog.findViewById(R.id.progress_gif);
        progress_gif.setImageResource(R.drawable.loading);
        loading_dialog.setCancelable(false);
        //   mDialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        loading_dialog.show();

    }
}