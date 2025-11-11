package com.shopman.model;

import java.util.Date;

public class Bill {
    private String id;
    private float total;
    private Date time;
    private Date date;
    
    public Bill() {
    }
    
    public Bill(String id, float total, Date time, Date date) {
        this.id = id;
        this.total = total;
        this.time = time;
        this.date = date;
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
    
    public Date getTime() {
        return time;
    }
    
    public void setTime(Date time) {
        this.time = time;
    }
    
    public Date getDate() {
        return date;
    }
    
    public void setDate(Date date) {
        this.date = date;
    }
}
