package com.tfr.microbrew.model;

/**
 *
 * Created by Erik on 4/22/2017.
 */
public class InventoryItem {

    private String name;
    private double quantity;
    private double reorderThreshold;
    private double reorderQuantity;

    public InventoryItem(String name, double quantity, double reorderThreshold, double reorderQuantity) {
        this.name = name;
        this.quantity = quantity;
        this.reorderThreshold = reorderThreshold;
        this.reorderQuantity = reorderQuantity;
    }

    public InventoryItem(String name, double reorderThreshold, double reorderQuantity) {
        this(name, 0.0, reorderThreshold, reorderQuantity);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        InventoryItem that = (InventoryItem) o;

        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public String toString() {
        return "InventoryItem{" +
                "name='" + name + '\'' +
                ", quantity=" + quantity +
                ", reorderThreshold=" + reorderThreshold +
                ", reorderQuantity=" + reorderQuantity +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public double getReorderThreshold() {
        return reorderThreshold;
    }

    public void setReorderThreshold(double reorderThreshold) {
        this.reorderThreshold = reorderThreshold;
    }

    public double getReorderQuantity() {
        return reorderQuantity;
    }

    public void setReorderQuantity(double reorderQuantity) {
        this.reorderQuantity = reorderQuantity;
    }
}