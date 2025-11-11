package com.shopman.model;

import java.util.Date;

public class Cart {
    private String id;
    private Date time;
    private Date date;
    
    public Cart() {
    }
    
    public Cart(String id, Date time, Date date) {
        this.id = id;
        this.time = time;
        this.date = date;
    }
    
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
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
