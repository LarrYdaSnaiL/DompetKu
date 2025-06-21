package com.cdm.uas_pbo.controller;

import com.cdm.uas_pbo.Service.Database;
import com.cdm.uas_pbo.Main;
import com.cdm.uas_pbo.model.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LoginController {
    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label statusLabel;

    @FXML
    protected void handleLogin() {
        String email = emailField.getText();
        String password = passwordField.getText();
        if (email.isEmpty() || password.isEmpty()) {
            statusLabel.setText("Email and password cannot be empty.");
            return;
        }
        String sql = "SELECT user_id, email FROM users WHERE email = ? AND password_hash = crypt(?, password_hash)";
        Connection conn = Database.getConnection();
        if (conn == null) {
            statusLabel.setText("Database connection error.");
            return;
        }

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);
            pstmt.setString(2, password);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) { // Kalo Ketemu
                    User currentUser = new User(rs.getInt("user_id"), rs.getString("email"));
                    loadDashboard(currentUser);
                } else { // Kalo Ga Ketemu
                    statusLabel.setText("Invalid email or password.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            statusLabel.setText("An error occurred during login.");
        }
    }

    @FXML
    protected void handleRegister() {
        String email = emailField.getText();
        String password = passwordField.getText();
        if (email.isEmpty() || password.isEmpty() || password.length() < 6) {
            statusLabel.setText("Email and password (min 6 chars) are required.");
            return;
        }
        String sql = "INSERT INTO users (email, password_hash) VALUES (?, crypt(?, gen_salt('bf')))";
        Connection conn = Database.getConnection();
        if (conn == null) {
            statusLabel.setText("Database connection error.");
            return;
        }
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);
            pstmt.setString(2, password);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) { // Kalo Berhasil
                statusLabel.setText("Registration successful! Please log in.");
            } else { // Kalo Gagal
                statusLabel.setText("Registration failed.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            statusLabel.setText("Registration failed. Email might already exist.");
        }
    }

    private void loadDashboard(User user) throws Exception {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("dashboard-view.fxml"));
        Parent root = loader.load();
        DashboardController dashboardController = loader.getController();
        dashboardController.setCurrentUser(user);
        Stage stage = (Stage) emailField.getScene().getWindow();
        stage.setScene(new Scene(root, 1100, 700));
        stage.setTitle("DompetKu. - " + user.getEmail());
        stage.setResizable(true);
    }
}