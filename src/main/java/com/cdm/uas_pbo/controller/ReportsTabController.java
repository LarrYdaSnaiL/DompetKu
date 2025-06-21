package com.cdm.uas_pbo.controller;

import com.cdm.uas_pbo.model.*;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ReportsTabController {
    @FXML private ComboBox<String> reportMonthCombo;
    @FXML private TextArea reportTextArea;

    private ObservableList<FinancialRecord> transactionData;

    public void initData(User user, ObservableList<FinancialRecord> transactionData, ObservableList<Category> categoryData) {
        this.transactionData = transactionData;
        populateReportMonths();
    }

    private void populateReportMonths() {
        int currentYear = LocalDate.now().getYear();
        for (int i = 1; i <= 12; i++) {
            String monthName = LocalDate.of(currentYear, i, 1).getMonth().name();
            reportMonthCombo.getItems().add(String.format("%02d - %s", i, monthName));
        }
    }

    @FXML private void handleGenerateReport() {
        if (reportMonthCombo.getValue() == null) {
            showAlert(Alert.AlertType.WARNING, "Input Error", "Please select a month.");
            return;
        }
        int month = Integer.parseInt(reportMonthCombo.getValue().substring(0, 2));
        int year = LocalDate.now().getYear();
        StringBuilder report = new StringBuilder();
        report.append(String.format("--- Financial Report for %s %d ---\n\n", LocalDate.of(year, month, 1).getMonth(), year));
        report.append("INCOME:\n");
        BigDecimal monthlyIncome = transactionData.stream().filter(r -> r.getDate().getYear() == year && r.getDate().getMonthValue() == month && r instanceof Income).peek(r -> report.append(String.format("- %s: %s (Rp %s)\n", r.getDate(), r.getDescription(), r.getAmount()))).map(FinancialRecord::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
        report.append("\nEXPENSES:\n");
        BigDecimal monthlyExpense = transactionData.stream().filter(r -> r.getDate().getYear() == year && r.getDate().getMonthValue() == month && r instanceof Expense).map(r -> (Expense) r).peek(e -> report.append(String.format("- %s: %s [%s] (Rp %s)\n", e.getDate(), e.getDescription(), e.getCategoryName(), e.getAmount()))).map(FinancialRecord::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
        report.append("\n--- SUMMARY ---\n");
        report.append(String.format("Total Income: Rp %s\n", monthlyIncome.toPlainString()));
        report.append(String.format("Total Expense: Rp %s\n", monthlyExpense.toPlainString()));
        report.append(String.format("Net Flow: Rp %s\n", monthlyIncome.subtract(monthlyExpense).toPlainString()));
        reportTextArea.setText(report.toString());
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}