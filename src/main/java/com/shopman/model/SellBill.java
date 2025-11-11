package com.shopman.model;

import java.util.Date;

public class SellBill extends Bill {
    private Order order;
    
    public SellBill() {
        super();
    }
    
    public SellBill(String id, float total, Date time, Date date, Order order) {
        super(id, total, time, date);
        this.order = order;
    }
    
    public Order getOrder() {
        return order;
    }
    
    public void setOrder(Order order) {
        this.order = order;
    }
}
