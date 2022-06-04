package com.example.laxmi_tvs_workshop;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.concurrent.Callable;

public class Send_SMS implements Callable {
 
    String vehicle;
    String due_date;
    String party;
    String mobile;
    int choosen_language_code;
    public Send_SMS(String party,String vehicle, String due_date, String mobile, int choosen_language_code){
        this.party = party;
        this.vehicle = vehicle;
        this.due_date = due_date;
        this. mobile = mobile;
        this.choosen_language_code = choosen_language_code;

    }



    @Override
    public Object call() throws Exception {

        if(choosen_language_code == 2){
            try {
                // Construct data
                String message1 = ""+party+", ਤੁਸੀਂ ਆਪਣੀ ਗੱਡੀ "+vehicle+" ਦੀ ਸਰਵਿਸ ਮਿਸ ਕਰਤੀ ਹੈ | ਜਿਹੜੀ ਕਿ "+due_date+" ਤਾਰੀਖ ਨੂੰ ਹੋਣੀ ਚਾਹੀਦੀ ਸੀ | ਸਰਵਿਸ ਮਿਸ ਕਰਨ ਤੇ ਹੋ ਸਕਦਾ ਹੈ ਕਿ ਤੁਹਾਡੀ ਗੱਡੀ ਦੀ ਸਥਿਤੀ ਵਧੀਆ ਨਾ ਰਵੇ | ਕ੍ਰਿਪਾ ਕਰਕੇ ਇਸ ਨੂੰ ਜਲਦੀ ਤੋਂ ਜਲਦੀ ਸਰਵਿਸ ਸੈਂਟਰ ਲੈਕੇ ਜਾਓ |%0A%0Aਵਰਕਸ਼ਾਪ ਟਾਈਮ :-- ਸੋਮਵਾਰ ਤੋਂ ਲੈਕੇ ਸ਼ਨੀਵਾਰ ਦਾ ਟਾਈਮ ਸਵੇਰੇ 9:00 ਵਜੇ ਤੋਂ ਸ਼ਾਮ 6:00 ਵਜੇ ਤਕ ਤੇ ਐਤਵਾਰ ਸਵੇਰੇ 9:00 ਵਜੇ ਤੋਂ ਦੁਪਹਿਰ 3:00 ਵਜੇ ਤਕ | ਲਕਸ਼ਮੀ ਟੀਵੀ ਐਸ";

                /*""+party+", ਤੁਸੀਂ ਆਪਣੀ ਗੱਡੀ "+vehicle+" ਦੀ ਸਰਵਿਸ ਮਿਸ ਕਰਤੀ ਹੈ | ਜਿਹੜੀ ਕਿ "+due_date+" ਤਾਰੀਖ ਨੂੰ ਹੋਣੀ ਚਾਹੀਦੀ ਸੀ | ਸਰਵਿਸ ਮਿਸ ਕਰਨ ਤੇ ਹੋ ਸਕਦਾ ਹੈ ਕਿ ਤੁਹਾਡੀ ਗੱਡੀ ਦੀ ਸਥਿਤੀ ਵਧੀਆ ਨਾ ਰਵੇ | ਕ੍ਰਿਪਾ ਕਰਕੇ ਇਸ ਨੂੰ ਜਲਦੀ ਤੋਂ ਜਲਦੀ ਸਰਵਿਸ ਸੈਂਟਰ ਲੈਕੇ ਜਾਓ |%0Aਵਰਕਸ਼ਾਪ ਟਾਈਮ :-- ਸੋਮਵਾਰ ਤੋਂ ਲੈਕੇ ਸ਼ਨੀਵਾਰ ਦਾ ਟਾਈਮ ਸਵੇਰੇ 9:00 ਵਜੇ ਤੋਂ ਸ਼ਾਮ 6:00 ਵਜੇ ਤਕ ਤੇ ਐਤਵਾਰ ਸਵੇਰੇ 9:00 ਵਜੇ ਤੋਂ ਦੁਪਹਿਰ 3:00 ਵਜੇ ਤਕ | ਲਕਸ਼ਮੀ ਟੀਵੀ ਐਸ";*/
                String apiKey = "apikey=" + URLEncoder.encode("NTY2NDc2NGI3NzcyNjI3MzYyMzM0OTM2NmU0MjQxNDc=", "UTF-8");
                String message = "&message=" + URLEncoder.encode(message1, "UTF-8");
                String sender = "&sender=" + URLEncoder.encode("LAXTVS", "UTF-8");
                String numbers = "&numbers=" + URLEncoder.encode("91"+ mobile, "UTF-8");
                String unicode = "&unicode=" + URLEncoder.encode("1","UTF-8");
                // Send data
                String data = "https://api.textlocal.in/send/?" + apiKey + numbers + message + sender + unicode;
                URL url = new URL(data);
                URLConnection conn = url.openConnection();
                conn.setDoOutput(true);

                // Get the response
                BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String line;
                String sResult = "";
                while ((line = rd.readLine()) != null) {
                    // Process line...
                    sResult = sResult + line + " ";


                    Log.d("sms_sent", "sent successfully: " + sResult);
                }
                rd.close();


            } catch (Exception e) {
                System.out.println("Error SMS " + e);
                Log.d("sms_result", "error_sending_sms: " + e);

            }
            return null;
        }else{
            try {
                String mobile1 = "8872084210";
                String mobile2 = "8872084211";
                // Construct data
                String message1 = "Dear "+party+", Your vehicle "+vehicle+" has missed its service which was due on "+due_date+". Missing Service may impact performance and warranty. Kindly get your vehicle serviced at the earliest. Workshop Timing: Monday - Saturday : 9 am - 6 pm. Sunday : 9 am - 3 pm. Call : "+mobile1+", "+mobile2+". LAXMI TVS";

                /*""+party+", ਤੁਸੀਂ ਆਪਣੀ ਗੱਡੀ "+vehicle+" ਦੀ ਸਰਵਿਸ ਮਿਸ ਕਰਤੀ ਹੈ | ਜਿਹੜੀ ਕਿ "+due_date+" ਤਾਰੀਖ ਨੂੰ ਹੋਣੀ ਚਾਹੀਦੀ ਸੀ | ਸਰਵਿਸ ਮਿਸ ਕਰਨ ਤੇ ਹੋ ਸਕਦਾ ਹੈ ਕਿ ਤੁਹਾਡੀ ਗੱਡੀ ਦੀ ਸਥਿਤੀ ਵਧੀਆ ਨਾ ਰਵੇ | ਕ੍ਰਿਪਾ ਕਰਕੇ ਇਸ ਨੂੰ ਜਲਦੀ ਤੋਂ ਜਲਦੀ ਸਰਵਿਸ ਸੈਂਟਰ ਲੈਕੇ ਜਾਓ |%0Aਵਰਕਸ਼ਾਪ ਟਾਈਮ :-- ਸੋਮਵਾਰ ਤੋਂ ਲੈਕੇ ਸ਼ਨੀਵਾਰ ਦਾ ਟਾਈਮ ਸਵੇਰੇ 9:00 ਵਜੇ ਤੋਂ ਸ਼ਾਮ 6:00 ਵਜੇ ਤਕ ਤੇ ਐਤਵਾਰ ਸਵੇਰੇ 9:00 ਵਜੇ ਤੋਂ ਦੁਪਹਿਰ 3:00 ਵਜੇ ਤਕ | ਲਕਸ਼ਮੀ ਟੀਵੀ ਐਸ";*/
                String apiKey = "apikey=" + URLEncoder.encode("NTY2NDc2NGI3NzcyNjI3MzYyMzM0OTM2NmU0MjQxNDc=", "UTF-8");
                String message = "&message=" + URLEncoder.encode(message1, "UTF-8");
                String sender = "&sender=" + URLEncoder.encode("TVSLAX", "UTF-8");
                String numbers = "&numbers=" + URLEncoder.encode("91"+ mobile, "UTF-8");
                // Send data
                String data = "https://api.textlocal.in/send/?" + apiKey + numbers + message + sender;
                URL url = new URL(data);
                URLConnection conn = url.openConnection();
                conn.setDoOutput(true);

                // Get the response
                BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String line;
                String sResult = "";
                while ((line = rd.readLine()) != null) {
                    // Process line...
                    sResult = sResult + line + " ";


                    Log.d("sms_sent", "sent successfully: " + sResult);
                }
                rd.close();


            } catch (Exception e) {
                System.out.println("Error SMS " + e);
                Log.d("sms_result", "error_sending_sms: " + e);

            }
            return null;
        }

    }
}