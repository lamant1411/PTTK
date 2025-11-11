package com.shopman.model;
public class Customer extends Member {
    private Cart cart;
    public Customer() {
        super();
    }
    public Customer(String id, String name, String username, String password, String add, String phone, String role, Cart cart) {
        super(id, name, username, password, add, phone, role);
        this.cart = cart;
    }
    public Cart getCart() {
        return cart;
    }
    public void setCart(Cart cart) {
        this.cart = cart;
    }
}