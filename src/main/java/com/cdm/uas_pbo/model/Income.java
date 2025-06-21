package com.cdm.uas_pbo.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Income extends FinancialRecord {
    public Income(int id, String description, BigDecimal amount, LocalDate date, int userId) {
        super(id, description, amount, date, userId);
    }

    @Override
    public BigDecimal getAmountForBalance() {
        return getAmount();
    }

    @Override
    public String getType() {
        return "Income";
    }
}