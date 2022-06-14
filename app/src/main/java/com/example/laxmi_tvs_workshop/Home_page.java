package com.example.laxmi_tvs_workshop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;

public class Home_page extends AppCompatActivity {
    
    Button free_Calling;
    Button paid_Calling;
    Button testing;
    androidx.appcompat.widget.SwitchCompat message_mode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        BASIC_DATA_HOLDER basic_data_holder = new BASIC_DATA_HOLDER();
        BASIC_DATA_HOLDER.setActivity(this);

        free_Calling = (Button) findViewById(R.id.free_Calling);
        paid_Calling = (Button) findViewById(R.id.paid_Calling);
        testing = (Button)findViewById(R.id.testing);
        message_mode = ( androidx.appcompat.widget.SwitchCompat)findViewById(R.id.message_mode);
        free_Calling.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref",MODE_PRIVATE);
                SharedPreferences.Editor myEdit = sharedPreferences.edit();
// Storing the key and its value as the data fetched from edittext
                myEdit.putBoolean("sendingmessage", false);
                myEdit.commit();
                BASIC_DATA_HOLDER.setCalling_type("calling_free");
                Intent intent = new Intent(Home_page.this, Make_Contact.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
            }
        });
        paid_Calling.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref",MODE_PRIVATE);
                SharedPreferences.Editor myEdit = sharedPreferences.edit();
// Storing the key and its value as the data fetched from edittext
                myEdit.putBoolean("sendingmessage", false);
                myEdit.commit();
                BASIC_DATA_HOLDER.setCalling_type("calling_paid");
                Intent intent = new Intent(Home_page.this, Make_Contact.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
            }
        });

        testing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref",MODE_PRIVATE);
                SharedPreferences.Editor myEdit = sharedPreferences.edit();
// Storing the key and its value as the data fetched from edittext
                myEdit.putBoolean("sendingmessage", false);
                myEdit.commit();
                BASIC_DATA_HOLDER.setCalling_type("testing");
                Intent intent = new Intent(Home_page.this, Make_Contact.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
            }
        });
        message_mode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(message_mode.isChecked()){
                    BASIC_DATA_HOLDER.setMessage_mode(true);
                }else{
                    BASIC_DATA_HOLDER.setMessage_mode(false);
                }
            }
        });

    }

}