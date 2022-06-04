package com.example.laxmi_tvs_workshop;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
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
    Spinner spinner_for_message_type;
    ArrayAdapter<CharSequence> adapter_for_spinner;


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

        spinner_for_message_type = (Spinner)findViewById(R.id.spinner);
        adapter_for_spinner = ArrayAdapter.createFromResource(this,R.array.message_type, android.R.layout.simple_spinner_dropdown_item);

        spinner_for_message_type.setAdapter(adapter_for_spinner);



        Button sms_button = (Button)findViewById(R.id.send_sms_button);
        sms_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int language_code;

                if(spinner_for_message_type.getSelectedItem().toString().equals("English")){
                    language_code = 1;
                }else{
                        language_code = 2;
                }
                databaseReference.child("mojo").setValue(1).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("failed", "onFailure: "+ e.toString());
                    }
                });
              int size = datalist.size();
                Log.d("print_result", "onClick: "+ datalist.size());
                for(int i = 0; i<size; i++){
                    DataSnapshot snapshot = datalist.get(i);
                    String name = snapshot.child("Customer").getValue(String.class);
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