package com.shopman.model;
public class Member {
    private String id;
    private String name;
    private String username;
    private String password;
    private String add;
    private String phone;
    private String role;
    public Member() {
    }
    public Member(String id, String name, String username, String password, String add, String phone, String role) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.password = password;
        this.add = add;
        this.phone = phone;
        this.role = role;
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
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
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
    public String getRole() {
        return role;
    }
    public void setRole(String role) {
        this.role = role;
    }
}