package com.cdm.uas_pbo.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public abstract class FinancialRecord {

    private int id;
    private String description;
    private BigDecimal amount;
    private LocalDate date;
    private int userId;

    public FinancialRecord(int id, String description, BigDecimal amount, LocalDate date, int userId) {
        this.id = id;
        this.description = description;
        this.amount = amount;
        this.date = date;
        this.userId = userId;
    }

    public abstract BigDecimal getAmountForBalance();

    public abstract String getType();

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
