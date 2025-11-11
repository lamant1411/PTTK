package com.shopman.model;

public class ImportDetail {
    private String id;
    private int quantity;
    private ImportBill importBill;
    private Product product;
    
    public ImportDetail() {
    }
    
    public ImportDetail(String id, int quantity, ImportBill importBill, Product product) {
        this.id = id;
        this.quantity = quantity;
        this.importBill = importBill;
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
    
    public ImportBill getImportBill() {
        return importBill;
    }
    
    public void setImportBill(ImportBill importBill) {
        this.importBill = importBill;
    }
    
    public Product getProduct() {
        return product;
    }
    
    public void setProduct(Product product) {
        this.product = product;
    }
}
