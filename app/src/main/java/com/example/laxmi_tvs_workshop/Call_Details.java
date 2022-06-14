package com.example.laxmi_tvs_workshop;

public class Call_Details {
    public static String call_number;
    public static String duration;

    public static String getCall_number() {
        return call_number;
    }

    public static void setCall_number(String call_number) {
        Call_Details.call_number = call_number;
    }

    public static String getDuration() {
        return duration;
    }

    public static void setDuration(String duration) {
        Call_Details.duration = duration;
    }

    public static String getCall_time_stamp() {
        return call_time_stamp;
    }

    public static void setCall_time_stamp(String call_time_stamp) {
        Call_Details.call_time_stamp = call_time_stamp;
    }

    public static String call_time_stamp;
    public Call_Details(){}


}
