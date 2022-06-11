package com.example.laxmi_tvs_workshop;

public class Remarks_Class {
    String date_of_transaction;
    String remark;
    public  Remarks_Class(){}
    public Remarks_Class (String date_of_transaction,String remark){
        this.date_of_transaction = date_of_transaction;
        this.remark =remark;
    }

    public String getDate_of_transaction() {
        return date_of_transaction;
    }

    public void setDate_of_transaction(String date_of_transaction) {
        this.date_of_transaction = date_of_transaction;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
