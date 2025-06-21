package com.cdm.uas_pbo.controller;

import com.cdm.uas_pbo.Service.Database;
import com.cdm.uas_pbo.model.Category;
import com.cdm.uas_pbo.model.FinancialRecord;
import com.cdm.uas_pbo.model.User;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.TextInputDialog;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CategoriesTabController {
    @FXML private ListView<Category> categoryListView;

    private User currentUser;
    private ObservableList<FinancialRecord> transactionData;
    private ObservableList<Category> categoryData;

    public void initData(User user, ObservableList<FinancialRecord> transactionData, ObservableList<Category> categoryData) {
        this.currentUser = user;
        this.transactionData = transactionData;
        this.categoryData = categoryData;
        categoryListView.setItems(this.categoryData);
    }

    private void reloadAllData() {
        new TransactionsTabController().initData(currentUser, transactionData, categoryData);
        new TransactionsTabController().loadAllData();
    }

    @FXML private void handleAddCategory() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("New Category");
        dialog.setHeaderText("Add a new expense category.");
        dialog.setContentText("Category Name:");
        dialog.showAndWait().ifPresent(name -> {
            if (name.trim().isEmpty()) { return; }
            String sql = "INSERT INTO categories (user_id, name) VALUES (?, ?)";
            Connection conn = Database.getConnection();
            if (conn == null) return;
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, currentUser.getId());
                pstmt.setString(2, name.trim());
                pstmt.executeUpdate();
                categoryData.add(new Category(0, name.trim()));
                reloadAllData();
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Database Error", "Could not save category.");
            }
        });
    }

    @FXML private void handleEditCategory() {
        Category selected = categoryListView.getSelectionModel().getSelectedItem();
        if (selected == null) { return; }
        TextInputDialog dialog = new TextInputDialog(selected.getName());
        dialog.showAndWait().ifPresent(name -> {
            if (name.trim().isEmpty()) return;
            String sql = "UPDATE categories SET name = ? WHERE category_id = ?";
            Connection conn = Database.getConnection();
            if (conn == null) return;
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, name.trim());
                pstmt.setInt(2, selected.getId());
                pstmt.executeUpdate();
                reloadAllData();
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to update category.");
            }
        });
    }

    @FXML private void handleDeleteCategory() {
        Category selected = categoryListView.getSelectionModel().getSelectedItem();
        if (selected == null) { return; }
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Delete '" + selected.getName() + "'?", ButtonType.YES, ButtonType.NO);
        if (confirm.showAndWait().orElse(ButtonType.NO) == ButtonType.YES) {
            String sql = "DELETE FROM categories WHERE category_id = ?";
            Connection conn = Database.getConnection();
            if (conn == null) return;
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, selected.getId());
                pstmt.executeUpdate();
                reloadAllData();
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to delete category.");
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