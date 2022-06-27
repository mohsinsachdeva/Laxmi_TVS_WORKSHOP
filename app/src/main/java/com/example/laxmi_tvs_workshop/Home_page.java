package com.example.laxmi_tvs_workshop;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Toast;

public class Home_page extends AppCompatActivity {
    public static final int REQUEST_CODE = 1234;
    Button free_Calling;
    Button paid_Calling;
    Button testing;
    Boolean permission_result;
    Button reminder;
    Spinner user_Spinner;
    ArrayAdapter<CharSequence> user_names;

    androidx.appcompat.widget.SwitchCompat message_mode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        BASIC_DATA_HOLDER basic_data_holder = new BASIC_DATA_HOLDER();
        BASIC_DATA_HOLDER.setActivity(this);
        getPermissions();


        free_Calling = (Button) findViewById(R.id.free_Calling);
        paid_Calling = (Button) findViewById(R.id.paid_Calling);
        reminder = (Button)findViewById(R.id.reminder);
        testing = (Button)findViewById(R.id.testing);
        message_mode = ( androidx.appcompat.widget.SwitchCompat)findViewById(R.id.message_mode);
        free_Calling.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(user_selected()){
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

            }
        });
        paid_Calling.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(user_selected()){
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

            }
        });

        reminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(user_selected()){
                    SharedPreferences sharedPreferences = getSharedPreferences("MySharedPref",MODE_PRIVATE);
                    SharedPreferences.Editor myEdit = sharedPreferences.edit();
// Storing the key and its value as the data fetched from edittext
                    myEdit.putBoolean("sendingmessage", false);
                    myEdit.commit();
                    BASIC_DATA_HOLDER.setCalling_type("reminder");
                    Intent intent = new Intent(Home_page.this, Make_Contact.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
                    finish();
                }


            }
        });

        testing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(user_selected()){
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
            }
        });
        message_mode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(message_mode.isChecked()){
                    SharedPreferences sharedPreferences = getSharedPreferences("MySharedPrefhome",MODE_PRIVATE);
                    SharedPreferences.Editor myEdit = sharedPreferences.edit();
// Storing the key and its value as the data fetched from edittext
                    myEdit.putBoolean("messagemode", true);
                    myEdit.commit();
                    BASIC_DATA_HOLDER.setMessage_mode(true);
                }else{
                    SharedPreferences sharedPreferences = getSharedPreferences("MySharedPrefhome",MODE_PRIVATE);
                    SharedPreferences.Editor myEdit = sharedPreferences.edit();
// Storing the key and its value as the data fetched from edittext
                    myEdit.putBoolean("messagemode", false);
                    myEdit.commit();
                    BASIC_DATA_HOLDER.setMessage_mode(false);
                }
            }
        });
        user_Spinner = (Spinner) findViewById(R.id.user_spinner);
        user_names = ArrayAdapter.createFromResource(this,R.array.user_names, android.R.layout.simple_spinner_dropdown_item);
        user_Spinner.setAdapter(user_names);
        user_Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position>0){
                    BASIC_DATA_HOLDER.setUser(user_Spinner.getSelectedItem().toString());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        SharedPreferences sh = getSharedPreferences("MySharedPrefhome", MODE_PRIVATE);
        message_mode.setChecked(sh.getBoolean("messagemode",false));
    }

    public boolean getPermissions() {

        if (Build.VERSION.SDK_INT >= 29) {


            // permissions video https://www.youtube.com/watch?v=AyhkpvQwFsI


            if ((ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)) +
                    (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
                    ) + (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_MEDIA_LOCATION))
                    + (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CALL_LOG)) +

                    (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS))
                    != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.

                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CALL_PHONE) ||
                        ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.SEND_SMS)
                        ||
                        ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_MEDIA_LOCATION)||
                        ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_CALL_LOG) ||
                        ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_CONTACTS)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Please Grant Permissions");
                    builder.setMessage("These Permissions are necessary for the App to function properly ");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(Home_page.this, new String[]{Manifest.permission.CALL_PHONE
                                    , Manifest.permission.SEND_SMS, Manifest.permission.ACCESS_MEDIA_LOCATION}, REQUEST_CODE);
                        }
                    });
                    builder.setNegativeButton("Cancel", null);
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                } else {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE
                            , Manifest.permission.SEND_SMS, Manifest.permission.ACCESS_MEDIA_LOCATION, Manifest.permission.READ_CALL_LOG, Manifest.permission.READ_CONTACTS}, REQUEST_CODE);

                }
                ActivityCompat.OnRequestPermissionsResultCallback mr = new ActivityCompat.OnRequestPermissionsResultCallback() {
                    @Override
                    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
                        if (requestCode > 0 && grantResults[0] == 123 && grantResults[1] == 123 && grantResults[2] == 123) {

                            permission_result = true;
                        } else {
                            permission_result = false;
                        }
                    }
                };
            } else {
                permission_result = true;
            }

            return permission_result;
        }else{
            if ((ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)) +
                    (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
                    ) + (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CALL_LOG)
            ) + (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
            )
                    != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.

                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CALL_PHONE) ||
                        ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.SEND_SMS) ||
                        ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_CALL_LOG)
                        || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_CONTACTS)){
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Please Grant Permissions");
                    builder.setMessage("These Permissions are necessary for the App to function properly ");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(Home_page.this, new String[]{Manifest.permission.CALL_PHONE
                                    , Manifest.permission.SEND_SMS}, REQUEST_CODE);
                        }
                    });
                    builder.setNegativeButton("Cancel", null);
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                } else {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE
                            , Manifest.permission.SEND_SMS, Manifest.permission.READ_CONTACTS}, REQUEST_CODE);

                }
                ActivityCompat.OnRequestPermissionsResultCallback mr = new ActivityCompat.OnRequestPermissionsResultCallback() {
                    @Override
                    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
                        if (requestCode > 0 && grantResults[0] == 123 && grantResults[1] == 123) {

                            permission_result = true;
                        } else {
                            permission_result = false;
                        }
                    }
                };
            } else {
                permission_result = true;
            }

            return permission_result;
        }

    }

    public boolean user_selected (){
        if(user_Spinner.getSelectedItemPosition()>0){
            return true;
        }else{
            Toast.makeText(this, "Please Select User", Toast.LENGTH_SHORT).show();
            return false;
        }
    }
}