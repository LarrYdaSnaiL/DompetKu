package com.cdm.uas_pbo.controller;

import com.cdm.uas_pbo.model.*;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;

import java.math.BigDecimal;
import java.util.stream.Collectors;

public class DashboardTabController {
    @FXML
    private Label totalIncomeLabel;
    @FXML
    private Label totalExpenseLabel;
    @FXML
    private Label balanceLabel;
    @FXML
    private PieChart expensePieChart;

    private ObservableList<FinancialRecord> transactionData;

    public void initData(User user, ObservableList<FinancialRecord> transactionData, ObservableList<Category> categoryData) {
        this.transactionData = transactionData;
        this.transactionData.addListener((ListChangeListener<FinancialRecord>) c -> updateUI());

        updateUI();
    }

    private void updateUI() {
        updateSummary();
        updateExpensePieChart();
    }

    private void updateSummary() {
        BigDecimal income = transactionData.stream()
                .filter(r -> r instanceof Income)
                .map(FinancialRecord::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal expense = transactionData.stream()
                .filter(r -> r instanceof Expense)
                .map(FinancialRecord::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal balance = income.subtract(expense);

        totalIncomeLabel.setText("Rp " + income.toPlainString());
        totalExpenseLabel.setText("Rp " + expense.toPlainString());
        balanceLabel.setText("Rp " + balance.toPlainString());
    }

    private void updateExpensePieChart() {
        expensePieChart.getData().clear();
        transactionData.stream()
                .filter(r -> r instanceof Expense)
                .map(r -> (Expense) r)
                .collect(Collectors.groupingBy(Expense::getCategoryName,
                        Collectors.reducing(BigDecimal.ZERO, Expense::getAmount, BigDecimal::add)))
                .forEach((categoryName, total) -> {
                    if (total.compareTo(BigDecimal.ZERO) > 0) {
                        expensePieChart.getData().add(new PieChart.Data(categoryName, total.doubleValue()));
                    }
                });
        expensePieChart.setTitle("Expenses by Category");
    }
}