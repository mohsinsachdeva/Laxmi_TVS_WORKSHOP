package com.example.laxmi_tvs_workshop;

import android.app.Activity;
import android.app.Dialog;
import android.widget.TextView;

import pl.droidsonroids.gif.GifImageView;

public class BASIC_DATA_HOLDER {
    public static Dialog loading_dialog;

    public static boolean message_mode;

    public static boolean isMessage_mode() {
        return message_mode;
    }

    public static void setMessage_mode(boolean message_mode) {
        BASIC_DATA_HOLDER.message_mode = message_mode;
    }

    public BASIC_DATA_HOLDER(){

    }
    public static String calling_type;
    public static Activity activity;

    public static Activity getActivity() {
        return activity;
    }

    public static void setActivity(Activity activity) {
        BASIC_DATA_HOLDER.activity = activity;
    }

    public static String getCalling_type() {
        return calling_type;
    }

    public static void setCalling_type(String calling_type) {
        BASIC_DATA_HOLDER.calling_type = calling_type;

    }
    public static void progress_bar(){
        loading_dialog = new Dialog(activity);
        loading_dialog.setContentView(R.layout.progress_loading);
        TextView loading = (TextView)loading_dialog.findViewById(R.id.text_view_progressbar);
        loading.setText(R.string.loading);
        GifImageView progress_gif = (GifImageView)loading_dialog.findViewById(R.id.progress_gif);
        progress_gif.setImageResource(R.drawable.loading);
        loading_dialog.setCancelable(false);
        //   mDialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        loading_dialog.show();

    }
}
