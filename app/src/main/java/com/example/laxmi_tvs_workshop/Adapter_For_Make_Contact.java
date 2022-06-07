package com.example.laxmi_tvs_workshop;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;


public class Adapter_For_Make_Contact extends RecyclerView.Adapter<Adapter_For_Make_Contact.Holder> {
    Activity activity;
    DatabaseReference databaseReference;
    public static HashMap<String, DataSnapshot> hashMap;
    public static ArrayList<String> key_holder;
    Calendar calendar;
    String last_message_date = "";
    String frame_no;

    public Adapter_For_Make_Contact(Activity activity, DatabaseReference databaseReference) {
        this.activity = activity;
        this.databaseReference = databaseReference;
        hashMap = new HashMap<>();
        calendar = Calendar.getInstance();
        key_holder = new ArrayList<>();
        counter_check ();

        Log.d("testing", "Adapter_For_Make_Contact: CALLED");
        databaseReference.child("Calling").addChildEventListener(childEventListener);

    }


    @Override
    public Adapter_For_Make_Contact.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_make_contact, parent, false);
        Holder holder = new Holder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter_For_Make_Contact.Holder holder, int position) {
        DataSnapshot snapshot = hashMap.get(key_holder.get(position));
        Log.d("view_error", "onBindViewHolder: "+ position);
        assert snapshot != null;
        Customer_Class customer_class = snapshot.getValue(Customer_Class.class);
        assert customer_class != null;
        holder.customer_name_value.setText(customer_class.getCustomer_name());
        holder.customer_vehicle.setText(customer_class.getModel());
        holder.service_due_value.setText(customer_class.getDue_date());
        holder.frame_number_value.setText(customer_class.getFrame_no());
        holder.address_line_one.setText(customer_class.address_line_2);
        holder.address_line_two.setText(customer_class.getAddress_line_3());
        if(customer_class.getMobile_1()!=null && !customer_class.getMobile_1().equals("")){
            holder.mobile_one_value.setText(String.valueOf(customer_class.mobile_1));
        }
        if(customer_class.getMobile_1() == 0){
            holder.mobile_one_value.setText("");
        }
       if(customer_class.getMobile_2()!=null){
           holder.mobile_two_value.setText(String.valueOf(customer_class.mobile_2));
       }
       if(customer_class.getMobile_2() == 0){
           holder.mobile_two_value.setText("");
       }

        holder.english_check_box.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Make_Contact.sending_message = true;
                long todays_date = calendar.getTimeInMillis();
                calendar.set(Calendar.HOUR,0);
                calendar.set(Calendar.MILLISECOND,0);
                calendar.set(Calendar.SECOND,0);
                calendar.set(Calendar.MINUTE,0);




                if (holder.mobile_selected.isChecked() && customer_class.getMobile_1() != null) {
                    try {
                        String break_line = "%0A";
                        String mobile = String.valueOf(customer_class.getMobile_1());
                        String msg;
                        if(service_status(customer_class.getDue_date())){
                            msg = "Dear " + customer_class.getCustomer_name() + " Ji," + break_line + "Your " + customer_class.getModel() + " is due for service. *The due date is " + customer_class.getDue_date() + ".*" + break_line + "Timely service of your Two-Wheeler *will give you good performance and great average*. Kindly bring your vehicle to the workshop soon." + break_line + "*Workshop Timming:*" + break_line +
                                    "Monday - Friday : 9am - 6:00pm" + break_line + "Sunday : 9am - 3pm" + break_line + "Laxmi TVS" + break_line + "Call : 8872084211, 8872084210"+break_line+""+break_line+"±±±±±±±±±±±±±±±±±±±±±±±±±±±±±±"+break_line+""+break_line+customer_class.getCustomer_name() + " जी," + break_line + "आपके दुपहिया वाहन " + customer_class.getModel() + " की सर्विस का टाइम आ गया है| *हमारे रिकॉर्ड के अनुसार सर्विस " + customer_class.getDue_date() + " किया जाना चाहिए|*" + break_line + "टाइम से सर्विस करवाने का फ़ायदा यह है की आपको *बेहतरीन एवरेज और परफॉरमेंस* मिलती है| कृपया वर्कशॉप में जल्दी आए|"+ break_line +"*वर्कशॉप का समय:*"+ break_line + "सोमवार - शनिवार: सुबह 9 बजे से - शाम 6 बजे तक।"+ break_line +"रविवार: सुबह 9 बजे से - दोपहर 3 बजे तक।"+ break_line + "लक्ष्मी टीवीएस" +break_line+ "कृपया कॉल करें : 8872084210, 8872084211";

                        }else{
                            msg = "Dear " + customer_class.getCustomer_name() + " Ji," + break_line + "Your " + customer_class.getModel() + " has missed its service. *The due date was " + customer_class.getDue_date() + ".*" + break_line + "Without service *vehilce performance and average may not be best.* Kindly bring your vehicle to the workshop soon." + break_line + "*Workshop Timming:*" + break_line +
                                    "Monday - Friday : 9am - 6:00pm" + break_line + "Sunday : 9am - 3pm" + break_line + "Laxmi TVS" + break_line + "Call : 8872084211, 8872084210" +break_line+""+break_line+"±±±±±±±±±±±±±±±±±±±±±±±±±±±±±±"+break_line+""+break_line+customer_class.getCustomer_name() + " जी," + break_line + "आपके दुपहिया वाहन " + customer_class.getModel() + " की सर्विस का टाइम चला गया गया है| *हमारे रिकॉर्ड के अनुसार सर्विस " + customer_class.getDue_date() + " को किया जाना चाहिए था|*"+ break_line + "टाइम से सर्विस करवाने का फ़ायदा यह है की *आपको बेहतरीन एवरेज और परफॉरमेंस* मिलती है| कृपया वर्कशॉप में जल्दी आए|"+ break_line +"*वर्कशॉप का समय:*"+ break_line + "सोमवार - शनिवार: सुबह 9 बजे से - शाम 6 बजे तक।"+ break_line +"रविवार: सुबह 9 बजे से - दोपहर 3 बजे तक।"+ break_line + "लक्ष्मी टीवीएस" +break_line+ "कृपया कॉल करें : 8872084210, 8872084211";
                        }




                        String mainMessage = "";

                        for (String str : msg.split(break_line)) {

                            mainMessage = mainMessage + str + "\n";

                            Log.d("string_test", "call: " + mainMessage);
                            ;
                        }
                        String user = "";
                        String frame_no = customer_class.getFrame_no();
                        String message = mainMessage ;

                        String contact_date =  String.valueOf(todays_date);
                        String contact_type = "WATSAPP";
                        String contacted_number;
                        if(holder.mobile_selected.isChecked()){
                            contacted_number = String.valueOf(customer_class.getMobile_1());

                        }else{
                            contacted_number = String.valueOf(customer_class.getMobile_2());
                        }
                        LOG_CLASS log_class = new LOG_CLASS("",frame_no,message,contact_date,contact_type,contacted_number);
                        databaseReference.child("LOGS").child(frame_no).push().setValue(log_class);

                        activity.startActivityForResult(new Intent(Intent.ACTION_VIEW, Uri.parse("https://api.whatsapp.com/send?phone=91" + mobile + "&text=" + mainMessage)),Make_Contact.REQUEST_CODE);


                        String key = snapshot.getKey();


                        update_counter();
                    } catch (Exception e) {
                        //whatsapp app not install
                    }
                } else if (holder.phone_selected.isChecked() && customer_class.getMobile_2() != null) {
                    try {
                        String break_line = "%0A";
                        String mobile = String.valueOf(customer_class.getMobile_2());
                        String msg;
                        if(service_status(customer_class.getDue_date())){
                            msg = "Dear " + customer_class.getCustomer_name() + " Ji," + break_line + "Your " + customer_class.getModel() + " is due for service. *The due date is " + customer_class.getDue_date() + ".*" + break_line + "Timely service of your Two-Wheeler *will give you good performance and great average*. Kindly bring your vehicle to the workshop soon." + break_line + "*Workshop Timming:*" + break_line +
                                    "Monday - Friday : 9am - 6:00pm" + break_line + "Sunday : 9am - 3pm" + break_line + "Laxmi TVS" + break_line + "Call : 8872084211, 8872084210"+break_line+""+break_line+"±±±±±±±±±±±±±±±±±±±±±±±±±±±±±±"+break_line+""+break_line+customer_class.getCustomer_name() + " जी," + break_line + "आपके दुपहिया वाहन " + customer_class.getModel() + " की सर्विस का टाइम आ गया है| *हमारे रिकॉर्ड के अनुसार सर्विस " + customer_class.getDue_date() + " किया जाना चाहिए|*" + break_line + "टाइम से सर्विस करवाने का फ़ायदा यह है की आपको *बेहतरीन एवरेज और परफॉरमेंस* मिलती है| कृपया वर्कशॉप में जल्दी आए|"+ break_line +"*वर्कशॉप का समय:*"+ break_line + "सोमवार - शनिवार: सुबह 9 बजे से - शाम 6 बजे तक।"+ break_line +"रविवार: सुबह 9 बजे से - दोपहर 3 बजे तक।"+ break_line + "लक्ष्मी टीवीएस" +break_line+ "कृपया कॉल करें : 8872084210, 8872084211";

                        }else{
                            msg = "Dear " + customer_class.getCustomer_name() + " Ji," + break_line + "Your " + customer_class.getModel() + " has missed its service. *The due date was " + customer_class.getDue_date() + ".*" + break_line + "Without service *vehilce performance and average may not be best.* Kindly bring your vehicle to the workshop soon." + break_line + "*Workshop Timming:*" + break_line +
                                    "Monday - Friday : 9am - 6:00pm" + break_line + "Sunday : 9am - 3pm" + break_line + "Laxmi TVS" + break_line + "Call : 8872084211, 8872084210" +break_line+""+break_line+"±±±±±±±±±±±±±±±±±±±±±±±±±±±±±±"+break_line+""+break_line+customer_class.getCustomer_name() + " जी," + break_line + "आपके दुपहिया वाहन " + customer_class.getModel() + " की सर्विस का टाइम चला गया गया है| *हमारे रिकॉर्ड के अनुसार सर्विस " + customer_class.getDue_date() + " को किया जाना चाहिए था|*"+ break_line + "टाइम से सर्विस करवाने का फ़ायदा यह है की *आपको बेहतरीन एवरेज और परफॉरमेंस* मिलती है| कृपया वर्कशॉप में जल्दी आए|"+ break_line +"*वर्कशॉप का समय:*"+ break_line + "सोमवार - शनिवार: सुबह 9 बजे से - शाम 6 बजे तक।"+ break_line +"रविवार: सुबह 9 बजे से - दोपहर 3 बजे तक।"+ break_line + "लक्ष्मी टीवीएस" +break_line+ "कृपया कॉल करें : 8872084210, 8872084211";
                        }




                        String mainMessage = "";

                        for (String str : msg.split(break_line)) {

                            mainMessage = mainMessage + str + "\n";

                            Log.d("string_test", "call: " + mainMessage);
                            ;
                        }
                        String user = "";
                        String frame_no = customer_class.getFrame_no();
                        String message = mainMessage ;

                        String contact_date =  String.valueOf(todays_date);
                        String contact_type = "WATSAPP";
                        String contacted_number;
                        if(holder.mobile_selected.isChecked()){
                            contacted_number = String.valueOf(customer_class.getMobile_1());

                        }else{
                            contacted_number = String.valueOf(customer_class.getMobile_2());
                        }


                        LOG_CLASS log_class = new LOG_CLASS("",frame_no,message,contact_date,contact_type,contacted_number);
                        databaseReference.child("LOGS").child(frame_no).push().setValue(log_class);

                        activity.startActivityForResult(new Intent(Intent.ACTION_VIEW, Uri.parse("https://api.whatsapp.com/send?phone=91" + mobile + "&text=" + mainMessage)),Make_Contact.REQUEST_CODE);
                        String key = snapshot.getKey();


                        update_counter();
                    } catch (Exception e) {
                        //whatsapp app not install
                    }
                }
            }
        });
        holder.mobile_selected.setChecked(true);
        holder.phone_selected.setChecked(false);

        holder.mobile_selected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(holder.mobile_selected.isChecked()){
                    holder.phone_selected.setChecked(false);
                }else{
                    holder.phone_selected.setChecked(true);
                }
            }
        });

        holder.phone_selected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(holder.phone_selected.isChecked()){
                    holder.mobile_selected.setChecked(false);
                }else{
                    holder.mobile_selected.setChecked(true);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return hashMap.size();
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
        ImageButton english_check_box;
        ImageView call_button;
        CheckBox mobile_selected;
        CheckBox phone_selected;


        public Holder(@NonNull View itemView) {
            super(itemView);

            customer_name_value = (TextView) itemView.findViewById(R.id.customer_name_value);
            customer_vehicle = (TextView) itemView.findViewById(R.id.customer_vehicle);
            service_due_value = (TextView) itemView.findViewById(R.id.service_due_value);
            frame_number_value = (TextView) itemView.findViewById(R.id.frame_number_value);
            address_line_one = (TextView) itemView.findViewById(R.id.address_line_one);
            address_line_two = (TextView) itemView.findViewById(R.id.address_line_two);
            mobile_one_value = (TextView) itemView.findViewById(R.id.mobile_one_value);
            mobile_two_value = (TextView) itemView.findViewById(R.id.mobile_two_value);
            english_check_box = (ImageButton) itemView.findViewById(R.id.english_check_box);
            call_button = (ImageView) itemView.findViewById(R.id.call_button);
            mobile_selected = (CheckBox) itemView.findViewById(R.id.mobile_selected);
            phone_selected = (CheckBox) itemView.findViewById(R.id.phone_selected);

        }
    }


    ChildEventListener childEventListener = new ChildEventListener() {
        @Override
        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
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
        databaseReference.child("Calling").removeEventListener(childEventListener);
        hashMap.clear();
    }

    public boolean service_status(String due_date_as_received) {

        String string_date = due_date_as_received;
        Log.d("due_date_status", "service_status: "+ due_date_as_received);
        SimpleDateFormat f = new SimpleDateFormat("dd-MMM-yy");
        try {
            Date d = f.parse(string_date);
            long milliseconds = d.getTime();
            long todays_time = Calendar.getInstance().getTimeInMillis();
            Log.d("due_date_status", "due_date: "+ milliseconds);
            Log.d("due_date_status", "todays date: "+ milliseconds);
            if(milliseconds > todays_time){
                return  true;
            }else{
                return  false;
            }

        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }

    }


public void counter_check (){
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR,0);
        cal.set(Calendar.MINUTE,0);
        cal.set(Calendar.SECOND,0);
        cal.set(Calendar.MILLISECOND,0);
        long today = cal.getTimeInMillis();
    Log.d("counter", "Todays date at start: "+  today);

        String string_today = String.valueOf(today);
        databaseReference.child("Counter").child(string_today).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    Make_Contact.counter_text_box.setText(snapshot.getValue(String.class));
                }else{
                    databaseReference.child("Counter").child(string_today).setValue("0");
                    Make_Contact.counter_text_box.setText("0");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Make_Contact.counter_text_box.setText("0");
            }
        });
}
    public void update_counter(){
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR,0);
        cal.set(Calendar.MINUTE,0);
        cal.set(Calendar.SECOND,0);
        cal.set(Calendar.MILLISECOND,0);
        long today = cal.getTimeInMillis();
        Log.d("counter", "On Update: "+  today);
        String string_today = String.valueOf(today);
        int current = Integer.parseInt(Make_Contact.counter_text_box.getText().toString());
        int new_counter = current + 1;
        databaseReference.child("Counter").child(string_today).setValue(String.valueOf(new_counter));
    }
}
