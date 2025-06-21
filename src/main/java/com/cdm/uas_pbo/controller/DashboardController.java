package com.cdm.uas_pbo.controller;

import com.cdm.uas_pbo.Main;
import com.cdm.uas_pbo.model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.stage.Stage;

import java.io.IOException;

public class DashboardController {
    private User currentUser;

    @FXML
    private Label welcomeLabel;

    @FXML
    private DashboardTabController dashboardTabController;
    @FXML
    private TransactionsTabController transactionsTabController;
    @FXML
    private CategoriesTabController categoriesTabController;
    @FXML
    private ReportsTabController reportsTabController;

    public void setCurrentUser(User user) {
        this.currentUser = user;
        welcomeLabel.setText("Welcome, " + currentUser.getEmail() + "!");
        initSubControllers();
    }

    private void initSubControllers() {
        ObservableList<com.cdm.uas_pbo.model.FinancialRecord> transactionData = FXCollections.observableArrayList();
        ObservableList<com.cdm.uas_pbo.model.Category> categoryData = FXCollections.observableArrayList();

        dashboardTabController.initData(currentUser, transactionData, categoryData);
        transactionsTabController.initData(currentUser, transactionData, categoryData);
        categoriesTabController.initData(currentUser, transactionData, categoryData);
        reportsTabController.initData(currentUser, transactionData, categoryData);

        // Initial data load
        transactionsTabController.loadAllData();
    }

    @FXML
    private void handleLogout() {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("login-view.fxml"));
            Scene scene = new Scene(loader.load(), 450, 350);
            Stage stage = (Stage) welcomeLabel.getScene().getWindow();
            stage.setScene(scene);
            stage.centerOnScreen();
            stage.setTitle("DompetKu. - Login");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}