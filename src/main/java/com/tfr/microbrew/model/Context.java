package com.tfr.microbrew.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.tfr.microbrew.model.serialize.LocalDateJsonDeserializer;
import com.tfr.microbrew.model.serialize.LocalDateJsonSerializer;
import org.joda.time.LocalDate;

import java.util.List;

/**
 *
 * Created by Erik on 5/8/2017.
 */
public class Context {

    @JsonDeserialize(using = LocalDateJsonDeserializer.class)
    @JsonSerialize(using = LocalDateJsonSerializer.class)
    private LocalDate date;

    private List<InventoryItem> inventory;
    private List<Batch> batches;
    private List<Sale> sales;
    private List<Cashflow> cashflows;

    public Context() {
        //for Jackson parsing
    }

    public Context(LocalDate date, List<InventoryItem> inventory, List<Batch> batches,
                   List<Sale> sales, List<Cashflow> cashflows) {
        this.date = date;
        this.inventory = inventory;
        this.batches = batches;
        this.sales = sales;
        this.cashflows = cashflows;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public List<InventoryItem> getInventory() {
        return inventory;
    }

    public void setInventory(List<InventoryItem> inventory) {
        this.inventory = inventory;
    }

    public List<Batch> getBatches() {
        return batches;
    }

    public void setBatches(List<Batch> batches) {
        this.batches = batches;
    }

    public List<Sale> getSales() {
        return sales;
    }

    public void setSales(List<Sale> sales) {
        this.sales = sales;
    }

    public List<Cashflow> getCashflows() {
        return cashflows;
    }

    public void setCashflows(List<Cashflow> cashflows) {
        this.cashflows = cashflows;
    }
}
