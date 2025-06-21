package com.cdm.uas_pbo.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Expense extends FinancialRecord {
    private Category category;

    public Expense(int id, String description, BigDecimal amount, LocalDate date, int userId, Category category) {
        super(id, description, amount, date, userId);
        this.category = category;
    }

    @Override
    public BigDecimal getAmountForBalance() {
        return getAmount().negate();
    }

    @Override
    public String getType() {
        return "Expense";
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getCategoryName() {
        return category != null ? category.getName() : "Uncategorized";
    }
}