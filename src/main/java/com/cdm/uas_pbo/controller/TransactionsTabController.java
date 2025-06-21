package com.cdm.uas_pbo.controller;

import com.cdm.uas_pbo.Service.Database;
import com.cdm.uas_pbo.Main;
import com.cdm.uas_pbo.model.*;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class TransactionsTabController {
    @FXML private TableView<FinancialRecord> transactionTable;
    @FXML private TableColumn<FinancialRecord, LocalDate> dateCol;
    @FXML private TableColumn<FinancialRecord, String> descCol;
    @FXML private TableColumn<FinancialRecord, String> catCol;
    @FXML private TableColumn<FinancialRecord, String> typeCol;
    @FXML private TableColumn<FinancialRecord, BigDecimal> amountCol;

    private User currentUser;
    private ObservableList<FinancialRecord> transactionData;
    private ObservableList<Category> categoryData;

    public void initData(User user, ObservableList<FinancialRecord> transactionData, ObservableList<Category> categoryData) {
        this.currentUser = user;
        this.transactionData = transactionData;
        this.categoryData = categoryData;
        setupTableColumns();
    }

    private void setupTableColumns() {
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        descCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        amountCol.setCellValueFactory(new PropertyValueFactory<>("amount"));
        amountCol.setStyle("-fx-alignment: CENTER-RIGHT;");
        catCol.setCellValueFactory(cellData -> {
            if (cellData.getValue() instanceof Expense) {
                return new javafx.beans.property.SimpleStringProperty(((Expense) cellData.getValue()).getCategoryName());
            }
            return new javafx.beans.property.SimpleStringProperty("N/A");
        });
        transactionTable.setItems(transactionData);
    }

    public void loadAllData() {
        loadCategories();
        loadTransactions();
    }

    private void loadCategories() {
        categoryData.clear();
        String sql = "SELECT category_id, name FROM categories WHERE user_id = ? ORDER BY name";
        Connection conn = Database.getConnection();
        if (conn == null) return;
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, currentUser.getId());
            try (ResultSet rs = pstmt.executeQuery()) {
                while(rs.next()) { categoryData.add(new Category(rs.getInt("category_id"), rs.getString("name"))); }
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to load categories: " + e.getMessage());
        }
    }

    private void loadTransactions() {
        transactionData.clear();
        String sql = "SELECT * FROM transactions WHERE user_id = ? ORDER BY transaction_date DESC, transaction_id DESC";
        Connection conn = Database.getConnection();
        if (conn == null) return;
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, currentUser.getId());
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("transaction_id");
                    String desc = rs.getString("description");
                    BigDecimal amount = rs.getBigDecimal("amount");
                    LocalDate date = rs.getDate("transaction_date").toLocalDate();
                    String type = rs.getString("type");
                    if ("income".equals(type)) {
                        transactionData.add(new Income(id, desc, amount, date, currentUser.getId()));
                    } else {
                        int categoryId = rs.getInt("category_id");
                        Category category = categoryData.stream().filter(c -> c.getId() == categoryId).findFirst().orElse(null);
                        transactionData.add(new Expense(id, desc, amount, date, currentUser.getId(), category));
                    }
                }
            }
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to load transactions: " + e.getMessage());
        }
    }

    @FXML private void handleAddTransaction() { showTransactionDialog(null); }
    @FXML private void handleEditTransaction() {
        FinancialRecord selected = transactionTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a transaction to edit.");
            return;
        }
        showTransactionDialog(selected);
    }

    private void showTransactionDialog(FinancialRecord record) {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("transaction-dialog.fxml"));
            VBox page = loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle(record == null ? "Add Transaction" : "Edit Transaction");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(transactionTable.getScene().getWindow());
            TransactionDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setCategories(categoryData);
            controller.setCurrentUser(currentUser);
            controller.setTransaction(record);
            dialogStage.setScene(new Scene(page));
            dialogStage.centerOnScreen();
            dialogStage.showAndWait();
            if (controller.isOkClicked()) {
                loadTransactions(); // Reload transactions after add/edit
            }
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Dialog Error", "Could not open transaction dialog.");
        }
    }

    @FXML private void handleDeleteTransaction() {
        FinancialRecord selected = transactionTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a transaction to delete.");
            return;
        }
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Delete '" + selected.getDescription() + "'?", ButtonType.YES, ButtonType.NO);
        if (confirm.showAndWait().orElse(ButtonType.NO) == ButtonType.YES) {
            String sql = "DELETE FROM transactions WHERE transaction_id = ?";
            Connection conn = Database.getConnection();
            if (conn == null) return;
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, selected.getId());
                pstmt.executeUpdate();
                loadTransactions(); // Reload transactions after delete
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to delete transaction.");
            }
        }
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}