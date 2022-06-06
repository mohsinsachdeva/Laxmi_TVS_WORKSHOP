package com.example.laxmi_tvs_workshop;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.rpc.context.AttributeContext;

public class Make_Contact extends AppCompatActivity {
ImageButton go_back_home;
androidx.recyclerview.widget.RecyclerView customer_list;
DatabaseReference databaseReference;
RecyclerView.LayoutManager mLayoutManager;
Adapter_For_Make_Contact adapter_for_make_contact;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_contact);

        databaseReference = FirebaseDatabase.getInstance("https://laxmi-tvs-workshop-default-rtdb.asia-southeast1.firebasedatabase.app").getReference();
        go_back_home = (ImageButton) findViewById(R.id.go_back_home);
        customer_list = (androidx.recyclerview.widget.RecyclerView) findViewById(R.id.customer_list);
        mLayoutManager = new LinearLayoutManager(this,RecyclerView.VERTICAL,false);




        go_back_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent go_to_settings_intent= new Intent(Make_Contact.this, MainActivity.class);
                go_to_settings_intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(go_to_settings_intent);
                finish();
            }
        });

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this,DividerItemDecoration.HORIZONTAL);
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(this,R.drawable.divider));
        customer_list.addItemDecoration(dividerItemDecoration);




    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        adapter_for_make_contact = new Adapter_For_Make_Contact(this, databaseReference);
        customer_list.setLayoutManager(mLayoutManager);
        customer_list.setAdapter(adapter_for_make_contact);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(adapter_for_make_contact!=null){
            adapter_for_make_contact.cleanup();
        }
    }
}