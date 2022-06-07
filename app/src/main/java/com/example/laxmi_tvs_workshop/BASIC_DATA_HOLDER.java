package com.example.laxmi_tvs_workshop;

import android.app.Activity;

public class BASIC_DATA_HOLDER {

    public BASIC_DATA_HOLDER(){}
    public static String calling_type;

    public static String getCalling_type() {
        return calling_type;
    }

    public static void setCalling_type(String calling_type) {
        BASIC_DATA_HOLDER.calling_type = calling_type;
    }

}
