package com.shopman.model;
public class CartDetail {
    private String id;
    private int quantity;
    private Product product;
    public CartDetail() {
    }
    public CartDetail(String id, int quantity, Product product) {
        this.id = id;
        this.quantity = quantity;
        this.product = product;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public int getQuantity() {
        return quantity;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    public Product getProduct() {
        return product;
    }
    public void setProduct(Product product) {
        this.product = product;
    }
}