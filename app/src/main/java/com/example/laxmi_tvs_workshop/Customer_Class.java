package com.example.laxmi_tvs_workshop;

public class Customer_Class {

    public Customer_Class(){}

    String customer_name;
    String model;
    String due_date;
    String frame_no;
    String address_line_2;
    String address_line_3;
    Long mobile_1;
    Long mobile_2;
    String recycle_date;
    String service_type;
    String registration;
    String appointment_date_time;

    public String getAppointment_date_time() {
        return appointment_date_time;
    }

    public void setAppointment_date_time(String appointment_date_time) {
        this.appointment_date_time = appointment_date_time;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getDue_date() {
        return due_date;
    }

    public void setDue_date(String due_date) {
        this.due_date = due_date;
    }

    public String getFrame_no() {
        return frame_no;
    }

    public void setFrame_no(String frame_no) {
        this.frame_no = frame_no;
    }

    public String getAddress_line_2() {
        return address_line_2;
    }

    public void setAddress_line_2(String address_line_2) {
        this.address_line_2 = address_line_2;
    }

    public String getAddress_line_3() {
        return address_line_3;
    }

    public void setAddress_line_3(String address_line_3) {
        this.address_line_3 = address_line_3;
    }

    public Long getMobile_1() {
        return mobile_1;
    }

    public void setMobile_1(Long mobile_1) {
        this.mobile_1 = mobile_1;
    }

    public Long getMobile_2() {
        return mobile_2;
    }

    public void setMobile_2(Long mobile_2) {
        this.mobile_2 = mobile_2;
    }

    public Customer_Class(String customer_name, String model, String due_date, String frame_no,
                          String address_line_2, String address_line_3, Long mobile_1, Long mobile_2, String registration){
        this.customer_name = customer_name;
        this.model = model;
        this.due_date = due_date;
        this.frame_no = frame_no;
        this.address_line_2 = address_line_2;
        this.address_line_3 = address_line_3;
        this.mobile_1 = mobile_1;
        this.mobile_2 = mobile_2;
        this.registration = registration;
    }

    public String getRecycle_date() {
        return recycle_date;
    }

    public void setRecycle_date(String recycle_date) {
        this.recycle_date = recycle_date;
    }

    public String getService_type() {
        return service_type;
    }

    public void setService_type(String service_type) {
        this.service_type = service_type;
    }

    public String getRegistration() {
        return registration;
    }

    public void setRegistration(String registration) {
        this.registration = registration;
    }

    public Customer_Class(String customer_name, String model, String due_date, String frame_no,
                          String address_line_2, String address_line_3, Long mobile_1, Long mobile_2, String recycle_date, String service_type, String registration){
        this.customer_name = customer_name;
        this.model = model;
        this.due_date = due_date;
        this.frame_no = frame_no;
        this.address_line_2 = address_line_2;
        this.address_line_3 = address_line_3;
        this.mobile_1 = mobile_1;
        this.mobile_2 = mobile_2;
        this.recycle_date = recycle_date;
        this.service_type = service_type;
        this.registration = registration;
    }


}
