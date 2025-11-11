package com.shopman.model;
public class Supplier {
    private String id;
    private String name;
    private String add;
    private String phone;
    public Supplier() {
    }
    public Supplier(String id, String name, String add, String phone) {
        this.id = id;
        this.name = name;
        this.add = add;
        this.phone = phone;
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
    public String getAdd() {
        return add;
    }
    public void setAdd(String add) {
        this.add = add;
    }
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
}