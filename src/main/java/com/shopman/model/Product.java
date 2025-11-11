package com.shopman.model;
public class Product {
    private String id;
    private String name;
    private float price;
    private int quantity;
    private String des;
    private String unit;
    public Product() {
    }
    public Product(String id, String name, float price, int quantity, String des) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.des = des;
    }
    public Product(String id, String name, float price, int quantity, String des, String unit) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.des = des;
        this.unit = unit;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
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
    public String getDes() {
        return des;
    }
    public void setDes(String des) {
        this.des = des;
    }
    public String getUnit() {
        return unit;
    }
    public void setUnit(String unit) {
        this.unit = unit;
    }
}