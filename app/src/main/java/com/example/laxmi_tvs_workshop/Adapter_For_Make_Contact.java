package com.example.laxmi_tvs_workshop;

import static android.provider.CallLog.Calls.DEFAULT_SORT_ORDER;
import static android.provider.CallLog.Calls.LIMIT_PARAM_KEY;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firestore.v1.StructuredQuery;

import org.apache.poi.ss.formula.functions.Columns;
import org.apache.poi.ss.formula.functions.Offset;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;


public class Adapter_For_Make_Contact extends RecyclerView.Adapter<Adapter_For_Make_Contact.Holder> {
   public static final String KEY_NAME_MESSAGE = "message";
   public static final String KEY_NAME_REMINDER = "reminder";
    Activity activity;
    DatabaseReference databaseReference;
    public static HashMap<String, DataSnapshot> hashMap;
    public static ArrayList<String> key_holder;
    public static HashMap<String, DataSnapshot> searchviewMap;
    public static ArrayList<String> search_view_key_holder_array;
    Calendar calendar;
    String last_message_date = "";
    String frame_no;
    public static Customer_Class customer_class_gloabl;
   public static boolean global_mobile1_selection;
    public static boolean global_mobile2_selection;
    long todays_date;
    boolean permission_result;
    final int REQUEST_CODE = 1234;
    Intent callIntent;
    Dialog service_history_dialog;
    String contact_number_in_use;
    String global_key;
    public static String last_dialled_number = "";
    public static int active_view_position;
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
    public static String filter_date = "";
    public static String filter_value ="";


    public Adapter_For_Make_Contact(Activity activity, DatabaseReference databaseReference) {
        this.activity = activity;
        this.databaseReference = databaseReference;
        hashMap = new HashMap<>();
        calendar = Calendar.getInstance();
        key_holder = new ArrayList<>();
        searchviewMap = new HashMap<>();
        search_view_key_holder_array = new ArrayList<>();
        counter_check();
        BASIC_DATA_HOLDER.progress_bar();




        Log.d("testing", "Adapter_For_Make_Contact: CALLED");
        databaseReference.child(BASIC_DATA_HOLDER.getCalling_type() +"_"+ BASIC_DATA_HOLDER.getUser()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    if(snapshot.getChildrenCount()>0){
                        databaseReference.child(BASIC_DATA_HOLDER.getCalling_type()+"_"+ BASIC_DATA_HOLDER.getUser()).addChildEventListener(childEventListener);
                    }else{
                        BASIC_DATA_HOLDER.loading_dialog.dismiss();
                        Toast.makeText(activity, "NO DATA IS AVAILABLE", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    BASIC_DATA_HOLDER.loading_dialog.dismiss();
                    Toast.makeText(activity, "NO DATA IS AVAILABLE", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                BASIC_DATA_HOLDER.loading_dialog.dismiss();
                Toast.makeText(activity, "SOMETHING WENT WRONG", Toast.LENGTH_SHORT).show();
            }
        });


    }


    @Override
    public Adapter_For_Make_Contact.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(BASIC_DATA_HOLDER.getCalling_type().equals(KEY_NAME_REMINDER)){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_make_contact_remindert, parent, false);
            Holder holder = new Holder(view);
            return holder;
        }else{
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_make_contact, parent, false);
            Holder holder = new Holder(view);
            return holder;
        }

    }

    @Override
    public void onBindViewHolder(@NonNull Adapter_For_Make_Contact.Holder holder, int position) {
        DataSnapshot snapshot;
        if(filter_value.equals("")){
            snapshot = hashMap.get(key_holder.get(position));
            Log.d("view_error", "onBindViewHolder: " + position);
            assert snapshot != null;
        }else{
            snapshot = searchviewMap.get(search_view_key_holder_array.get(position));
            Log.d("view_error", "onBindViewHolder: " + position);
            assert snapshot != null;
        }
        String key_value = snapshot.getKey();


        if(BASIC_DATA_HOLDER.getCalling_type().equals(KEY_NAME_REMINDER)){
            holder.mobile_selected.setChecked(Make_Contact.mobile_one_value);
            holder.phone_selected.setChecked(Make_Contact.mobile_two_value);

            Customer_Class customer_class = snapshot.getValue(Customer_Class.class);
            assert customer_class != null;
            holder.customer_name_value.setText(customer_class.getCustomer_name());
            holder.customer_vehicle.setText(customer_class.getModel());
            holder.service_due_value.setText(customer_class.getDue_date());
            holder.frame_number_value.setText(customer_class.getFrame_no());
            holder.address_line_one.setText(customer_class.address_line_2);
            holder.address_line_two.setText(customer_class.getAddress_line_3());
            holder.registration_value.setText(customer_class.getRegistration());
            holder.service_type_value.setText(customer_class.getService_type());
            long recycle_date = Long.parseLong(customer_class.getRecycle_date());

            holder.recycle_date_value.setText(dateFormat.format(recycle_date));
            if (customer_class.getMobile_1() != null && !customer_class.getMobile_1().equals("")) {
                holder.mobile_one_value.setText(String.valueOf(customer_class.mobile_1));
            }

            if (customer_class.getMobile_1() == 0) {
                holder.mobile_one_value.setText("");
            }
            if (customer_class.getMobile_2() != null) {
                holder.mobile_two_value.setText(String.valueOf(customer_class.mobile_2));
            }
            if (customer_class.getMobile_2() == 0) {
                holder.mobile_two_value.setText("");
            }

            holder.english_check_box.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    BASIC_DATA_HOLDER.progress_bar();
                    active_view_position = position;
                    global_mobile1_selection = holder.mobile_selected.isChecked();
                    global_mobile2_selection = holder.phone_selected.isChecked();
                    Make_Contact.sending_message = true;
                    calendar.set(Calendar.HOUR, 0);
                    calendar.set(Calendar.MILLISECOND, 0);
                    calendar.set(Calendar.SECOND, 0);
                    calendar.set(Calendar.MINUTE, 0);
                    todays_date = calendar.getTimeInMillis();

                    frame_no = customer_class.getFrame_no();
                    customer_class_gloabl = customer_class;
                    if(global_mobile1_selection){
                        contact_number_in_use = String.valueOf(customer_class.getMobile_1());
                    }else{
                        contact_number_in_use = String.valueOf(customer_class.getMobile_2());
                    }

                    databaseReference.child("LOGS").child(contact_number_in_use).limitToLast(1).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                global_key = key_holder.get(position);
                                databaseReference.child("LOGS").child(contact_number_in_use).limitToLast(1).addChildEventListener(date_check);
                            } else {
                                if (holder.mobile_selected.isChecked() && customer_class.getMobile_1() != null) {
                                    try {
                                        String break_line = "%0A";
                                        String mobile = String.valueOf(customer_class.getMobile_1());
                                        String msg;
                                        if (service_status(customer_class.getDue_date())) {
                                            msg = "Dear " + customer_class.getCustomer_name() + " Ji,"+ break_line + "Your " + customer_class.getModel() + " is due for service. *The due date is " + customer_class.getDue_date() + ".*" + break_line + "Timely service of your Two-Wheeler *will give you good performance and great average*. Kindly bring your vehicle to the workshop soon." + break_line + "*Workshop Timming:*" + break_line +
                                                    "Monday - Friday : 9am - 6:00pm" + break_line + "Sunday : 9am - 3pm" + break_line + "Laxmi TVS" + break_line + "Call : 8872084211, 8872084210" + break_line + "" + break_line + "▬▬▬▬▬▬▬▬▬▬▬▬▬▬" + break_line + "" + break_line + customer_class.getCustomer_name() + " जी," + break_line + "आपके दुपहिया वाहन " + customer_class.getModel() + " की सर्विस का टाइम आ गया है| *हमारे रिकॉर्ड के अनुसार सर्विस " + customer_class.getDue_date() + " किया जाना चाहिए|*" + break_line + "टाइम से सर्विस करवाने का फ़ायदा यह है की आपको *बेहतरीन एवरेज और परफॉरमेंस* मिलती है| कृपया वर्कशॉप में जल्दी आए|" + break_line + "*वर्कशॉप का समय:*" + break_line + "सोमवार - शनिवार: सुबह 9 बजे से - शाम 6 बजे तक।" + break_line + "रविवार: सुबह 9 बजे से - दोपहर 3 बजे तक।" + break_line + "लक्ष्मी टीवीएस" + break_line + "कृपया कॉल करें : 8872084210, 8872084211";

                                        } else {
                                            msg =  "Dear " + customer_class.getCustomer_name() + " Ji,"+ break_line + "Your " + customer_class.getModel() + " has missed its service. *The due date was " + customer_class.getDue_date() + ".*" + break_line + "Without service *vehilce performance and average may not be best.* Kindly bring your vehicle to the workshop soon." + break_line + "*Workshop Timming:*" + break_line +
                                                    "Monday - Friday : 9am - 6:00pm" + break_line + "Sunday : 9am - 3pm" + break_line + "Laxmi TVS" + break_line + "Call : 8872084211, 8872084210" + break_line + "" + break_line + "▬▬▬▬▬▬▬▬▬▬▬▬▬▬" + break_line + "" + break_line + customer_class.getCustomer_name() + " जी," + break_line + "आपके दुपहिया वाहन " + customer_class.getModel() + " की सर्विस का टाइम चला गया गया है| *हमारे रिकॉर्ड के अनुसार सर्विस " + customer_class.getDue_date() + " को किया जाना चाहिए था|*" + break_line + "टाइम से सर्विस करवाने का फ़ायदा यह है की *आपको बेहतरीन एवरेज और परफॉरमेंस* मिलती है| कृपया वर्कशॉप में जल्दी आए|" + break_line + "*वर्कशॉप का समय:*" + break_line + "सोमवार - शनिवार: सुबह 9 बजे से - शाम 6 बजे तक।" + break_line + "रविवार: सुबह 9 बजे से - दोपहर 3 बजे तक।" + break_line + "लक्ष्मी टीवीएस" + break_line + "कृपया कॉल करें : 8872084210, 8872084211";
                                        }


                                        String mainMessage = "";

                                        for (String str : msg.split(break_line)) {

                                            mainMessage = mainMessage + str + "\n";

                                            Log.d("string_test", "call: " + mainMessage);
                                            ;
                                        }
                                        String user = "";
                                        String frame_no = customer_class.getFrame_no();
                                        String message = mainMessage;

                                        String contact_date = String.valueOf(todays_date);
                                        String contact_type = "WATSAPP";
                                        String contacted_number;
                                        if (holder.mobile_selected.isChecked()) {
                                            contacted_number = String.valueOf(customer_class.getMobile_1());

                                        } else {
                                            contacted_number = String.valueOf(customer_class.getMobile_2());
                                        }
                                        long push_value = Calendar.getInstance().getTimeInMillis();
                                        LOG_CLASS log_class = new LOG_CLASS(BASIC_DATA_HOLDER.getUser(), frame_no, message, contact_date, contact_type, contacted_number);
                                        databaseReference.child("LOGS").child(contact_number_in_use).child(String.valueOf(push_value)).setValue(log_class);

                                        activity.startActivityForResult(new Intent(Intent.ACTION_VIEW, Uri.parse("https://api.whatsapp.com/send?phone=91" + mobile + "&text=" + mainMessage)), Make_Contact.REQUEST_CODE);


                                        String key = snapshot.getKey();


                                        update_counter();
                                        BASIC_DATA_HOLDER.loading_dialog.dismiss();
                                    } catch (Exception e) {
                                        BASIC_DATA_HOLDER.loading_dialog.dismiss();
                                    }
                                } else if (holder.phone_selected.isChecked() && customer_class.getMobile_2() != null) {
                                    try {
                                        String break_line = "%0A";
                                        String mobile = String.valueOf(customer_class.getMobile_2());
                                        String msg;
                                        if (service_status(customer_class.getDue_date())) {
                                            msg = "Dear " + customer_class.getCustomer_name() + " Ji," + break_line + "Your " + customer_class.getModel() + " is due for service. *The due date is " + customer_class.getDue_date() + ".*" + break_line + "Timely service of your Two-Wheeler *will give you good performance and great average*. Kindly bring your vehicle to the workshop soon." + break_line + "*Workshop Timming:*" + break_line +
                                                    "Monday - Friday : 9am - 6:00pm" + break_line + "Sunday : 9am - 3pm" + break_line + "Laxmi TVS" + break_line + "Call : 8872084211, 8872084210" + break_line + "" + break_line + "▬▬▬▬▬▬▬▬▬▬▬▬▬▬" + break_line + "" + break_line + customer_class.getCustomer_name() + " जी," + break_line + "आपके दुपहिया वाहन " + customer_class.getModel() + " की सर्विस का टाइम आ गया है| *हमारे रिकॉर्ड के अनुसार सर्विस " + customer_class.getDue_date() + " किया जाना चाहिए|*" + break_line + "टाइम से सर्विस करवाने का फ़ायदा यह है की आपको *बेहतरीन एवरेज और परफॉरमेंस* मिलती है| कृपया वर्कशॉप में जल्दी आए|" + break_line + "*वर्कशॉप का समय:*" + break_line + "सोमवार - शनिवार: सुबह 9 बजे से - शाम 6 बजे तक।" + break_line + "रविवार: सुबह 9 बजे से - दोपहर 3 बजे तक।" + break_line + "लक्ष्मी टीवीएस" + break_line + "कृपया कॉल करें : 8872084210, 8872084211";

                                        } else {
                                            msg = "Dear " + customer_class.getCustomer_name() + " Ji," + break_line + "Your " + customer_class.getModel() + " has missed its service. *The due date was " + customer_class.getDue_date() + ".*" + break_line + "Without service *vehilce performance and average may not be best.* Kindly bring your vehicle to the workshop soon." + break_line + "*Workshop Timming:*" + break_line +
                                                    "Monday - Friday : 9am - 6:00pm" + break_line + "Sunday : 9am - 3pm" + break_line + "Laxmi TVS" + break_line + "Call : 8872084211, 8872084210" + break_line + "" + break_line + "▬▬▬▬▬▬▬▬▬▬▬▬▬▬" + break_line + "" + break_line + customer_class.getCustomer_name() + " जी," + break_line + "आपके दुपहिया वाहन " + customer_class.getModel() + " की सर्विस का टाइम चला गया गया है| *हमारे रिकॉर्ड के अनुसार सर्विस " + customer_class.getDue_date() + " को किया जाना चाहिए था|*" + break_line + "टाइम से सर्विस करवाने का फ़ायदा यह है की *आपको बेहतरीन एवरेज और परफॉरमेंस* मिलती है| कृपया वर्कशॉप में जल्दी आए|" + break_line + "*वर्कशॉप का समय:*" + break_line + "सोमवार - शनिवार: सुबह 9 बजे से - शाम 6 बजे तक।" + break_line + "रविवार: सुबह 9 बजे से - दोपहर 3 बजे तक।" + break_line + "लक्ष्मी टीवीएस" + break_line + "कृपया कॉल करें : 8872084210, 8872084211";
                                        }


                                        String mainMessage = "";

                                        for (String str : msg.split(break_line)) {

                                            mainMessage = mainMessage + str + "\n";

                                            Log.d("string_test", "call: " + mainMessage);
                                            ;
                                        }
                                        String user = "";
                                        String frame_no = customer_class.getFrame_no();
                                        String message = mainMessage;

                                        String contact_date = String.valueOf(todays_date);
                                        String contact_type = "WATSAPP";
                                        String contacted_number;
                                        if (holder.mobile_selected.isChecked()) {
                                            contacted_number = String.valueOf(customer_class.getMobile_1());

                                        } else {
                                            contacted_number = String.valueOf(customer_class.getMobile_2());
                                        }

                                        long push_value = Calendar.getInstance().getTimeInMillis();
                                        LOG_CLASS log_class = new LOG_CLASS(BASIC_DATA_HOLDER.getUser(), frame_no, message, contact_date, contact_type, contacted_number);
                                        databaseReference.child("LOGS").child(contact_number_in_use).child(String.valueOf(push_value)).setValue(log_class);

                                        activity.startActivityForResult(new Intent(Intent.ACTION_VIEW, Uri.parse("https://api.whatsapp.com/send?phone=91" + mobile + "&text=" + mainMessage)), Make_Contact.REQUEST_CODE);
                                        String key = snapshot.getKey();


                                        update_counter();
                                        BASIC_DATA_HOLDER.loading_dialog.dismiss();
                                    } catch (Exception e) {
                                        BASIC_DATA_HOLDER.loading_dialog.dismiss();
                                        //whatsapp app not install
                                    }
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            BASIC_DATA_HOLDER.loading_dialog.dismiss();
                        }
                    });


                }
            });
            holder.mobile_selected.setChecked(Make_Contact.mobile_one_value);
            holder.phone_selected.setChecked(Make_Contact.mobile_two_value);
            global_mobile1_selection = holder.mobile_selected.isChecked();
            global_mobile2_selection = holder.phone_selected.isChecked();

            holder.mobile_selected.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (holder.mobile_selected.isChecked()) {
                        holder.phone_selected.setChecked(false);
                        global_mobile1_selection = true;
                        Make_Contact.mobile_one_value = true;

                    } else {
                        holder.phone_selected.setChecked(true);
                        global_mobile1_selection = false;
                        Make_Contact.mobile_one_value = false;
                    }
                }
            });


            holder.phone_selected.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (holder.phone_selected.isChecked()) {
                        holder.mobile_selected.setChecked(false);
                        global_mobile2_selection = true;
                        Make_Contact.mobile_one_value = true;
                        Make_Contact.mobile_two_value = false;
                    } else {
                        holder.mobile_selected.setChecked(true);
                        global_mobile2_selection = false;
                        Make_Contact.mobile_one_value = false;
                        Make_Contact.mobile_two_value = true;
                    }
                }
            });


            holder.call_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    customer_class_gloabl = customer_class;
                    active_view_position = position;
                    if(getPermissions()){
                        // Create the intent.
                        Log.e("calling", "permission granted");
                        callIntent = new Intent(Intent.ACTION_CALL);
                        // Set the data for the intent as the phone number.

                        if (holder.mobile_selected.isChecked()) {
                            callIntent.setData(Uri.parse("tel:" + holder.mobile_one_value.getText().toString()));
                            // If package resolves to an app, send intent.
                            if (callIntent.resolveActivity(activity.getPackageManager()) != null) {
                                if (getPermissions()) {
                                    Make_Contact.sending_message = true;
                                    activity.startActivityForResult(callIntent,1234);
                                }
                            } else {

                                Log.e("calling", "Can't resolve app for ACTION_CALL Intent.");
                            }
                        }else{
                            callIntent.setData(Uri.parse("tel:" + holder.mobile_two_value.getText().toString()));
                            // If package resolves to an app, send intent.
                            if (callIntent.resolveActivity(activity.getPackageManager()) != null) {
                                if (getPermissions()) {
                                    Make_Contact.sending_message = true;
                                    activity.startActivity(callIntent);
                                }
                            } else {

                                Log.e("calling", "Can't resolve app for ACTION_CALL Intent.");
                            }
                        }
                    }else{
                        Log.e("calling", "permission denied");
                    }
                }
            });
            holder.service_history_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    service_history_dialog = new Dialog(activity);
                    service_history_dialog.setCancelable(true);
                    service_history_dialog.setContentView(R.layout.service_history);
                    EditText enter_remark = (EditText)service_history_dialog.findViewById(R.id.enter_remark);
                    ImageView send_remark = (ImageView)service_history_dialog.findViewById(R.id.send_remark);
                    LinearLayoutManager layoutManager = new LinearLayoutManager(activity.getApplicationContext());

                    androidx.recyclerview.widget.RecyclerView recyclerView
                            = (androidx.recyclerview.widget.RecyclerView) service_history_dialog.findViewById(R.id.service_history_recycleview);
                    Adapter_For_Make_Service_History adapter_for_make_service_history = new Adapter_For_Make_Service_History(activity,databaseReference,customer_class.getFrame_no());
                    DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(activity,DividerItemDecoration.VERTICAL);
                    dividerItemDecoration.setDrawable(ContextCompat.getDrawable(activity,R.drawable.divider));
                    recyclerView.addItemDecoration(dividerItemDecoration);
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setAdapter(adapter_for_make_service_history);
                    layoutManager.setStackFromEnd(true);
                    send_remark.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(!enter_remark.getText().toString().equals("")){
                                BASIC_DATA_HOLDER.progress_bar();
                                String new_remark = enter_remark.getText().toString();
                                long date = Calendar.getInstance().getTimeInMillis();
                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd 'at' HH:mm:ss");
                                String new_date = dateFormat.format(date);
                                Remarks_Class remarks_class = new Remarks_Class(new_date,new_remark);
                                databaseReference.child("remarks").child(customer_class.getFrame_no()).push().setValue(remarks_class).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        BASIC_DATA_HOLDER.loading_dialog.dismiss();
                                        enter_remark.setText("");
                                    }
                                });
                            }else{
                                Toast.makeText(activity, "Please Add Remark", Toast.LENGTH_SHORT).show();
                            }
                        }

                    });

                    service_history_dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                    service_history_dialog.show();
                }

            });
            holder.cancel_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    remove_after_message_sent(snapshot.getKey());
                }
            });

            holder.reminder.setOnClickListener(new View.OnClickListener() {
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

                            if(cal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY){
                                String reminder_date = String.valueOf(cal.getTimeInMillis());
                                Customer_Class customer_class_for_reminder = customer_class;
                                customer_class_for_reminder.setRecycle_date(reminder_date);
                                customer_class_for_reminder.setService_type(BASIC_DATA_HOLDER.getCalling_type());
                                databaseReference.child(KEY_NAME_REMINDER + "_" + BASIC_DATA_HOLDER.getUser()).child(customer_class.getFrame_no()).setValue(customer_class).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(activity, "Reminder Set Successfully", Toast.LENGTH_SHORT).show();

                                    }
                                });
                            }else{
                                Toast.makeText(activity, "Sunday Reminder Not Allowed", Toast.LENGTH_SHORT).show();
                            }


                        }
                    };
                    new DatePickerDialog(activity,android.R.style.Theme_Holo_Light_Dialog_MinWidth,dateSetListener,cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show();
                }
            });
            if(global_mobile1_selection){
                Make_Contact.last_attempt = holder.mobile_one_value.getText().toString();
            }else{
                Make_Contact.last_attempt = holder.mobile_two_value.getText().toString();
            }
            if(BASIC_DATA_HOLDER.isMessage_mode()){
                holder.cancel_button.setVisibility(View.INVISIBLE);
                holder.call_button.setVisibility(View.INVISIBLE);
                holder.service_history_button.setVisibility(View.INVISIBLE);
            }else{
                holder.cancel_button.setVisibility(View.VISIBLE);
                holder.call_button.setVisibility(View.VISIBLE);
                holder.service_history_button.setVisibility(View.VISIBLE);
                holder.english_check_box.setVisibility(View.VISIBLE);
            }
        }else{
            holder.mobile_selected.setChecked(Make_Contact.mobile_one_value);
            holder.phone_selected.setChecked(Make_Contact.mobile_two_value);

            Customer_Class customer_class = snapshot.getValue(Customer_Class.class);
            assert customer_class != null;
            holder.customer_name_value.setText(customer_class.getCustomer_name());
            holder.customer_vehicle.setText(customer_class.getModel());
            holder.service_due_value.setText(customer_class.getDue_date());
            holder.frame_number_value.setText(customer_class.getFrame_no());
            holder.address_line_one.setText(customer_class.address_line_2);
            holder.address_line_two.setText(customer_class.getAddress_line_3());
            holder.registration_value.setText(customer_class.getRegistration());
            if (customer_class.getMobile_1() != null && !customer_class.getMobile_1().equals("")) {
                holder.mobile_one_value.setText(String.valueOf(customer_class.mobile_1));
            }
            if (customer_class.getMobile_1() == 0) {
                holder.mobile_one_value.setText("");
            }
            if (customer_class.getMobile_2() != null) {
                holder.mobile_two_value.setText(String.valueOf(customer_class.mobile_2));
            }
            if (customer_class.getMobile_2() == 0) {
                holder.mobile_two_value.setText("");
            }

            holder.english_check_box.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    BASIC_DATA_HOLDER.progress_bar();
                    active_view_position = position;
                    global_mobile1_selection = holder.mobile_selected.isChecked();
                    global_mobile2_selection = holder.phone_selected.isChecked();
                    Make_Contact.sending_message = true;
                    calendar.set(Calendar.HOUR, 0);
                    calendar.set(Calendar.MILLISECOND, 0);
                    calendar.set(Calendar.SECOND, 0);
                    calendar.set(Calendar.MINUTE, 0);
                    todays_date = calendar.getTimeInMillis();

                    frame_no = customer_class.getFrame_no();
                    customer_class_gloabl = customer_class;
                    if(global_mobile1_selection){
                        contact_number_in_use = String.valueOf(customer_class.getMobile_1());
                    }else{
                        contact_number_in_use = String.valueOf(customer_class.getMobile_2());
                    }

                    databaseReference.child("LOGS").child(contact_number_in_use).limitToLast(1).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                global_key = key_holder.get(position);
                                databaseReference.child("LOGS").child(contact_number_in_use).limitToLast(1).addChildEventListener(date_check);
                            } else {
                                if (holder.mobile_selected.isChecked() && customer_class.getMobile_1() != null) {
                                    try {
                                        String break_line = "%0A";
                                        String mobile = String.valueOf(customer_class.getMobile_1());
                                        String msg;
                                        if (service_status(customer_class.getDue_date())) {
                                            msg = "Dear " + customer_class.getCustomer_name() + " Ji,"+ break_line + "Your " + customer_class.getModel() + " is due for service. *The due date is " + customer_class.getDue_date() + ".*" + break_line + "Timely service of your Two-Wheeler *will give you good performance and great average*. Kindly bring your vehicle to the workshop soon." + break_line + "*Workshop Timming:*" + break_line +
                                                    "Monday - Friday : 9am - 6:00pm" + break_line + "Sunday : 9am - 3pm" + break_line + "Laxmi TVS" + break_line + "Call : 8872084211, 8872084210" + break_line + "" + break_line + "▬▬▬▬▬▬▬▬▬▬▬▬▬▬" + break_line + "" + break_line + customer_class.getCustomer_name() + " जी," + break_line + "आपके दुपहिया वाहन " + customer_class.getModel() + " की सर्विस का टाइम आ गया है| *हमारे रिकॉर्ड के अनुसार सर्विस " + customer_class.getDue_date() + " किया जाना चाहिए|*" + break_line + "टाइम से सर्विस करवाने का फ़ायदा यह है की आपको *बेहतरीन एवरेज और परफॉरमेंस* मिलती है| कृपया वर्कशॉप में जल्दी आए|" + break_line + "*वर्कशॉप का समय:*" + break_line + "सोमवार - शनिवार: सुबह 9 बजे से - शाम 6 बजे तक।" + break_line + "रविवार: सुबह 9 बजे से - दोपहर 3 बजे तक।" + break_line + "लक्ष्मी टीवीएस" + break_line + "कृपया कॉल करें : 8872084210, 8872084211";

                                        } else {
                                            msg =  "Dear " + customer_class.getCustomer_name() + " Ji,"+ break_line + "Your " + customer_class.getModel() + " has missed its service. *The due date was " + customer_class.getDue_date() + ".*" + break_line + "Without service *vehilce performance and average may not be best.* Kindly bring your vehicle to the workshop soon." + break_line + "*Workshop Timming:*" + break_line +
                                                    "Monday - Friday : 9am - 6:00pm" + break_line + "Sunday : 9am - 3pm" + break_line + "Laxmi TVS" + break_line + "Call : 8872084211, 8872084210" + break_line + "" + break_line + "▬▬▬▬▬▬▬▬▬▬▬▬▬▬" + break_line + "" + break_line + customer_class.getCustomer_name() + " जी," + break_line + "आपके दुपहिया वाहन " + customer_class.getModel() + " की सर्विस का टाइम चला गया गया है| *हमारे रिकॉर्ड के अनुसार सर्विस " + customer_class.getDue_date() + " को किया जाना चाहिए था|*" + break_line + "टाइम से सर्विस करवाने का फ़ायदा यह है की *आपको बेहतरीन एवरेज और परफॉरमेंस* मिलती है| कृपया वर्कशॉप में जल्दी आए|" + break_line + "*वर्कशॉप का समय:*" + break_line + "सोमवार - शनिवार: सुबह 9 बजे से - शाम 6 बजे तक।" + break_line + "रविवार: सुबह 9 बजे से - दोपहर 3 बजे तक।" + break_line + "लक्ष्मी टीवीएस" + break_line + "कृपया कॉल करें : 8872084210, 8872084211";
                                        }


                                        String mainMessage = "";

                                        for (String str : msg.split(break_line)) {

                                            mainMessage = mainMessage + str + "\n";

                                            Log.d("string_test", "call: " + mainMessage);
                                            ;
                                        }
                                        String user = "";
                                        String frame_no = customer_class.getFrame_no();
                                        String message = mainMessage;

                                        String contact_date = String.valueOf(todays_date);
                                        String contact_type = "WATSAPP";
                                        String contacted_number;
                                        if (holder.mobile_selected.isChecked()) {
                                            contacted_number = String.valueOf(customer_class.getMobile_1());

                                        } else {
                                            contacted_number = String.valueOf(customer_class.getMobile_2());
                                        }
                                        long push_value = Calendar.getInstance().getTimeInMillis();
                                        LOG_CLASS log_class = new LOG_CLASS(BASIC_DATA_HOLDER.getUser(), frame_no, message, contact_date, contact_type, contacted_number);
                                        databaseReference.child("LOGS").child(contact_number_in_use).child(String.valueOf(push_value)).setValue(log_class);

                                        activity.startActivityForResult(new Intent(Intent.ACTION_VIEW, Uri.parse("https://api.whatsapp.com/send?phone=91" + mobile + "&text=" + mainMessage)), Make_Contact.REQUEST_CODE);


                                        String key = snapshot.getKey();


                                        update_counter();
                                        BASIC_DATA_HOLDER.loading_dialog.dismiss();
                                    } catch (Exception e) {
                                        BASIC_DATA_HOLDER.loading_dialog.dismiss();
                                    }
                                } else if (holder.phone_selected.isChecked() && customer_class.getMobile_2() != null) {
                                    try {
                                        String break_line = "%0A";
                                        String mobile = String.valueOf(customer_class.getMobile_2());
                                        String msg;
                                        if (service_status(customer_class.getDue_date())) {
                                            msg = "Dear " + customer_class.getCustomer_name() + " Ji," + break_line + "Your " + customer_class.getModel() + " is due for service. *The due date is " + customer_class.getDue_date() + ".*" + break_line + "Timely service of your Two-Wheeler *will give you good performance and great average*. Kindly bring your vehicle to the workshop soon." + break_line + "*Workshop Timming:*" + break_line +
                                                    "Monday - Friday : 9am - 6:00pm" + break_line + "Sunday : 9am - 3pm" + break_line + "Laxmi TVS" + break_line + "Call : 8872084211, 8872084210" + break_line + "" + break_line + "▬▬▬▬▬▬▬▬▬▬▬▬▬▬" + break_line + "" + break_line + customer_class.getCustomer_name() + " जी," + break_line + "आपके दुपहिया वाहन " + customer_class.getModel() + " की सर्विस का टाइम आ गया है| *हमारे रिकॉर्ड के अनुसार सर्विस " + customer_class.getDue_date() + " किया जाना चाहिए|*" + break_line + "टाइम से सर्विस करवाने का फ़ायदा यह है की आपको *बेहतरीन एवरेज और परफॉरमेंस* मिलती है| कृपया वर्कशॉप में जल्दी आए|" + break_line + "*वर्कशॉप का समय:*" + break_line + "सोमवार - शनिवार: सुबह 9 बजे से - शाम 6 बजे तक।" + break_line + "रविवार: सुबह 9 बजे से - दोपहर 3 बजे तक।" + break_line + "लक्ष्मी टीवीएस" + break_line + "कृपया कॉल करें : 8872084210, 8872084211";

                                        } else {
                                            msg = "Dear " + customer_class.getCustomer_name() + " Ji," + break_line + "Your " + customer_class.getModel() + " has missed its service. *The due date was " + customer_class.getDue_date() + ".*" + break_line + "Without service *vehilce performance and average may not be best.* Kindly bring your vehicle to the workshop soon." + break_line + "*Workshop Timming:*" + break_line +
                                                    "Monday - Friday : 9am - 6:00pm" + break_line + "Sunday : 9am - 3pm" + break_line + "Laxmi TVS" + break_line + "Call : 8872084211, 8872084210" + break_line + "" + break_line + "▬▬▬▬▬▬▬▬▬▬▬▬▬▬" + break_line + "" + break_line + customer_class.getCustomer_name() + " जी," + break_line + "आपके दुपहिया वाहन " + customer_class.getModel() + " की सर्विस का टाइम चला गया गया है| *हमारे रिकॉर्ड के अनुसार सर्विस " + customer_class.getDue_date() + " को किया जाना चाहिए था|*" + break_line + "टाइम से सर्विस करवाने का फ़ायदा यह है की *आपको बेहतरीन एवरेज और परफॉरमेंस* मिलती है| कृपया वर्कशॉप में जल्दी आए|" + break_line + "*वर्कशॉप का समय:*" + break_line + "सोमवार - शनिवार: सुबह 9 बजे से - शाम 6 बजे तक।" + break_line + "रविवार: सुबह 9 बजे से - दोपहर 3 बजे तक।" + break_line + "लक्ष्मी टीवीएस" + break_line + "कृपया कॉल करें : 8872084210, 8872084211";
                                        }


                                        String mainMessage = "";

                                        for (String str : msg.split(break_line)) {

                                            mainMessage = mainMessage + str + "\n";

                                            Log.d("string_test", "call: " + mainMessage);
                                            ;
                                        }
                                        String user = "";
                                        String frame_no = customer_class.getFrame_no();
                                        String message = mainMessage;

                                        String contact_date = String.valueOf(todays_date);
                                        String contact_type = "WATSAPP";
                                        String contacted_number;
                                        if (holder.mobile_selected.isChecked()) {
                                            contacted_number = String.valueOf(customer_class.getMobile_1());

                                        } else {
                                            contacted_number = String.valueOf(customer_class.getMobile_2());
                                        }

                                        long push_value = Calendar.getInstance().getTimeInMillis();
                                        LOG_CLASS log_class = new LOG_CLASS(BASIC_DATA_HOLDER.getUser(), frame_no, message, contact_date, contact_type, contacted_number);
                                        databaseReference.child("LOGS").child(contact_number_in_use).child(String.valueOf(push_value)).setValue(log_class);

                                        activity.startActivityForResult(new Intent(Intent.ACTION_VIEW, Uri.parse("https://api.whatsapp.com/send?phone=91" + mobile + "&text=" + mainMessage)), Make_Contact.REQUEST_CODE);
                                        String key = snapshot.getKey();


                                        update_counter();
                                        BASIC_DATA_HOLDER.loading_dialog.dismiss();
                                    } catch (Exception e) {
                                        BASIC_DATA_HOLDER.loading_dialog.dismiss();
                                        //whatsapp app not install
                                    }
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            BASIC_DATA_HOLDER.loading_dialog.dismiss();
                        }
                    });


                }
            });


            global_mobile1_selection = holder.mobile_selected.isChecked();
            global_mobile2_selection = holder.phone_selected.isChecked();
            holder.mobile_selected.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (holder.mobile_selected.isChecked()) {
                        holder.phone_selected.setChecked(false);
                        global_mobile1_selection = true;
                        Make_Contact.mobile_one_value = true;
                        Make_Contact.mobile_two_value = false;
                    } else {
                        holder.phone_selected.setChecked(true);
                        global_mobile1_selection = false;
                        Make_Contact.mobile_one_value = false;
                        Make_Contact.mobile_two_value = true;
                    }
                }
            });

            holder.phone_selected.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (holder.phone_selected.isChecked()) {
                        holder.mobile_selected.setChecked(false);
                        global_mobile2_selection = true;
                    } else {
                        holder.mobile_selected.setChecked(true);
                        global_mobile2_selection = false;
                    }
                }
            });
            holder.call_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    customer_class_gloabl = customer_class;
                    active_view_position = position;
                        // Create the intent.
                        callIntent = new Intent(Intent.ACTION_CALL);
                        // Set the data for the intent as the phone number.

                        if (holder.mobile_selected.isChecked()) {
                            callIntent.setData(Uri.parse("tel:" + holder.mobile_one_value.getText().toString()));
                            // If package resolves to an app, send intent.
                            if (callIntent.resolveActivity(activity.getPackageManager()) != null) {
                                    Make_Contact.sending_message = true;
                                    activity.startActivityForResult(callIntent,1234);

                            } else {

                                Log.e("calling", "Can't resolve app for ACTION_CALL Intent.");
                            }
                        }else{
                            callIntent.setData(Uri.parse("tel:" + holder.mobile_two_value.getText().toString()));
                            // If package resolves to an app, send intent.
                            if (callIntent.resolveActivity(activity.getPackageManager()) != null) {

                                    Make_Contact.sending_message = true;
                                    activity.startActivity(callIntent);

                            } else {

                                Log.e("calling", "Can't resolve app for ACTION_CALL Intent.");
                            }
                        }

                }
            });
            holder.service_history_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    service_history_dialog = new Dialog(activity);
                    service_history_dialog.setCancelable(true);
                    service_history_dialog.setContentView(R.layout.service_history);
                    EditText enter_remark = (EditText)service_history_dialog.findViewById(R.id.enter_remark);
                    ImageView send_remark = (ImageView)service_history_dialog.findViewById(R.id.send_remark);
                    LinearLayoutManager layoutManager = new LinearLayoutManager(activity.getApplicationContext());

                    androidx.recyclerview.widget.RecyclerView recyclerView
                            = (androidx.recyclerview.widget.RecyclerView) service_history_dialog.findViewById(R.id.service_history_recycleview);
                    Adapter_For_Make_Service_History adapter_for_make_service_history = new Adapter_For_Make_Service_History(activity,databaseReference,customer_class.getFrame_no());
                    DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(activity,DividerItemDecoration.VERTICAL);
                    dividerItemDecoration.setDrawable(ContextCompat.getDrawable(activity,R.drawable.divider));
                    recyclerView.addItemDecoration(dividerItemDecoration);
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setAdapter(adapter_for_make_service_history);
                    layoutManager.setStackFromEnd(true);
                    send_remark.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(!enter_remark.getText().toString().equals("")){
                                BASIC_DATA_HOLDER.progress_bar();
                                String new_remark = enter_remark.getText().toString();
                                long date = Calendar.getInstance().getTimeInMillis();
                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd 'at' HH:mm:ss");
                                String new_date = dateFormat.format(date);
                                Remarks_Class remarks_class = new Remarks_Class(new_date,new_remark);
                                databaseReference.child("remarks").child(customer_class.getFrame_no()).push().setValue(remarks_class).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        BASIC_DATA_HOLDER.loading_dialog.dismiss();
                                        enter_remark.setText("");
                                    }
                                });
                            }else{
                                Toast.makeText(activity, "Please Add Remark", Toast.LENGTH_SHORT).show();
                            }
                        }

                    });

                    service_history_dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                    service_history_dialog.show();
                }

            });
            holder.cancel_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("snapshot_key", "onBindViewHolder: " + key_value);
                    remove_after_message_sent(key_value);
                }
            });
            holder.mobile_selected.setChecked(Make_Contact.mobile_one_value);
            holder.phone_selected.setChecked(Make_Contact.mobile_two_value);

            holder.reminder.setOnClickListener(new View.OnClickListener() {
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

                            if(cal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY){
                                String reminder_date = String.valueOf(cal.getTimeInMillis());
                                Customer_Class customer_class_for_reminder = customer_class;
                                customer_class_for_reminder.setRecycle_date(reminder_date);
                                customer_class_for_reminder.setService_type(BASIC_DATA_HOLDER.getCalling_type());
                                databaseReference.child(KEY_NAME_REMINDER+ "_" + BASIC_DATA_HOLDER.getUser()).child(customer_class.getFrame_no()).setValue(customer_class).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(activity, "Reminder Set Successfully", Toast.LENGTH_SHORT).show();

                                    }
                                });
                            }else{
                                Toast.makeText(activity, "Sunday Reminder Not Allowed", Toast.LENGTH_SHORT).show();
                            }


                        }
                    };
                    new DatePickerDialog(activity,android.R.style.Theme_Holo_Light_Dialog_MinWidth,dateSetListener,cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show();
                }
            });
            if(BASIC_DATA_HOLDER.isMessage_mode()){
                holder.cancel_button.setVisibility(View.INVISIBLE);
                holder.call_button.setVisibility(View.INVISIBLE);
                holder.service_history_button.setVisibility(View.INVISIBLE);
            }else{
                holder.cancel_button.setVisibility(View.VISIBLE);
                holder.call_button.setVisibility(View.VISIBLE);
                holder.service_history_button.setVisibility(View.VISIBLE);
                holder.english_check_box.setVisibility(View.VISIBLE);
            }
        }


    }

    @Override
    public int getItemCount() {
        if(filter_value.equals("")){
            return hashMap.size();
        }else{
          return  searchviewMap.size();
        }
    }

    public class Holder extends RecyclerView.ViewHolder {

        TextView customer_name_value;
        TextView customer_vehicle;
        TextView service_due_value;
        TextView frame_number_value;
        TextView address_line_one;
        TextView address_line_two;
        TextView mobile_one_value;
        TextView mobile_two_value;
        TextView service_type_value;
        TextView recycle_date_value;
        TextView registration_value;
        ImageButton english_check_box;
        ImageButton setappointment;
        ImageView call_button;
        CheckBox mobile_selected;
        CheckBox phone_selected;
        ImageButton service_history_button;
        ImageButton cancel_button;
        ImageButton reminder;



        public Holder(@NonNull View itemView) {
            super(itemView);

            customer_name_value = (TextView) itemView.findViewById(R.id.customer_name_value);
            customer_vehicle = (TextView) itemView.findViewById(R.id.customer_vehicle);
            service_due_value = (TextView) itemView.findViewById(R.id.service_due_value);
            frame_number_value = (TextView) itemView.findViewById(R.id.frame_number_value);
            address_line_one = (TextView) itemView.findViewById(R.id.address_line_one);
            address_line_two = (TextView) itemView.findViewById(R.id.address_line_two);
            service_type_value = (TextView)itemView.findViewById(R.id.service_type_value);
            recycle_date_value = (TextView)itemView.findViewById(R.id.recycle_date_value);
            mobile_one_value = (TextView) itemView.findViewById(R.id.mobile_one_value);
            mobile_two_value = (TextView) itemView.findViewById(R.id.mobile_two_value);
            english_check_box = (ImageButton) itemView.findViewById(R.id.english_check_box);
            call_button = (ImageView) itemView.findViewById(R.id.call_button);
            mobile_selected = (CheckBox) itemView.findViewById(R.id.mobile_selected);
            phone_selected = (CheckBox) itemView.findViewById(R.id.phone_selected);
            service_history_button = (ImageButton) itemView.findViewById(R.id.service_history_button);
            cancel_button = (ImageButton) itemView.findViewById(R.id.cancel);
            reminder = (ImageButton) itemView.findViewById(R.id.reminder);
            registration_value = (TextView) itemView.findViewById(R.id.registration_value);
            setappointment = (ImageButton) itemView.findViewById(R.id.setappointment);
        }
    }


    ChildEventListener childEventListener = new ChildEventListener() {
        @Override
        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
          if(previousChildName==null){
              BASIC_DATA_HOLDER.loading_dialog.dismiss();
          }
            hashMap.put(snapshot.getKey(), snapshot);
            Log.d("testing", "onChildAdded: " + snapshot.getKey());
            String key = snapshot.getKey();
            key_holder.add(key);
            notifyDataSetChanged();
        }

        @Override
        public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

        }

        @Override
        public void onChildRemoved(@NonNull DataSnapshot snapshot) {
            String key = snapshot.getKey();
            hashMap.remove(String.valueOf(key));
            key_holder.remove(key);

            searchviewMap.remove(String.valueOf(key));
            search_view_key_holder_array.remove(key);

            notifyDataSetChanged();
        }

        @Override
        public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    };

    public void cleanup() {
        databaseReference.child(BASIC_DATA_HOLDER.getCalling_type()).removeEventListener(childEventListener);
        hashMap.clear();
        searchviewMap.clear();
        search_view_key_holder_array.clear();
    }

    public boolean service_status(String due_date_as_received) {

        String string_date = due_date_as_received;
        Log.d("due_date_status", "service_status: " + due_date_as_received);
        SimpleDateFormat f = new SimpleDateFormat("dd-MMM-yy");
        try {
            Date d = f.parse(string_date);
            long milliseconds = d.getTime();
            long todays_time = Calendar.getInstance().getTimeInMillis();
            Log.d("due_date_status", "due_date: " + milliseconds);
            Log.d("due_date_status", "todays date: " + milliseconds);
            if (milliseconds >= todays_time) {
                return true;
            } else {
                return false;
            }

        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }

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
        databaseReference.child("Counter").child(string_today).child(BASIC_DATA_HOLDER.getUser()).child(KEY_NAME_MESSAGE).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Make_Contact.counter_text_box.setText(snapshot.getValue(String.class));
                } else {
                    databaseReference.child("Counter").child(string_today).child(BASIC_DATA_HOLDER.getUser()).child(KEY_NAME_MESSAGE).setValue("0");
                    Make_Contact.counter_text_box.setText("0");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Make_Contact.counter_text_box.setText("0");
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
        int current = Integer.parseInt(Make_Contact.counter_text_box.getText().toString());
        int new_counter = current + 1;
        databaseReference.child("Counter").child(string_today).child(BASIC_DATA_HOLDER.getUser()).child(KEY_NAME_MESSAGE).setValue(String.valueOf(new_counter));
    }


    ChildEventListener date_check = new ChildEventListener() {
        @Override
        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            if (previousChildName == null) {
                databaseReference.child("LOGS").child(contact_number_in_use).limitToLast(1).removeEventListener(date_check);
                String previous_date_value = snapshot.child("contact_date").getValue(String.class);
                Log.d("snapshot", "onChildAdded: key " + snapshot.getKey());
                long previous_date_long = Long.parseLong(previous_date_value);
                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.HOUR, 0);
                cal.set(Calendar.MINUTE, 0);
                cal.set(Calendar.SECOND, 0);
                cal.set(Calendar.MILLISECOND, 0);
                long today = cal.getTimeInMillis();
                Log.d("snapshot", "onChildAdded: " + today);
                Log.d("snapshot", "previous date: " + previous_date_long);
                if (today - previous_date_long >= 345600000) {
                    if (global_mobile1_selection && customer_class_gloabl.getMobile_1() != null) {
                        try {
                            Log.d("snapshot", "location : childevent ok ");
                            String break_line = "%0A";
                            String mobile = String.valueOf(customer_class_gloabl.getMobile_1());
                            String msg;
                            if (service_status(customer_class_gloabl.getDue_date())) {
                                msg = "Dear " + customer_class_gloabl.getCustomer_name() + " Ji," + break_line + "Your " + customer_class_gloabl.getModel() + " is due for service. *The due date is " + customer_class_gloabl.getDue_date() + ".*" + break_line + "Timely service of your Two-Wheeler *will give you good performance and great average*. Kindly bring your vehicle to the workshop soon." + break_line + "*Workshop Timming:*" + break_line +
                                        "Monday - Friday : 9am - 6:00pm" + break_line + "Sunday : 9am - 3pm" + break_line + "Laxmi TVS" + break_line + "Call : 8872084211, 8872084210" + break_line + "" + break_line + "±±±±±±±±±±±±±±±±±±±±±±±±±±±±±±" + break_line + "" + break_line + customer_class_gloabl.getCustomer_name() + " जी," + break_line + "आपके दुपहिया वाहन " + customer_class_gloabl.getModel() + " की सर्विस का टाइम आ गया है| *हमारे रिकॉर्ड के अनुसार सर्विस " + customer_class_gloabl.getDue_date() + " किया जाना चाहिए|*" + break_line + "टाइम से सर्विस करवाने का फ़ायदा यह है की आपको *बेहतरीन एवरेज और परफॉरमेंस* मिलती है| कृपया वर्कशॉप में जल्दी आए|" + break_line + "*वर्कशॉप का समय:*" + break_line + "सोमवार - शनिवार: सुबह 9 बजे से - शाम 6 बजे तक।" + break_line + "रविवार: सुबह 9 बजे से - दोपहर 3 बजे तक।" + break_line + "लक्ष्मी टीवीएस" + break_line + "कृपया कॉल करें : 8872084210, 8872084211";

                            } else {
                                msg = "Dear " + customer_class_gloabl.getCustomer_name() + " Ji," + break_line + "Your " + customer_class_gloabl.getModel() + " has missed its service. *The due date was " + customer_class_gloabl.getDue_date() + ".*" + break_line + "Without service *vehilce performance and average may not be best.* Kindly bring your vehicle to the workshop soon." + break_line + "*Workshop Timming:*" + break_line +
                                        "Monday - Friday : 9am - 6:00pm" + break_line + "Sunday : 9am - 3pm" + break_line + "Laxmi TVS" + break_line + "Call : 8872084211, 8872084210" + break_line + "" + break_line + "±±±±±±±±±±±±±±±±±±±±±±±±±±±±±±" + break_line + "" + break_line + customer_class_gloabl.getCustomer_name() + " जी," + break_line + "आपके दुपहिया वाहन " + customer_class_gloabl.getModel() + " की सर्विस का टाइम चला गया गया है| *हमारे रिकॉर्ड के अनुसार सर्विस " + customer_class_gloabl.getDue_date() + " को किया जाना चाहिए था|*" + break_line + "टाइम से सर्विस करवाने का फ़ायदा यह है की *आपको बेहतरीन एवरेज और परफॉरमेंस* मिलती है| कृपया वर्कशॉप में जल्दी आए|" + break_line + "*वर्कशॉप का समय:*" + break_line + "सोमवार - शनिवार: सुबह 9 बजे से - शाम 6 बजे तक।" + break_line + "रविवार: सुबह 9 बजे से - दोपहर 3 बजे तक।" + break_line + "लक्ष्मी टीवीएस" + break_line + "कृपया कॉल करें : 8872084210, 8872084211";
                            }


                            String mainMessage = "";

                            for (String str : msg.split(break_line)) {

                                mainMessage = mainMessage + str + "\n";

                                Log.d("string_test", "call: " + mainMessage);
                                ;
                            }
                            String user = "";
                            String frame_no = customer_class_gloabl.getFrame_no();
                            String message = mainMessage;

                            String contact_date = String.valueOf(todays_date);
                            String contact_type = "WATSAPP";
                            String contacted_number;
                            if (global_mobile1_selection) {
                                contacted_number = String.valueOf(customer_class_gloabl.getMobile_1());

                            } else {
                                contacted_number = String.valueOf(customer_class_gloabl.getMobile_2());
                            }
                            long push_value = Calendar.getInstance().getTimeInMillis();

                            LOG_CLASS log_class = new LOG_CLASS(BASIC_DATA_HOLDER.getUser(), frame_no, message, contact_date, contact_type, contacted_number);
                            databaseReference.child("LOGS").child(contact_number_in_use).child(String.valueOf(push_value)).setValue(log_class);

                            activity.startActivityForResult(new Intent(Intent.ACTION_VIEW, Uri.parse("https://api.whatsapp.com/send?phone=91" + mobile + "&text=" + mainMessage)), Make_Contact.REQUEST_CODE);


                            String key = snapshot.getKey();


                            update_counter();
                            BASIC_DATA_HOLDER.loading_dialog.dismiss();
                        } catch (Exception e) {
                            BASIC_DATA_HOLDER.loading_dialog.dismiss();
                            //whatsapp app not install
                        }
                    } else if (global_mobile2_selection && customer_class_gloabl.getMobile_2() != null) {
                        try {
                            String break_line = "%0A";
                            String mobile = String.valueOf(customer_class_gloabl.getMobile_2());
                            String msg;
                            if (service_status(customer_class_gloabl.getDue_date())) {
                                msg = "Dear " + customer_class_gloabl.getCustomer_name() + " Ji," + break_line + "Your " + customer_class_gloabl.getModel() + " is due for service. *The due date is " + customer_class_gloabl.getDue_date() + ".*" + break_line + "Timely service of your Two-Wheeler *will give you good performance and great average*. Kindly bring your vehicle to the workshop soon." + break_line + "*Workshop Timming:*" + break_line +
                                        "Monday - Friday : 9am - 6:00pm" + break_line + "Sunday : 9am - 3pm" + break_line + "Laxmi TVS" + break_line + "Call : 8872084211, 8872084210" + break_line + "" + break_line + "±±±±±±±±±±±±±±±±±±±±±±±±±±±±±±" + break_line + "" + break_line + customer_class_gloabl.getCustomer_name() + " जी," + break_line + "आपके दुपहिया वाहन " + customer_class_gloabl.getModel() + " की सर्विस का टाइम आ गया है| *हमारे रिकॉर्ड के अनुसार सर्विस " + customer_class_gloabl.getDue_date() + " किया जाना चाहिए|*" + break_line + "टाइम से सर्विस करवाने का फ़ायदा यह है की आपको *बेहतरीन एवरेज और परफॉरमेंस* मिलती है| कृपया वर्कशॉप में जल्दी आए|" + break_line + "*वर्कशॉप का समय:*" + break_line + "सोमवार - शनिवार: सुबह 9 बजे से - शाम 6 बजे तक।" + break_line + "रविवार: सुबह 9 बजे से - दोपहर 3 बजे तक।" + break_line + "लक्ष्मी टीवीएस" + break_line + "कृपया कॉल करें : 8872084210, 8872084211";

                            } else {
                                msg = "Dear " + customer_class_gloabl.getCustomer_name() + " Ji," + break_line + "Your " + customer_class_gloabl.getModel() + " has missed its service. *The due date was " + customer_class_gloabl.getDue_date() + ".*" + break_line + "Without service *vehilce performance and average may not be best.* Kindly bring your vehicle to the workshop soon." + break_line + "*Workshop Timming:*" + break_line +
                                        "Monday - Friday : 9am - 6:00pm" + break_line + "Sunday : 9am - 3pm" + break_line + "Laxmi TVS" + break_line + "Call : 8872084211, 8872084210" + break_line + "" + break_line + "±±±±±±±±±±±±±±±±±±±±±±±±±±±±±±" + break_line + "" + break_line + customer_class_gloabl.getCustomer_name() + " जी," + break_line + "आपके दुपहिया वाहन " + customer_class_gloabl.getModel() + " की सर्विस का टाइम चला गया गया है| *हमारे रिकॉर्ड के अनुसार सर्विस " + customer_class_gloabl.getDue_date() + " को किया जाना चाहिए था|*" + break_line + "टाइम से सर्विस करवाने का फ़ायदा यह है की *आपको बेहतरीन एवरेज और परफॉरमेंस* मिलती है| कृपया वर्कशॉप में जल्दी आए|" + break_line + "*वर्कशॉप का समय:*" + break_line + "सोमवार - शनिवार: सुबह 9 बजे से - शाम 6 बजे तक।" + break_line + "रविवार: सुबह 9 बजे से - दोपहर 3 बजे तक।" + break_line + "लक्ष्मी टीवीएस" + break_line + "कृपया कॉल करें : 8872084210, 8872084211";
                            }


                            String mainMessage = "";

                            for (String str : msg.split(break_line)) {

                                mainMessage = mainMessage + str + "\n";

                                Log.d("string_test", "call: " + mainMessage);
                                ;
                            }
                            String user = "";
                            String frame_no = customer_class_gloabl.getFrame_no();
                            String message = mainMessage;

                            String contact_date = String.valueOf(todays_date);
                            String contact_type = "WATSAPP";
                            String contacted_number;
                            if (global_mobile2_selection) {
                                contacted_number = String.valueOf(customer_class_gloabl.getMobile_1());

                            } else {
                                contacted_number = String.valueOf(customer_class_gloabl.getMobile_2());
                            }
                            long push_value = Calendar.getInstance().getTimeInMillis();

                            LOG_CLASS log_class = new LOG_CLASS(BASIC_DATA_HOLDER.getUser(), frame_no, message, contact_date, contact_type, contacted_number);
                            databaseReference.child("LOGS").child(contact_number_in_use).child(String.valueOf(push_value)).setValue(log_class);

                            activity.startActivityForResult(new Intent(Intent.ACTION_VIEW, Uri.parse("https://api.whatsapp.com/send?phone=91" + mobile + "&text=" + mainMessage)), Make_Contact.REQUEST_CODE);
                            String key = snapshot.getKey();


                            update_counter();
                            BASIC_DATA_HOLDER.loading_dialog.dismiss();
                        } catch (Exception e) {
                            BASIC_DATA_HOLDER.loading_dialog.dismiss();
                            //whatsapp app not install
                        }
                    }
                } else {
                    if(BASIC_DATA_HOLDER.isMessage_mode()){
                       // DataSnapshot snapshot1 = Adapter_For_Make_Contact.hashMap.get(Adapter_For_Make_Contact.key_holder.get(0));
                        remove_after_message_sent(global_key);
                        BASIC_DATA_HOLDER.loading_dialog.dismiss();
                    }else{
                        BASIC_DATA_HOLDER.loading_dialog.dismiss();
                        Toast.makeText(activity, "Message Sent In Last 4 Days", Toast.LENGTH_SHORT).show();
                    }

                }
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
    };

    public void remove_after_message_sent(String key) {
        Log.d("remove", "remove_after_message_sent: " + key);
        databaseReference.child(BASIC_DATA_HOLDER.getCalling_type()+"_"+BASIC_DATA_HOLDER.getUser()).child(key).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d("remove", "removed successfully ");
                BASIC_DATA_HOLDER.loading_dialog.dismiss();
                Make_Contact.sending_message = false;
            }
        });
    }



    public boolean getPermissions() {

        if (Build.VERSION.SDK_INT >= 29) {


            // permissions video https://www.youtube.com/watch?v=AyhkpvQwFsI


            if ((ContextCompat.checkSelfPermission(activity, Manifest.permission.CALL_PHONE)) +
                    (ContextCompat.checkSelfPermission(activity, Manifest.permission.SEND_SMS)
                    ) + (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_MEDIA_LOCATION))
                        + (ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_CALL_LOG)) +

                    (ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_CONTACTS))
                    != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.

                if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.CALL_PHONE) ||
                        ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.SEND_SMS)
                        ||
                        ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.ACCESS_MEDIA_LOCATION)||
                        ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.READ_CALL_LOG) ||
                        ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.READ_CONTACTS)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                    builder.setTitle("Please Grant Permissions");
                    builder.setMessage("These Permissions are necessary for the App to function properly ");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CALL_PHONE
                                    , Manifest.permission.SEND_SMS, Manifest.permission.ACCESS_MEDIA_LOCATION}, REQUEST_CODE);
                        }
                    });
                    builder.setNegativeButton("Cancel", null);
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                } else {
                    ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CALL_PHONE
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
            if ((ContextCompat.checkSelfPermission(activity, Manifest.permission.CALL_PHONE)) +
                    (ContextCompat.checkSelfPermission(activity, Manifest.permission.SEND_SMS)
                    ) + (ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_CALL_LOG)
            ) + (ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_CONTACTS)
            )
                    != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.

                if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.CALL_PHONE) ||
                        ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.SEND_SMS) ||
                ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.READ_CALL_LOG)
                || ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.READ_CONTACTS)){
                    AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                    builder.setTitle("Please Grant Permissions");
                    builder.setMessage("These Permissions are necessary for the App to function properly ");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CALL_PHONE
                                    , Manifest.permission.SEND_SMS}, REQUEST_CODE);
                        }
                    });
                    builder.setNegativeButton("Cancel", null);
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                } else {
                    ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CALL_PHONE
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

    public void last_call_details (){


        if(global_mobile1_selection && !last_dialled_number.equals(String.valueOf(customer_class_gloabl.getMobile_1()))){
            Log.d("last_call", "last dialled number: " + last_dialled_number);
            Log.d("last_call", "number sent for dail mobile 1" + customer_class_gloabl.getMobile_1());
            Call_Details callDetails = new Call_Details();
            Cursor cursor;
            int offset = 1;

            Uri contacts = CallLog.Calls.CONTENT_URI;


                //Cursor managedCursor = activity.getContentResolver().query(contacts, null, null, null, android.provider.CallLog.Calls.DATE + " DESC limit 1;");

                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
                    Log.d("last_call", "version less than 30 ");
                    cursor =  activity.getContentResolver().query(
                            CallLog.Calls.CONTENT_URI.buildUpon().appendQueryParameter(LIMIT_PARAM_KEY, "1")
                                    .build(),
                            null, null, null, android.provider.CallLog.Calls.DATE + " DESC limit 1;");

                    try{
                        if (cursor.moveToFirst()) {
                            int number = cursor.getColumnIndex(CallLog.Calls.NUMBER);
                            int duration = cursor.getColumnIndex(CallLog.Calls.DURATION);
                            int date = cursor.getColumnIndex(CallLog.Calls.DATE);
                            int incomingtype = cursor.getColumnIndex(String.valueOf(CallLog.Calls.INCOMING_TYPE));
                            Log.d("last_call", "int duration " + duration);
                            Log.d("last_call", "int number " + number);
                            Log.d("last_call", "int date " + date);

                            String callType;
                            String phNumber = cursor.getString(number);
                            if (incomingtype == -1) {
                                callType = "incoming";
                            } else {
                                callType = "outgoing";
                            }
                            String callDate = cursor.getString(date);
                            String callDayTime = new Date(Long.valueOf(callDate)).toString();

                            String callDuration = cursor.getString(duration);


                            Call_Details.setCall_number(phNumber);
                            Call_Details.setDuration(callDuration);
                            Call_Details.setCall_time_stamp(callDayTime);

                            cursor.close();
                        }
                    } catch (SecurityException e) {
                        Log.e("Security Exception", "User denied call log permission");

                    }
                } else {
                    Log.d("last_call", "version greater than 30 ");
                    Bundle bundle = new Bundle();
                    bundle.putInt(LIMIT_PARAM_KEY,1);
                    cursor = activity
                            .getContentResolver()
                            .query(contacts,null,
                                    bundle,
                                    null);
                    try{
                        if (cursor.moveToLast()) {
                            int number = cursor.getColumnIndex(CallLog.Calls.NUMBER);
                            int duration = cursor.getColumnIndex(CallLog.Calls.DURATION);
                            int date = cursor.getColumnIndex(CallLog.Calls.DATE);
                            int incomingtype = cursor.getColumnIndex(String.valueOf(CallLog.Calls.INCOMING_TYPE));
                            Log.d("last_call", "int duration " + duration);
                            Log.d("last_call", "int number " + number);
                            Log.d("last_call", "int date " + date);

                            String callType;
                            String phNumber = cursor.getString(number);
                            if (incomingtype == -1) {
                                callType = "incoming";
                            } else {
                                callType = "outgoing";
                            }
                            String callDate = cursor.getString(date);
                            String callDayTime = new Date(Long.valueOf(callDate)).toString();

                            String callDuration = cursor.getString(duration);


                            Call_Details.setCall_number(phNumber);
                            Call_Details.setDuration(callDuration);
                            Call_Details.setCall_time_stamp(callDayTime);

                            cursor.close();
                        }
                    } catch (SecurityException e) {
                        Log.e("Security Exception", "User denied call log permission");

                    }
                }


        }else if(global_mobile2_selection && !last_dialled_number.equals(String.valueOf(customer_class_gloabl.getMobile_2()))){
            Log.d("last_call", "last dialled number: " + last_dialled_number);
            Log.d("last_call", "number sent for dail mobile 2" + customer_class_gloabl.getMobile_2());
            Call_Details callDetails = new Call_Details();
            Cursor cursor;
            int offset = 1;

            Uri contacts = CallLog.Calls.CONTENT_URI;


                //Cursor managedCursor = activity.getContentResolver().query(contacts, null, null, null, android.provider.CallLog.Calls.DATE + " DESC limit 1;");

                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
                    Log.d("call+duration", "last_call stage one ");
                    cursor =  activity.getContentResolver().query(
                            CallLog.Calls.CONTENT_URI.buildUpon().appendQueryParameter(LIMIT_PARAM_KEY, "1")
                                    .build(),
                            null, null, null, android.provider.CallLog.Calls.DATE + " DESC limit 1;");

                    try{
                        if (cursor.moveToFirst()) {
                            int number = cursor.getColumnIndex(CallLog.Calls.NUMBER);
                            int duration = cursor.getColumnIndex(CallLog.Calls.DURATION);
                            int date = cursor.getColumnIndex(CallLog.Calls.DATE);
                            int incomingtype = cursor.getColumnIndex(String.valueOf(CallLog.Calls.INCOMING_TYPE));
                            Log.d("last_call", "int duration " + duration);
                            Log.d("last_call", "int number " + number);
                            Log.d("last_call", "int date " + date);

                            String callType;
                            String phNumber = cursor.getString(number);
                            if (incomingtype == -1) {
                                callType = "incoming";
                            } else {
                                callType = "outgoing";
                            }
                            String callDate = cursor.getString(date);
                            String callDayTime = new Date(Long.valueOf(callDate)).toString();

                            String callDuration = cursor.getString(duration);


                            Call_Details.setCall_number(phNumber);
                            Call_Details.setDuration(callDuration);
                            Call_Details.setCall_time_stamp(callDayTime);

                            cursor.close();
                        }
                    } catch (SecurityException e) {
                        Log.e("Security Exception", "User denied call log permission");

                    }
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putInt(LIMIT_PARAM_KEY,1);
                    cursor = activity.getContentResolver()
                            .query(contacts,null,
                                    bundle,
                                    null);

                    try{
                        if (cursor.moveToLast()) {
                            int number = cursor.getColumnIndex(CallLog.Calls.NUMBER);
                            int duration = cursor.getColumnIndex(CallLog.Calls.DURATION);
                            int date = cursor.getColumnIndex(CallLog.Calls.DATE);
                            int incomingtype = cursor.getColumnIndex(String.valueOf(CallLog.Calls.INCOMING_TYPE));
                            Log.d("last_call", "int duration " + duration);
                            Log.d("last_call", "int number " + number);
                            Log.d("last_call", "int date " + date);

                            String callType;
                            String phNumber = cursor.getString(number);
                            if (incomingtype == -1) {
                                callType = "incoming";
                            } else {
                                callType = "outgoing";
                            }
                            String callDate = cursor.getString(date);
                            String callDayTime = new Date(Long.valueOf(callDate)).toString();

                            String callDuration = cursor.getString(duration);


                            Call_Details.setCall_number(phNumber);
                            Call_Details.setDuration(callDuration);
                            Call_Details.setCall_time_stamp(callDayTime);

                            cursor.close();
                        }
                    } catch (SecurityException e) {
                        Log.e("Security Exception", "User denied call log permission");

                    }
                }





        }else{
            Log.d("last_call", "zero triggered + last dialled" + last_dialled_number);
            Log.d("last_call", "zero triggered + mobile 1"+ customer_class_gloabl.getMobile_1());
            Log.d("last_call", "zero triggered + mobile 2"+ customer_class_gloabl.getMobile_2());
            Call_Details.setCall_number("0");
            Call_Details.setDuration("0");
            Call_Details.setCall_time_stamp("0");
        }


    }

    public void search(String newtext) {
        BASIC_DATA_HOLDER.progress_bar();
        searchviewMap = new HashMap<>();
        search_view_key_holder_array = new ArrayList<>();
        if (!newtext.equals("") || !newtext.equals(null)) {
            this.filter_value = newtext;
            for (int i = 0; i < hashMap.size(); i++) {
                DataSnapshot snapshot = hashMap.get(String.valueOf(i));
                if(snapshot!=null){
                    Log.d("filter_position", "search: position " + i );
                    if(Objects.equals(snapshot.child("due_date").getValue(String.class), newtext)){
                        searchviewMap.put(key_holder.get(i), hashMap.get(key_holder.get(i)));
                        search_view_key_holder_array.add(key_holder.get(i));
                    }
                }

            }
            BASIC_DATA_HOLDER.loading_dialog.dismiss();
            notifyDataSetChanged();

        } else {
            searchviewMap.clear();
            search_view_key_holder_array.clear();
            BASIC_DATA_HOLDER.loading_dialog.dismiss();
            notifyDataSetChanged();
        }
    }
}

