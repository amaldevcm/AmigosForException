package com.example.amigosforexception;

import java.util.ArrayList;
import java.util.List;

public class Global {
    String customerName, customerMobile;
    List<CartItems> dataList;

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerMobile() {
        return customerMobile;
    }

    public void setCustomerMobile(String customerMobile) {
        this.customerMobile = customerMobile;
    }

    public List<CartItems> getDataList() {
        return dataList;
    }

    public void setDataList(List<CartItems> dataList) {
        this.dataList = dataList;
    }

    public Global() {
        customerName = "";
        customerMobile = "";
        dataList = new ArrayList<>();
    }

    private static final Global global = new Global();
    public static Global getInstance() {return global;}
}
