package com.cdm.uas_pbo.controller;

import com.cdm.uas_pbo.Service.Database;
import com.cdm.uas_pbo.model.*;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;

public class TransactionDialogController {
    @FXML
    private ToggleGroup transactionType;
    @FXML
    private RadioButton incomeRadio;
    @FXML
    private RadioButton expenseRadio;
    @FXML
    private TextField descriptionField;
    @FXML
    private TextField amountField;
    @FXML
    private DatePicker datePicker;
    @FXML
    private ComboBox<Category> categoryComboBox;
    private Stage dialogStage;
    private User currentUser;
    private FinancialRecord transaction;
    private boolean okClicked = false;

    @FXML
    private void initialize() {
        transactionType.selectedToggleProperty().addListener((obs, oldVal, newVal) -> {
            boolean isExpense = newVal == expenseRadio;
            categoryComboBox.setDisable(!isExpense);
            if (!isExpense) {
                categoryComboBox.getSelectionModel().clearSelection();
            }
        });
        datePicker.setValue(LocalDate.now());
    }

    public void setDialogStage(Stage stage) {
        this.dialogStage = stage;
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    public void setCategories(ObservableList<Category> categories) {
        categoryComboBox.setItems(categories);
    }

    public boolean isOkClicked() {
        return okClicked;
    }

    public void setTransaction(FinancialRecord record) {
        this.transaction = record;
        if (record != null) {
            descriptionField.setText(record.getDescription());
            amountField.setText(record.getAmount().toPlainString());
            datePicker.setValue(record.getDate());
            if (record instanceof Expense) {
                expenseRadio.setSelected(true);
                categoryComboBox.setValue(((Expense) record).getCategory());
            } else {
                incomeRadio.setSelected(true);
            }
        }
    }

    @FXML
    private void handleOk() {
        if (isInputValid()) {
            saveTransaction();
            okClicked = true;
            dialogStage.close();
        }
    }

    private void saveTransaction() {
        String type = incomeRadio.isSelected() ? "income" : "expense";
        String sql = (transaction == null) ? "INSERT INTO transactions (user_id, category_id, description, amount, type, transaction_date) VALUES (?, ?, ?, ?, ?, ?)" : "UPDATE transactions SET category_id = ?, description = ?, amount = ?, type = ?, transaction_date = ? WHERE transaction_id = ?";
        Connection conn = Database.getConnection();
        if (conn == null) {
            showAlert("Database Error", "Cannot connect to the database.");
            return;
        }
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            Category selectedCategory = categoryComboBox.getValue();
            if (transaction == null) {
                pstmt.setInt(1, currentUser.getId());
                if (type.equals("expense") && selectedCategory != null) {
                    pstmt.setInt(2, selectedCategory.getId());
                } else {
                    pstmt.setNull(2, Types.INTEGER);
                }
                pstmt.setString(3, descriptionField.getText());
                pstmt.setBigDecimal(4, new BigDecimal(amountField.getText()));
                pstmt.setString(5, type);
                pstmt.setDate(6, Date.valueOf(datePicker.getValue()));
            } else {
                if (type.equals("expense") && selectedCategory != null) {
                    pstmt.setInt(1, selectedCategory.getId());
                } else {
                    pstmt.setNull(1, Types.INTEGER);
                }
                pstmt.setString(2, descriptionField.getText());
                pstmt.setBigDecimal(3, new BigDecimal(amountField.getText()));
                pstmt.setString(4, type);
                pstmt.setDate(5, Date.valueOf(datePicker.getValue()));
                pstmt.setInt(6, transaction.getId());
            }
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Database Error", "Failed to save transaction: " + e.getMessage());
        }
    }

    @FXML
    private void handleCancel() {
        dialogStage.close();
    }

    private boolean isInputValid() {
        String errorMessage = "";
        if (descriptionField.getText() == null || descriptionField.getText().trim().isEmpty()) {
            errorMessage += "Description cannot be empty.\n";
        }
        if (amountField.getText() == null || amountField.getText().trim().isEmpty()) {
            errorMessage += "Amount cannot be empty.\n";
        } else {
            try {
                if (new BigDecimal(amountField.getText()).compareTo(BigDecimal.ZERO) <= 0) {
                    errorMessage += "Amount must be a positive number.\n";
                }
            } catch (NumberFormatException e) {
                errorMessage += "Invalid amount format (e.g., 50000.00).\n";
            }
        }
        if (datePicker.getValue() == null) {
            errorMessage += "Date cannot be empty.\n";
        }
        if (expenseRadio.isSelected() && categoryComboBox.getValue() == null) {
            errorMessage += "An expense must have a category.\n";
        }
        if (errorMessage.isEmpty()) {
            return true;
        } else {
            showAlert("Invalid Fields", errorMessage);
            return false;
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.initOwner(dialogStage);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}