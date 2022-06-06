package com.example.laxmi_tvs_workshop;

public class LOG_CLASS {

    public LOG_CLASS(){}

    String user;
    String frame_no;
    String message;
    String contact_date;
    String contact_type;
    String contact_number;

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getFrame_no() {
        return frame_no;
    }

    public void setFrame_no(String frame_no) {
        this.frame_no = frame_no;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getContact_date() {
        return contact_date;
    }

    public void setContact_date(String contact_date) {
        this.contact_date = contact_date;
    }

    public String getContact_type() {
        return contact_type;
    }

    public void setContact_type(String contact_type) {
        this.contact_type = contact_type;
    }

    public String getContact_number() {
        return contact_number;
    }

    public void setContact_number(String contact_number) {
        this.contact_number = contact_number;
    }

    public LOG_CLASS(String user, String frame_no, String message, String contact_date, String contact_type, String contact_number){
        this.user = user;
        this.frame_no = frame_no;
        this.message = message;
        this.contact_date = contact_date;
        this.contact_type = contact_type;
        this.contact_number = contact_number;
    }
}
