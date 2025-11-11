package com.shopman.model;

import java.sql.Date;
import java.sql.Time;

public class Order {
    private String id;
    private float total;
    private Time time;
    private Date date;
    private String status;
    private Seller seller;
    private Customer customer;
    private Shipper shipper;
    
    public Order() {
    }
    
    public Order(String id, float total, Time time, Date date, Seller seller, Customer customer, Shipper shipper) {
        this.id = id;
        this.total = total;
        this.time = time;
        this.date = date;
        this.seller = seller;
        this.customer = customer;
        this.shipper = shipper;
    }
    
    public Order(String id, float total, Time time, Date date, String status, Seller seller, Customer customer, Shipper shipper) {
        this.id = id;
        this.total = total;
        this.time = time;
        this.date = date;
        this.status = status;
        this.seller = seller;
        this.customer = customer;
        this.shipper = shipper;
    }
    
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public float getTotal() {
        return total;
    }
    
    public void setTotal(float total) {
        this.total = total;
    }
    
    public Time getTime() {
        return time;
    }
    
    public void setTime(Time time) {
        this.time = time;
    }
    
    public Date getDate() {
        return date;
    }
    
    public void setDate(Date date) {
        this.date = date;
    }
    
    public Seller getSeller() {
        return seller;
    }
    
    public void setSeller(Seller seller) {
        this.seller = seller;
    }
    
    public Customer getCustomer() {
        return customer;
    }
    
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
    
    public Shipper getShipper() {
        return shipper;
    }
    
    public void setShipper(Shipper shipper) {
        this.shipper = shipper;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
}
