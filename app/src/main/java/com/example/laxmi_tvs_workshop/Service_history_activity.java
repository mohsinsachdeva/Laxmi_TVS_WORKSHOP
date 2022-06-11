package com.example.laxmi_tvs_workshop;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class Service_history_activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.service_history);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}