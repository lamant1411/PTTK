package com.shopman.model;
public class Shipper extends Member {
    private String name;
    private String phone;
    public Shipper() {
        super();
    }
    public Shipper(String id, String memberName, String username, String password, String add, String memberPhone, String role, String name, String phone) {
        super(id, memberName, username, password, add, memberPhone, role);
        this.name = name;
        this.phone = phone;
    }
    @Override
    public String getName() {
        return name;
    }
    @Override
    public void setName(String name) {
        this.name = name;
    }
    @Override
    public String getPhone() {
        return phone;
    }
    @Override
    public void setPhone(String phone) {
        this.phone = phone;
    }
}