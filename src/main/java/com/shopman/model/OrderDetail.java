package com.shopman.model;

public class OrderDetail {
    private String id;
    private float price;
    private int quantity;
    private Order order;
    private Product product;
    
    public OrderDetail() {
    }
    
    public OrderDetail(String id, float price, int quantity, Order order, Product product) {
        this.id = id;
        this.price = price;
        this.quantity = quantity;
        this.order = order;
        this.product = product;
    }
    
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public float getPrice() {
        return price;
    }
    
    public void setPrice(float price) {
        this.price = price;
    }
    
    public int getQuantity() {
        return quantity;
    }
    
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    
    public Order getOrder() {
        return order;
    }
    
    public void setOrder(Order order) {
        this.order = order;
    }
    
    public Product getProduct() {
        return product;
    }
    
    public void setProduct(Product product) {
        this.product = product;
    }
}
