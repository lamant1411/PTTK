package com.shopman.model;
import java.util.Date;
public class ImportBill extends Bill {
    private Manager manager;
    private Supplier supplier;
    public ImportBill() {
        super();
    }
    public ImportBill(String id, float total, Date time, Date date, Manager manager, Supplier supplier) {
        super(id, total, time, date);
        this.manager = manager;
        this.supplier = supplier;
    }
    public Manager getManager() {
        return manager;
    }
    public void setManager(Manager manager) {
        this.manager = manager;
    }
    public Supplier getSupplier() {
        return supplier;
    }
    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }
}