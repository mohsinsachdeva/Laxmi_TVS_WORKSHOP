package com.example.laxmi_tvs_workshop;

public class SMS_LIST {

    public SMS_LIST(){}
    String frame_no;
    String mobile_no;
    String model;
    String customer_name;
    String due_date;

    public String getFrame_no() {
        return frame_no;
    }

    public void setFrame_no(String frame_no) {
        this.frame_no = frame_no;
    }

    public String getMobile_no() {
        return mobile_no;
    }

    public void setMobile_no(String mobile_no) {
        this.mobile_no = mobile_no;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public String getDue_date() {
        return due_date;
    }

    public void setDue_date(String due_date) {
        this.due_date = due_date;
    }

    public SMS_LIST(String frame_no, String mobile_no, String model, String customer_name, String due_date){
        this.frame_no = frame_no;
        this.mobile_no = mobile_no;
        this.model = model;
        this.customer_name = customer_name;
        this.due_date = due_date;
    }
}
