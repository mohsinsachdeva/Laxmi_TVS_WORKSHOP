package com.example.laxmi_tvs_workshop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Home_page extends AppCompatActivity {
    
    Button free_Calling;
    Button paid_Calling;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        BASIC_DATA_HOLDER basic_data_holder = new BASIC_DATA_HOLDER();

        free_Calling = (Button) findViewById(R.id.free_Calling);
        paid_Calling = (Button) findViewById(R.id.paid_Calling);
        free_Calling.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                BASIC_DATA_HOLDER.setCalling_type("calling_paid");
                Intent intent = new Intent(Home_page.this, Make_Contact.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
            }
        });

    }
}