package com.example.laxmi_tvs_workshop;

import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.concurrent.Callable;

public class Send_WATSAPP implements Callable {

    String vehicle;
    String due_date;
    String party;
    String mobile;
    int choosen_due_message_type;
    int choosen_language_code;
    public Send_WATSAPP(String party, String vehicle, String due_date, String mobile, int choosen_language_code, int choosen_due_message_type){
        this.party = party;
        this.vehicle = vehicle;
        this.due_date = due_date;
        this. mobile = mobile;
        this.choosen_language_code = choosen_language_code;
        this.choosen_due_message_type = choosen_due_message_type;
    }



    @Override
    public Object call() throws Exception {

        if(choosen_language_code == 1){
            try {
                String break_line = "%0A";
                // Construct data
                String message1;
                if(choosen_due_message_type == 1){
                    message1 = "Dear "+party+","+break_line+"Your "+vehicle+" is due for service. *The due date is "+due_date+".*"+break_line+"Timely service of your Two-Wheeler *will give you good performance and great average*. Kindly bring your vehicle to the workshop soon." + break_line + "Workshop Timming:"+break_line+
                            "Monday - Friday : 9am - 5:30pm"+break_line+"Sunday : 9am - 3pm"+break_line+"Laxmi TVS"+break_line+"Call : 8872084211, 8872084210";
                }else{
                    message1 = "Dear "+party+","+break_line+"*Your "+vehicle+" has missed its service*. The due date was "+due_date+"."+break_line+"*Missing service may impact vehicle performance and your warranty claim*. Kindly bring your vehicle to the workshop soon." + break_line + "Workshop Timming:"+break_line+
                            "Monday - Friday : 9am - 5:30pm"+break_line+"Sunday : 9am - 3pm"+break_line+"Laxmi TVS"+break_line+"Call : 8872084211, 8872084210";
                }


                String mainMessage="";

                for ( String str : message1.split(break_line)){

                    mainMessage = mainMessage + str + "\n";

                    Log.d("string_test", "call: "+mainMessage);;
                }


                String apiKey = "apikey=" + URLEncoder.encode("2a30cc6dee744c59a0cefe6a36541bfa", "UTF-8");
                String message = "&msg=" + URLEncoder.encode(mainMessage, "UTF-8");
                String numbers = "&mobile=" + URLEncoder.encode( mobile, "UTF-8");
                // Send data
                String data = "http://148.251.129.118/wapp/api/send?" + apiKey + numbers + message;
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
        }else if(choosen_language_code == 2){
            try {
                // Construct data
                String break_line = "%0A";
                String message1 = "Dear "+party+","+break_line+" ਤੁਸੀਂ ਆਪਣੀ ਗੱਡੀ "+vehicle+" ਦੀ ਸਰਵਿਸ ਮਿਸ ਕਰਤੀ ਹੈ | "+break_line+" ਜਿਹੜੀ ਕਿ "+due_date+" ਤਾਰੀਖ ਨੂੰ ਹੋਣੀ ਚਾਹੀਦੀ ਸੀ | ਸਰਵਿਸ ਮਿਸ ਕਰਨ ਤੇ ਹੋ ਸਕਦਾ ਹੈ ਕਿ ਤੁਹਾਡੀ ਗੱਡੀ ਦੀ ਸਥਿਤੀ ਵਧੀਆ ਨਾ ਰਵੇ | ਕ੍ਰਿਪਾ ਕਰਕੇ ਇਸ ਨੂੰ ਜਲਦੀ ਤੋਂ ਜਲਦੀ ਸਰਵਿਸ ਸੈਂਟਰ ਲੈਕੇ ਜਾਓ |"+break_line+"Aਵਰਕਸ਼ਾਪ ਟਾਈਮ :-- ਸੋਮਵਾਰ ਤੋਂ ਲੈਕੇ ਸ਼ਨੀਵਾਰ ਦਾ ਟਾਈਮ ਸਵੇਰੇ 9:00 ਵਜੇ ਤੋਂ ਸ਼ਾਮ 6:00 ਵਜੇ ਤਕ ਤੇ ਐਤਵਾਰ ਸਵੇਰੇ 9:00 ਵਜੇ ਤੋਂ ਦੁਪਹਿਰ 3:00 ਵਜੇ ਤਕ | ਲਕਸ਼ਮੀ ਟੀਵੀ ਐਸ";


                String mainMessage="";

               for ( String str : message1.split("%0A")){

                   mainMessage = mainMessage + str + "\n";

                   System.out.println(str);
                }
                Log.d("string_test", "call: "+ mainMessage);



                String apiKey = "apikey=" + URLEncoder.encode("2a30cc6dee744c59a0cefe6a36541bfa", "UTF-8");
                String message = "&msg=" + URLEncoder.encode(mainMessage, "UTF-8");
                String numbers = "&mobile=" + URLEncoder.encode( mobile, "UTF-8");
                // Send data
               String data = "http://148.251.129.118/wapp/api/send?" + apiKey + numbers + message;
             //   String data  = "http://148.251.129.118/wapp/api/send?apikey=2a30cc6dee744c59a0cefe6a36541bfa&mobile=9815520300&msg=mohsin you have done it 300";
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
        }else {

            try {
                // Construct data
                String break_line = "%0A";
                String message1 = "Dear "+party+","+break_line+" ਤੁਸੀਂ ਆਪਣੀ ਗੱਡੀ "+vehicle+" ਦੀ ਸਰਵਿਸ ਮਿਸ ਕਰਤੀ ਹੈ | "+break_line+" ਜਿਹੜੀ ਕਿ "+due_date+" ਤਾਰੀਖ ਨੂੰ ਹੋਣੀ ਚਾਹੀਦੀ ਸੀ | ਸਰਵਿਸ ਮਿਸ ਕਰਨ ਤੇ ਹੋ ਸਕਦਾ ਹੈ ਕਿ ਤੁਹਾਡੀ ਗੱਡੀ ਦੀ ਸਥਿਤੀ ਵਧੀਆ ਨਾ ਰਵੇ | ਕ੍ਰਿਪਾ ਕਰਕੇ ਇਸ ਨੂੰ ਜਲਦੀ ਤੋਂ ਜਲਦੀ ਸਰਵਿਸ ਸੈਂਟਰ ਲੈਕੇ ਜਾਓ |"+break_line+"Aਵਰਕਸ਼ਾਪ ਟਾਈਮ :-- ਸੋਮਵਾਰ ਤੋਂ ਲੈਕੇ ਸ਼ਨੀਵਾਰ ਦਾ ਟਾਈਮ ਸਵੇਰੇ 9:00 ਵਜੇ ਤੋਂ ਸ਼ਾਮ 6:00 ਵਜੇ ਤਕ ਤੇ ਐਤਵਾਰ ਸਵੇਰੇ 9:00 ਵਜੇ ਤੋਂ ਦੁਪਹਿਰ 3:00 ਵਜੇ ਤਕ | ਲਕਸ਼ਮੀ ਟੀਵੀ ਐਸ";


                String mainMessage="";

                for ( String str : message1.split("%0A")){

                    mainMessage = mainMessage + str + "\n";

                    System.out.println(str);
                }
                Log.d("string_test", "call: "+ mainMessage);



                String apiKey = "apikey=" + URLEncoder.encode("2a30cc6dee744c59a0cefe6a36541bfa", "UTF-8");
                String message = "&msg=" + URLEncoder.encode(mainMessage, "UTF-8");
                String numbers = "&mobile=" + URLEncoder.encode( mobile, "UTF-8");
                // Send data
                String data = "http://148.251.129.118/wapp/api/send?" + apiKey + numbers + message;
                //   String data  = "http://148.251.129.118/wapp/api/send?apikey=2a30cc6dee744c59a0cefe6a36541bfa&mobile=9815520300&msg=mohsin you have done it 300";
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

        }
        return  null;
    }
}